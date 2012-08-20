package org.opaeum.runtime.statemachines;

import java.util.HashSet;
import java.util.Set;

import org.opaeum.runtime.domain.IProcessStep;

public abstract class StateActivation<SME extends IStateMachineExecution,T extends IStateMachineToken<SME>> extends VertexActivation<SME,T> implements
		IProcessStep{
	private Set<TransitionInstance<SME,T>> outgoing = new HashSet<TransitionInstance<SME,T>>();
	protected Set<RegionActivation<SME,T>> regions = new HashSet<RegionActivation<SME,T>>();
	public StateActivation(String id,RegionActivation<SME,T> region){
		super(id, region);
	}
	public boolean contains(VertexActivation<SME,T> other){
		for(RegionActivation<SME,T> r:regions){
			if(r.contains(other)){
				return true;
			}
		}
		return false;
	}
	public void enter(T token,VertexActivation<SME,T> target){
		super.enter(token, target);
		for(RegionActivation<SME,T> ra:regions){
			if(ra.contains(target)){
				ra.enter(token, target);
			}else{
				ra.initiate(token);
			}
		}
	}
	public void addOutgoing(TransitionInstance<SME,T> ti){
		outgoing.add(ti);
	}
	protected void exit(T token){
		super.exit(token);
		onExit();
	}
	public void onExit(){
	}
	public Set<RegionActivation<SME,T>> getRegions(){
		return regions;
	}
	public void setRegions(Set<RegionActivation<SME,T>> regions){
		this.regions = regions;
	}
	public boolean onCompletion(){
		return true;
	}
}
