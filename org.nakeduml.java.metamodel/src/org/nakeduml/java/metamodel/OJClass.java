package org.nakeduml.java.metamodel;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.nakeduml.java.metamodel.generated.OJClassGEN;
import org.nakeduml.java.metamodel.utilities.JavaStringHelpers;
import org.nakeduml.java.metamodel.utilities.JavaUtil;




public class OJClass extends OJClassGEN {
	/******************************************************
	 * The constructor for this classifier.
	*******************************************************/	
	public OJClass() {
		super();
		this.setVisibility(OJVisibilityKind.PUBLIC);
	}

	/** Constructor for OJClass
	 * 
	 * @param name 
	 * @param comment 
	 * @param isStatic 
	 * @param isFinal 
	 * @param isVolatile 
	 * @param uniqueNumber 
	 * @param isAbstract 
	 * @param isDerived 
	 */
	public OJClass(String name, String comment, boolean isStatic, boolean isFinal, boolean isVolatile, int uniqueNumber, boolean isAbstract, boolean isDerived) {
//		super(name, comment, isStatic, isFinal, isVolatile, uniqueNumber, isAbstract, isDerived);
	}

	public OJClass getDeepCopy(OJPackage owner) {
		OJClass copy = new OJClass();
		copy.setMyPackage(owner);
		copyDeepInfoInto(copy);
		return copy;
	}
	
	protected void copyDeepInfoInto(OJClass copy) {
		super.copyDeepInfoInto(copy);
		Set<OJConstructor> constructors = getConstructors();
		for (OJConstructor ojConstructor : constructors) {
			OJConstructor copyConstructor = ojConstructor.getDeepConstructorCopy();
			copyConstructor.setReturnType(copy.getPathName());
			copy.addToConstructors(copyConstructor);
		}
		if (getSuperclass()!=null) {
			OJPathName superClassCopy = getSuperclass().getDeepCopy();
			copy.setSuperclass(superClassCopy);
		}
		List<OJField> fields = getFields();
		for (OJField ojField : fields) {
			OJField ojFieldCopy = (OJField)ojField.getDeepCopy();
			ojFieldCopy.setOwner(copy);
			copy.addToFields(ojFieldCopy);
		}
	}

	public void calcImports() {
		super.calcImports(); // does operations
		// fields
		Iterator it = getFields().iterator();
		while( it.hasNext()) {
			OJField f = (OJField) it.next();
			this.addToImports(f.getType());
		}
		// interfaces
		it = getImplementedInterfaces().iterator();
		while( it.hasNext()) {
			OJPathName intf = (OJPathName) it.next();
			this.addToImports(intf);
		}
		// constructors
		it = getConstructors().iterator();
		while( it.hasNext()) {
			OJConstructor constr = (OJConstructor) it.next();
			Iterator params = constr.getParamTypes().iterator();
			while( params.hasNext()) {
				this.addToImports((OJPathName)params.next());
			}
		}
		// supertype
		this.addToImports(this.getSuperclass());
	}

	public OJConstructor getDefaultConstructor() {
		OJConstructor result = super.getDefaultConstructor();
		if (result == null) {
			OJConstructor constructor = new OJConstructor();
			constructor.setBody(new OJBlock());
			constructor.setComment("default constructor for " + this.getName());
			this.addToConstructors(constructor);
			result = constructor;
		}
		return result;
	}
	
	public String toJavaString(){
		this.calcImports();
		StringBuilder classInfo = new StringBuilder();
		classInfo.append(getMyPackage().toJavaString());
		classInfo.append("\n");
		classInfo.append(imports());
		classInfo.append("\n");
		if (!getComment().equals("")){
			addJavaDocComment(classInfo);
		}
		if (this.getNeedsSuppress()) {
			classInfo.append("@SuppressWarnings(\"serial\")\n");
		}
		if (this.isAbstract()) {
			classInfo.append("abstract ");
		}
		classInfo.append(visToJava(this) + " ");
		classInfo.append("class " + getName());
		if ( getSuperclass() != null) {
			classInfo.append(" extends " + getSuperclass().getLast());
		}
		classInfo.append(implementedInterfaces());
		classInfo.append(" {\n");
		classInfo.append(JavaStringHelpers.indent(fields(),1));
		classInfo.append("\n\n");
		classInfo.append(JavaStringHelpers.indent(constructors(),1));
		classInfo.append("\n");
		classInfo.append(JavaStringHelpers.indent(operations(),1));
		classInfo.append("\n}");
		return classInfo.toString();
	}
	
	/**
	 * @return
	 */
	private StringBuilder constructors() {
		StringBuilder result = new StringBuilder();
		result.append(JavaUtil.collectionToJavaString(this.getConstructors(), "\n"));
		return result;
	}
	/**
	 * @return
	 */
	private StringBuilder fields() {
		StringBuilder result = new StringBuilder();
		result.append(JavaUtil.collectionToJavaString(this.getFields(), "\n"));
		return result;
	}
	/**
	 * @return
	 */
	private StringBuilder implementedInterfaces() {
		StringBuilder result = new StringBuilder();
		if (!this.getImplementedInterfaces().isEmpty()) result.append(" implements ");
		Iterator it = getImplementedInterfaces().iterator();
		while (it.hasNext()){
			OJPathName elem = (OJPathName) it.next();
			result.append(elem.getLast());
			if (it.hasNext()) result.append(", ");
		}		
		return result;
	}
	/**
	 * @param string
	 * @return
	 */
	public OJField findField(String string) {
		OJField result = null;
		Iterator it = getFields().iterator();
		while (it.hasNext()){
			OJField elem = (OJField) it.next();
			if (elem.getName().equals(string)){
				result = elem;
			}
		}		
		return result;
	}
}