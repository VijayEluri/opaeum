package org.opaeum.hibernate.domain;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.opaeum.runtime.domain.IBehaviorExecution;
import org.opaeum.runtime.domain.IExecutionElement;
import org.opaeum.runtime.domain.IToken;

@MappedSuperclass
public abstract class AbstractToken <BE extends IBehaviorExecution >implements  IToken<BE>{
	private static final long serialVersionUID = 5721390964740553738L;
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.TABLE)
	private Long id;
	@Basic
	private boolean isActive=true;
	@Basic
	private String currentElementId;
	@Basic
	private boolean hasRunToCompletion = false;
	@SuppressWarnings("unchecked")
	@Override
	public IExecutionElement<BE> getCurrentExecutionElement() {
		BE behaviorExecution = getBehaviorExecution();
		return behaviorExecution.getExecutionElements().get(
				currentElementId);
	}
	public IToken<BE> getInnermostNonParallelToken(){
		if(getChildTokens().size()>1 || getChildTokens().isEmpty()){
			return this;
		}else{
			return getChildTokens().iterator().next().getInnermostNonParallelToken();
		}
	}

	@Override
	public void transferTo(@SuppressWarnings("rawtypes") IExecutionElement vertexActivation) {
		currentElementId = vertexActivation.getId();
	}
	@Override
	public boolean isActive(){
		return isActive;
	}

	@Override
	public void setActive(boolean isActive){
		this.isActive = isActive;
	}
	@Override
	public boolean isHasRunToCompletion() {
		return hasRunToCompletion;
	}

	@Override
	public void setHasRunToCompletion(boolean hasRunToCompletion) {
		this.hasRunToCompletion = hasRunToCompletion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getUid() {
		return "";
	}

	@Override
	public int getObjectVersion() {
		return 0;
	}
}
