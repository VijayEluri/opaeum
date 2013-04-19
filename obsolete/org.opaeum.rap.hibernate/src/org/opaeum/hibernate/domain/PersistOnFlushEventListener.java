package org.opaeum.hibernate.domain;

import org.hibernate.engine.spi.CascadingAction;

public class PersistOnFlushEventListener extends PersistEventListener{
	private static final long serialVersionUID = -608108611465616998L;
	@Override
	protected CascadingAction getCascadeAction(){
		return CascadingAction.PERSIST_ON_FLUSH;
	}
}
