package structuredbusiness;

import org.opaeum.runtime.bpm.organization.PersonNode;
import org.opaeum.runtime.bpm.request.Participation;
import org.opaeum.runtime.domain.CompositionNode;
import org.opaeum.simulation.EntityInstanceSimulation;
import org.opaeum.simulation.SimulationMetaData;

public class AccountantActualInstance4_1XLwb349EeGY3962KIdJNA extends EntityInstanceSimulation {
	static final private Accountant ACCOUNTANTACTUALINSTANCE4 = new Accountant();
	static final public AccountantActualInstance4_1XLwb349EeGY3962KIdJNA INSTANCE = new AccountantActualInstance4_1XLwb349EeGY3962KIdJNA();


	public Accountant createNewObject(CompositionNode parent) {
		Accountant result = ACCOUNTANTACTUALINSTANCE4;
		result.init(parent);
		result.set__name("__name4");
		result.addToOwningObject();
		return result;
	}
	
	public void populateReferences(CompositionNode in) {
		Accountant instance = (Accountant)in;
		int participationCount = 0;
		while ( participationCount<SimulationMetaData.getInstance().getNextPropertySize("demo1_201204041206::DishwashersIncActualInstance1::accountant::null::AccountantActualInstance4","participation") ) {
			participationCount++;
			instance.addToParticipation((Participation)SimulationMetaData.getInstance().getEntityValueProvider("demo1_201204041206::DishwashersIncActualInstance1::accountant::null::AccountantActualInstance4","participation").getNextReference());
		}
		int representedPersonCount = 0;
		while ( representedPersonCount<1 ) {
			representedPersonCount++;
			instance.setRepresentedPerson((PersonNode)SimulationMetaData.getInstance().getEntityValueProvider("demo1_201204041206::DishwashersIncActualInstance1::accountant::null::AccountantActualInstance4","representedPerson").getNextReference());
		}
	}

}