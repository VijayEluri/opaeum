
package nl.klasse.octopus.codegen.umlToJava.expgenerators.creators;

import java.util.HashMap;
import java.util.Iterator;

import nl.klasse.octopus.codegen.helpers.CommonNames;
import nl.klasse.octopus.codegen.umlToJava.maps.ClassifierMap;
import nl.klasse.octopus.codegen.umlToJava.maps.TupleTypeMap;
import nl.klasse.octopus.codegen.umlToJava.modelgenerators.creators.DataTypeCreator;
import nl.klasse.tools.common.StringHelpers;

import org.eclipse.ocl.uml.TupleType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJSimpleStatement;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.utilities.JavaPathNames;
import org.opaeum.javageneration.util.OJUtil;
import org.opaeum.name.NameConverter;


public class TupleTypeCreator extends DataTypeCreator{
	private String standardClassComment = "TupleType implementation generated by Octopus";
	private HashMap<String,OJClass> tupleTypes = null;
	public TupleTypeCreator(OJUtil ojUtil){
		super(ojUtil);
		tupleTypes = new HashMap<String,OJClass>();
	}
	public OJClass make(TupleType in,OJPackage tuplePack){
		String key = "";
		// build key from the types of the parts, in alphabetical order
		TupleTypeMap TUPLE = ojUtil.buildTupleTypeMap((TupleType) in);
		String[] typeNames = TUPLE.get_typenames();
		for(int i = 0;i < typeNames.length;i++){
			key = key + "#" + typeNames[i];
		}
		OJClass result = (OJClass) tupleTypes.get(key);
		if(result != null){ // found the type; it already exists.
			return result;
		}
		result = priv_make(in, tuplePack);
		tupleTypes.put(key, result);
		return result;
	}
	private OJClass priv_make(TupleType in,OJPackage tuplePack){
		OJClass created = new OJClass();
		created.setComment(CommonNames.standardClassComment);
		tuplePack.addToClasses(created);
		TupleTypeMap TUPLE = ojUtil.buildTupleTypeMap((TupleType) in);
		String TYPE = TUPLE.getClassName();
		created.setName(TYPE);
		created.setVisibility(ojUtil.buildClassifierMap(in).javaVisibility());
		created.setComment(standardClassComment);
		this.createDataType(created, in);
		OJConstructor constr = created.getDefaultConstructor();
		//
		Iterator<?> it = TUPLE.sort_parts().iterator();
		while(it.hasNext()){
			Property decl = (Property) it.next();
			ClassifierMap mapper = ojUtil.buildClassifierMap((Classifier) decl.getType());
			created.addToFields(make_attribute(decl, mapper));
			constr.getBody().addToStatements("this." + getFieldName(decl) + " = " + getFieldName(decl));
			constr.addParam(getFieldName(decl), mapper.javaTypePath());
			created.addToImports(mapper.javaTypePath());
			created.addToOperations(createGetter(decl, mapper));
			created.addToImports(mapper.javaTypePath());
		}
		return created;
	}
	private OJOperation createGetter(Property decl,ClassifierMap mapper){
		OJOperation oper = new OJOperation();
		oper.setName(getGetterName(decl));
		oper.setReturnType(mapper.javaTypePath());
		oper.setVisibility(OJVisibilityKind.PUBLIC);
		oper.getBody().addToStatements("return " + getFieldName(decl));
		return oper;
	}
	private OJField make_attribute(Property decl,ClassifierMap mapper){
		OJField field = new OJField();
		field.setName(getFieldName(decl));
		field.setVisibility(OJVisibilityKind.PRIVATE);
		field.setType(mapper.javaTypePath());
		return field;
	}
	private String getFieldName(Property decl){
		return NameConverter.decapitalize(decl.getName());
	}
	private String getGetterName(Property decl){
		return "get" + NameConverter.capitalize(decl.getName());
	}
	/**
	 * Creates the operation 'equals' for <arg>in<\arg>. See ITEM 7 in Effective Java. The operation compares all non-derived attributes.
	 * 
	 * @param in
	 * @return
	 */
	// we need to overwrite the equal_op in DataTypeGenerator
	protected OJOperation equal_op(DataType in){
		String param = "tuple";
		TupleTypeMap TUPLE = ojUtil.buildTupleTypeMap((TupleType) in);
		String TYPE = TUPLE.getClassName();
		OJOperation oper = null;
		oper = new OJOperation();
		currentClass.addToOperations(oper);
		oper.setReturnType(JavaPathNames.Bool);
		oper.setName("equals");
		OJParameter param1 = new OJParameter();
		oper.addToParameters(param1);
		param1.setType(JavaPathNames.Object);
		param1.setName(param);
		/* <param/> */
		
		OJBlock body1 = new OJBlock();
		oper.setBody(body1);
		OJIfStatement if1 = new OJIfStatement();
		if1.setCondition("!(" + param + " instanceof " + TYPE + ")");
		body1.addToStatements(if1);
		OJBlock then1 = new OJBlock();
		if1.setThenPart(then1);
		
		OJSimpleStatement exp1 = new OJSimpleStatement("return false");
		then1.addToStatements(exp1);
		
		/* <then/> */
		/* <if/> */
		
		OJSimpleStatement exp2 = new OJSimpleStatement(TYPE + " par = (" + TYPE + ") " + param);
		body1.addToStatements(exp2);
		
		
		
		// compare fields
		param = "((" + TYPE + ")" + param + ")";
		Iterator<?> it = ((TupleType) in).oclProperties().iterator();
		while(it.hasNext()){
			Property elem = (Property) it.next();
			Classifier type = (Classifier) elem.getType();
			String name = getGetterName(elem) + "()";
			oper.getBody().addToStatements(compareInnerElements(param, type, name));
		}
		oper.getBody().addToStatements("return true");
		return oper;
	}
}
