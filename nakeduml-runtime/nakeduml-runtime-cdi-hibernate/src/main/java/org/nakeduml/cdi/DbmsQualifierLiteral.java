package org.nakeduml.cdi;

import javax.enterprise.util.AnnotationLiteral;

import org.nakeduml.runtime.persistence.DatabaseManagementSystem;

@SuppressWarnings(value="all")
public class DbmsQualifierLiteral extends AnnotationLiteral<DbmsQualifier> implements DbmsQualifier{
	private static final long serialVersionUID = 1L;
	DatabaseManagementSystem dbms;
	public DbmsQualifierLiteral(DatabaseManagementSystem dbms){
		this.dbms = dbms;
	}
	@Override
	public DatabaseManagementSystem value(){
		return dbms;
	}
}
