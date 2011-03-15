package net.sf.nakeduml.javageneration.hibernate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.AbstractJavaProducingVisitor;
import net.sf.nakeduml.javageneration.JavaTextSource.OutputRootId;
import net.sf.nakeduml.javageneration.NakedClassifierMap;
import net.sf.nakeduml.linkage.GeneralizationUtil;
import net.sf.nakeduml.metamodel.core.INakedElement;
import net.sf.nakeduml.metamodel.core.INakedEntity;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.core.INakedRootObject;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;

import org.hibernate.dialect.Dialect;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedPackage;
import org.nakeduml.java.metamodel.annotation.OJAnnotationAttributeValue;
import org.nakeduml.java.metamodel.annotation.OJAnnotationValue;

public abstract class AbstractHibernatePackageAnnotator extends AbstractJavaProducingVisitor {
	private boolean isIntegrationPhase;

	public final class InterfaceCollector extends AbstractJavaProducingVisitor {
		Set<INakedInterface> interfaces = new HashSet<INakedInterface>();

		@VisitBefore
		public void visitInterface(INakedInterface i) {
			interfaces.add(i);
		}
	}

	public AbstractHibernatePackageAnnotator(boolean isIntegrationPhase) {
		super();
		this.isIntegrationPhase = isIntegrationPhase;
	}

	public abstract void visitWorkspace(INakedModelWorkspace root);

	public abstract void visitModel(INakedModel model);

	protected void doWorkspace(INakedModelWorkspace workspace) {
		if (isIntegrationPhase) {
			applyFilter(true, OutputRootId.INTEGRATED_ADAPTOR_GEN_SRC);
			Collection<? extends INakedElement> ownedElements = workspace.getOwnedElements();
			Set<INakedInterface> interfaces = collectInterfaces((ownedElements));
			for (INakedInterface i : interfaces) {
				doInterface(i, GeneralizationUtil.getConcreteEntityImplementationsOf(i, (Collection<INakedRootObject>) ownedElements), true,
						OutputRootId.INTEGRATED_ADAPTOR_GEN_SRC);
			}
		}
	}

	protected void applyFilter(boolean isAdaptor, OutputRootId outputRoot) {
		OJAnnotatedPackage ap = findOrCreatePackage(HibernateUtil.getHibernatePackage(isAdaptor));
		OJAnnotationValue filterDef = new OJAnnotationValue(new OJPathName("org.hibernate.annotations.FilterDef"));
		filterDef.putAttribute(new OJAnnotationAttributeValue("name", "noDeletedObjects"));
		filterDef.putAttribute(new OJAnnotationAttributeValue("defaultCondition", "deleted_on > " + getCurrentTimestampSQLFunction()));
		ap.putAnnotation(filterDef);
		createTextPathIfRequired(ap, outputRoot);
	}

	private String getCurrentTimestampSQLFunction() {
		Dialect dialect = HibernateUtil.getHibernateDialect(this.config);
		return dialect.getCurrentTimestampSQLFunctionName();
	}

	protected void doModel(INakedModel model) {
		if (!isIntegrationPhase) {
			applyFilter(true, OutputRootId.ADAPTOR_GEN_TEST_SRC);
			applyFilter(false, OutputRootId.DOMAIN_GEN_TEST_SRC);
			Collection<INakedRootObject> selfAndDependencies = getModelInScope();
			Set<INakedInterface> interfaces = collectInterfaces(selfAndDependencies);
			for (INakedInterface i : interfaces) {
				doInterface(i, GeneralizationUtil.getConcreteEntityImplementationsOf(i, selfAndDependencies), true, OutputRootId.ADAPTOR_GEN_TEST_SRC);
				doInterface(i, GeneralizationUtil.getConcreteEntityImplementationsOf(i, selfAndDependencies), false, OutputRootId.DOMAIN_GEN_TEST_SRC);
			}
		}
	}

	// TODO find another place for this
	private Set<INakedInterface> collectInterfaces(Collection<? extends INakedElement> ownedElements) {
		InterfaceCollector collector = new InterfaceCollector();
		for (INakedElement e : ownedElements) {
			if (e instanceof INakedModel) {
				INakedModel model = (INakedModel) e;
				collector.visitRecursively(model);
			}
		}
		Set<INakedInterface> interfaces = collector.interfaces;
		return interfaces;
	}

	private void doInterface(INakedInterface i, Collection<INakedEntity> impls, boolean isAdaptor, OutputRootId outputRoot) {
		OJAnnotationValue metaDef = new OJAnnotationValue(new OJPathName("org.hibernate.annotations.AnyMetaDef"));
		OJAnnotatedPackage p = (OJAnnotatedPackage) this.findOrCreatePackage(HibernateUtil.getHibernatePackage(isAdaptor));
		createTextPathIfRequired(p, outputRoot);
		OJAnnotationValue anyMetaDefs = getAnyMetaDefs(p);
		anyMetaDefs.addAnnotationValue(metaDef);
		metaDef.putAttribute("name", getMetaDefName(i));
		metaDef.putAttribute("metaType", "string");
		metaDef.putAttribute("idType", getIdType());
		OJAnnotationAttributeValue metaValues = new OJAnnotationAttributeValue("metaValues");
		metaDef.putAttribute(metaValues);
		for (INakedEntity iNakedEntity : impls) {
			OJAnnotationValue metaValue = new OJAnnotationValue(new OJPathName("org.hibernate.annotations.MetaValue"));
			NakedClassifierMap map = new NakedClassifierMap(iNakedEntity);
			OJPathName javaTypePath = map.javaTypePath();
			metaValue.putAttribute("value", javaTypePath.toString());
			metaValue.putAttribute("targetEntity", getTargetEntity(javaTypePath));
			metaValues.addAnnotationValue(metaValue);
		}
	}

	protected abstract OJPathName getTargetEntity(OJPathName javaTypePath);

	protected abstract String getIdType();

	protected abstract String getMetaDefName(INakedInterface i);

	private OJAnnotationValue getAnyMetaDefs(OJAnnotatedPackage p) {
		OJAnnotationValue anyMetaDefs = p.findAnnotation(new OJPathName("org.hibernate.annotations.AnyMetaDefs"));
		if (anyMetaDefs == null) {
			anyMetaDefs = new OJAnnotationValue(new OJPathName("org.hibernate.annotations.AnyMetaDefs"));
			p.putAnnotation(anyMetaDefs);
		}
		return anyMetaDefs;
	}
}