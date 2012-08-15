package org.opaeum.audit;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Lifecycle;
import org.hibernate.ejb.event.EJB3FlushEntityEventListener;
import org.hibernate.ejb.event.EntityCallbackHandler;
import org.hibernate.engine.EntityEntry;
import org.hibernate.engine.Versioning;
import org.hibernate.event.EventSource;
import org.hibernate.event.FlushEntityEvent;
import org.hibernate.event.FlushEntityEventListener;
import org.hibernate.event.FlushEvent;
import org.hibernate.event.FlushEventListener;
import org.hibernate.event.Initializable;
import org.hibernate.event.PersistEvent;
import org.hibernate.event.PersistEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.def.AbstractFlushingEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.opaeum.hibernate.domain.AbstractHibernatePersistence;
import org.opaeum.hibernate.domain.CascadingInterfaceValue;
import org.opaeum.hibernate.domain.InterfaceValue;
import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.domain.IntrospectionUtil;
import org.opaeum.runtime.persistence.AbstractPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditListener extends AbstractFlushingEventListener implements PostInsertEventListener,PostLoadEventListener,
		PostUpdateEventListener,FlushEventListener,Initializable,PersistEventListener,FlushEntityEventListener{
	private static Map<EventSource,AbstractHibernatePersistence> persistenceMap = Collections
			.synchronizedMap(new WeakHashMap<EventSource,AbstractHibernatePersistence>());
	private static final long serialVersionUID = -233067098331332700L;
	private static final Logger log = LoggerFactory.getLogger(AuditListener.class);
	private static final Map<EventSource,AuditWorkUnit> entries = Collections.synchronizedMap(new HashMap<EventSource,AuditWorkUnit>());
	private EJB3FlushEntityEventListener ejb3FlushEntityEventListener;
	@Override
	public void onPostUpdate(PostUpdateEvent event){
		Object entity = event.getEntity();
		EntityPersister persister = event.getPersister();
		EventSource session = event.getSession();
		if(entity instanceof IPersistentObject && IntrospectionUtil.getOriginalClass(entity).isAnnotationPresent(AuditMe.class)){
			int[] d = persister.findDirty(event.getState(), event.getOldState(), entity, session);
			if(d != null && d.length > 0){
				Number n = (Number) Versioning.getVersion(event.getState(), persister);
				getWorkUnitForSession(session).logPropertyChanges(event.getOldState(), event.getState(), d, (IPersistentObject) entity,
						persister.getPropertyNames(), n.intValue());
			}
		}
	}
	@Override
	public void onPostInsert(PostInsertEvent event){
		Object entity = event.getEntity();
		EntityPersister persister = event.getPersister();
		EventSource session = event.getSession();
		if(entity instanceof IPersistentObject && IntrospectionUtil.getOriginalClass(entity).isAnnotationPresent(AuditMe.class)){
			Number n = (Number) Versioning.getVersion(event.getState(), persister);
			getWorkUnitForSession(session).logInsertedProperties(event.getState(), persister.getPropertyNames(), (IPersistentObject) entity,
					n.intValue());
		}
	}
	private AuditWorkUnit getWorkUnitForSession(EventSource session){
		AuditWorkUnit map = entries.get(session);
		if(map == null){
			map = new AuditWorkUnit(session);
			entries.put(session, map);
		}
		return map;
	}
	@Override
	public void onFlush(FlushEvent event) throws HibernateException{
		final EventSource source = event.getSession();
		if(source.getPersistenceContext().hasNonReadOnlyEntities()){
			flushEverythingToExecutions(event);
			performExecutions(source);
			postFlush(source);
			if(source.getFactory().getStatistics().isStatisticsEnabled()){
				source.getFactory().getStatisticsImplementor().flush();
			}
			getWorkUnitForSession(source).flush();
		}
		entries.remove(source);
	}
	protected void performExecutions(EventSource session) throws HibernateException{
		log.trace("executing flush");
		session.getPersistenceContext().setFlushing(true);
		try{
			session.getJDBCContext().getConnectionManager().flushBeginning();
			// we need to lock the collection caches before
			// executing entity inserts/updates in order to
			// account for bidi associations
			session.getActionQueue().prepareActions();
			session.getActionQueue().executeActions();
		}catch(HibernateException he){
			// log.error("Could not synchronize database state with session",
			// he);
			throw he;
		}finally{
			session.getPersistenceContext().setFlushing(false);
			session.getJDBCContext().getConnectionManager().flushEnding();
		}
	}
	@Override
	public void initialize(Configuration cfg){
		cfg.getEventListeners().getPostLoadEventListeners()[1] = this;
		// hahahahahahahahaha
	}
	@SuppressWarnings("rawtypes")
	public void onPersist(PersistEvent event,Map createdAlready) throws HibernateException{
		EventSource session = event.getSession();
		String entityName = event.getEntityName();
		EntityPersister p = session.getEntityPersister(entityName, event.getObject());
		Object[] propertyValues = p.getPropertyValues(event.getObject(), EntityMode.POJO);
		doInterfaceValues(createdAlready, session, propertyValues);
	}
	@SuppressWarnings("rawtypes")
	private void doInterfaceValues(Map createdAlready,EventSource session,Object[] propertyValues){
		session.getSessionFactory().getAllClassMetadata();
		for(Object object2:propertyValues){
			if(object2 instanceof InterfaceValue){
				InterfaceValue iv = (InterfaceValue) object2;
				if(iv.hasValue() && iv.getIdentifier() == null){
					IPersistentObject value = iv.getValue(getPersistence(session));
					if(iv instanceof CascadingInterfaceValue){
						String entityName = IntrospectionUtil.getOriginalClass(value).getSimpleName();
						session.persistOnFlush(entityName, value, new HashMap());
						EntityEntry entry = session.getPersistenceContext().getEntry(value);
						getEjb3FlushEntityEventListener().onFlushEntity(new FlushEntityEvent(session, value, entry));
					}
					iv.setValue(value);// Populate the id
				}
			}
		}
	}
	private EJB3FlushEntityEventListener getEjb3FlushEntityEventListener(){
		if(ejb3FlushEntityEventListener == null){
			ejb3FlushEntityEventListener = new EJB3FlushEntityEventListener(new EntityCallbackHandler());
		}
		return ejb3FlushEntityEventListener;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void onPersist(PersistEvent event) throws HibernateException{
		this.onPersist(event, new HashMap());
	}
	private AbstractPersistence getPersistence(EventSource session){
		AbstractHibernatePersistence persistence = persistenceMap.get(session);
		if(persistence == null){
			persistence = new AbstractHibernatePersistence(session){
			};
			persistenceMap.put(session, persistence);
		}
		return persistence;
	}
	@Override
	public void onPostLoad(PostLoadEvent event){
		// NB!!! Don't touch this code - copied from hibernate
		if(event.getPersister().implementsLifecycle(event.getSession().getEntityMode())){
			// log.debug( "calling onLoad()" );
			((Lifecycle) event.getEntity()).onLoad(event.getSession(), event.getId());
		}
		if(event.getEntity() instanceof AuditEntry){
			AuditEntry ae = (AuditEntry) event.getEntity();
			ae.setOriginal((IPersistentObject) event.getSession().load(ae.getOriginalClass(), ae.getOriginalId()));
		}else if(event.getEntity() instanceof PropertyChange){
			PropertyChange<?> c = (PropertyChange<?>) event.getEntity();
			c.resolve(event.getSession());
		}
		try{
			Field declaredField = event.getPersister().getMappedClass(EntityMode.POJO).getDeclaredField("persistence");
			if(declaredField != null){
				declaredField.setAccessible(true);
				declaredField.set(event.getEntity(), getPersistence(event.getSession()));
			}
		}catch(NoSuchFieldException e){
		}catch(RuntimeException re){
			throw re;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void onFlushEntity(FlushEntityEvent event) throws HibernateException{
		if(event.getEntity() instanceof IPersistentObject){
			EventSource session = event.getSession();
			Object[] propertyValues = event.getPropertyValues();
			doInterfaceValues(new HashMap(), session, propertyValues);
		}
	}
}
