package org.opeum.metamodel.activities;
import java.util.Set;

import org.opeum.metamodel.commonbehaviors.INakedStep;
import org.opeum.metamodel.core.INakedElement;
public interface INakedActivityNode extends INakedElement,INakedStep{
	INakedActivity getActivity();
	INakedActivityPartition getInPartition();
	void setInPartition(INakedActivityPartition s);
	Set<INakedActivityEdge> getOutgoing();
	Set<INakedActivityEdge> getIncoming();
	/**
	 * Returns all the outgoing edges for this node and all its contained pins
	 * @return
	 */
	Set<INakedActivityEdge> getAllEffectiveOutgoing();
	/**
	 * Returns all the incoming edges for this node and all its contained pins
	 * @return
	 */
	Set<INakedActivityEdge> getAllEffectiveIncoming();
	void addIncoming(INakedActivityEdge edge);
	void addOutgoing(INakedActivityEdge edge);
	Set<INakedActivityEdge> getConditionalOutgoing();
	Set<INakedActivityEdge> getDefaultOutgoing();
	boolean isImplicitFork();
	boolean isImplicitDecision();
	boolean isImplicitJoin();
	INakedStructuredActivityNode getInStructuredNode();
	public abstract void removeOutgoing(INakedActivityEdge edge);
	public abstract void removeIncoming(INakedActivityEdge edge);
	Set<INakedActivityNode> getRedefinedNodes();	
	
}