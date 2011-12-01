package net.sf.nakeduml.metamodel.core;


public interface ICompositionParticipant extends INakedClassifier{
	boolean hasComposite();
	INakedProperty getEndToComposite();
	void setEndToComposite(INakedProperty artificialProperty);
}