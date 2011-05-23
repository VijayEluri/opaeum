package org.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class BaseTinkerSoftDelete extends BaseTinker implements Serializable{

	private static final long serialVersionUID = 3751023772087546585L;
	protected Vertex auditVertex;
	
	public Vertex getAuditVertex() {
		return auditVertex;
	}
	public BaseTinkerSoftDelete() {
		super();
	}

	public Date getDeletedOn() {
		return TinkerFormatter.parse((String)this.vertex.getProperty("deletedOn"));
	}
	public void setDeletedOn(Date deletedOn) {
		this.vertex.setProperty("deletedOn", TinkerFormatter.format(deletedOn));
	}


	public void defaultCreate() {
		super.defaultCreate();
		this.vertex.setProperty("deletedOn", TinkerFormatter.format(new Timestamp(1000L*60*60*24*365*1000)));
	}

}
