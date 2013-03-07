package org.opaeum.demo1.structuredbusiness.branch;

import org.opaeum.demo1.structuredbusiness.jobs.Job;
import org.opaeum.runtime.bpm.organization.OrganizationNode;
import org.opaeum.runtime.bpm.request.Participation;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.simulation.EntityInstanceSimulation;
import org.opaeum.simulation.SimulationMetaData;

public class BranchActualInstance2_6z4q8H_JEeK5usaVqVCtXw extends EntityInstanceSimulation<Branch> {
	static final public Branch BRANCHACTUALINSTANCE2 = new Branch();
	static final public BranchActualInstance2_6z4q8H_JEeK5usaVqVCtXw INSTANCE = new BranchActualInstance2_6z4q8H_JEeK5usaVqVCtXw();


	public Branch createNewObject(CompositionNode parent) throws Exception {
		Branch result = BRANCHACTUALINSTANCE2;
		result.init(parent);
		int customerAssistantCount = 0;
		while ( customerAssistantCount<3 ) {
			customerAssistantCount++;
			SimulationMetaData.getInstance().getEntityValueProvider("demo1_201302260606::BusinessNetworkActualInstance1::businessCollaboration::ApplianceCollaborationContainedActualInstance::ApplianceCollaborationActualInstance1::applianceDoctor::ApplianceDoctorContainedActualInstance::ApplianceDoctorActualInstance1::branch::BranchContainedActualInstance::BranchActualInstance2","customerAssistant").createNewInstance(result);
		}
		result.setCity(City.JOHANNESBURG);
		int technicianCount = 0;
		while ( technicianCount<3 ) {
			technicianCount++;
			SimulationMetaData.getInstance().getEntityValueProvider("demo1_201302260606::BusinessNetworkActualInstance1::businessCollaboration::ApplianceCollaborationContainedActualInstance::ApplianceCollaborationActualInstance1::applianceDoctor::ApplianceDoctorContainedActualInstance::ApplianceDoctorActualInstance1::branch::BranchContainedActualInstance::BranchActualInstance2","technician").createNewInstance(result);
		}
		int jobCount = 0;
		while ( jobCount<3 ) {
			jobCount++;
			SimulationMetaData.getInstance().getEntityValueProvider("demo1_201302260606::BusinessNetworkActualInstance1::businessCollaboration::ApplianceCollaborationContainedActualInstance::ApplianceCollaborationActualInstance1::applianceDoctor::ApplianceDoctorContainedActualInstance::ApplianceDoctorActualInstance1::branch::BranchContainedActualInstance::BranchActualInstance2","job").createNewInstance(result);
		}
		result.setName("name2");
		result.setNumberOfOpenPositions(0.5);
		result.addToOwningObject();
		return result;
	}
	
	public void populateReferences(CompositionNode in) throws Exception {
		Branch instance = (Branch)in;
		int participationCount = 0;
		while ( participationCount<SimulationMetaData.getInstance().getNextPropertySize("demo1_201302260606::BusinessNetworkActualInstance1::businessCollaboration::ApplianceCollaborationContainedActualInstance::ApplianceCollaborationActualInstance1::applianceDoctor::ApplianceDoctorContainedActualInstance::ApplianceDoctorActualInstance1::branch::BranchContainedActualInstance::BranchActualInstance2","participation") ) {
			Participation newVal = (Participation)SimulationMetaData.getInstance().getEntityValueProvider("demo1_201302260606::BusinessNetworkActualInstance1::businessCollaboration::ApplianceCollaborationContainedActualInstance::ApplianceCollaborationActualInstance1::applianceDoctor::ApplianceDoctorContainedActualInstance::ApplianceDoctorActualInstance1::branch::BranchContainedActualInstance::BranchActualInstance2","participation").getNextReference();
			participationCount++;
			if ( newVal != null && newVal.getParticipant() ==null ) {
				instance.addToParticipation(newVal);
			}
		}
		int representedOrganizationCount = 0;
		while ( representedOrganizationCount<1 ) {
			OrganizationNode newVal = (OrganizationNode)SimulationMetaData.getInstance().getEntityValueProvider("demo1_201302260606::BusinessNetworkActualInstance1::businessCollaboration::ApplianceCollaborationContainedActualInstance::ApplianceCollaborationActualInstance1::applianceDoctor::ApplianceDoctorContainedActualInstance::ApplianceDoctorActualInstance1::branch::BranchContainedActualInstance::BranchActualInstance2","representedOrganization").getNextReference();
			representedOrganizationCount++;
			if ( instance.getRepresentedOrganization() == null ) {
				instance.setRepresentedOrganization(newVal);
			}
		}
	}

}