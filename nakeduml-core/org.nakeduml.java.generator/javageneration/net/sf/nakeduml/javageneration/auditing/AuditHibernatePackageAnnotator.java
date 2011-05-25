package net.sf.nakeduml.javageneration.auditing;

import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.hibernate.AbstractHibernatePackageAnnotator;
import net.sf.nakeduml.javageneration.hibernate.HibernateUtil;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;

import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.runtime.domain.AuditId;

public class AuditHibernatePackageAnnotator extends AbstractHibernatePackageAnnotator {
	public AuditHibernatePackageAnnotator(boolean isIntegrationPhase) {
		super(isIntegrationPhase);
	}

	public AuditHibernatePackageAnnotator() {
		super(true);
	}

	protected String getMetaDefNameSuffix() {
		return "Audit";
	}

	protected String getIdType() {
		return AuditId.class.getName();
	}

	protected OJPathName getTargetEntity(OJPathName javaTypePath) {
		return new OJPathName(javaTypePath + "_Audit");
	}

	@Override
	@VisitBefore
	public void visitWorkspace(INakedModelWorkspace root) {
		doWorkspace(root);
	}

	@VisitBefore
	@Override
	public void visitModel(INakedModel model) {
		doModel(model);
	}
}
