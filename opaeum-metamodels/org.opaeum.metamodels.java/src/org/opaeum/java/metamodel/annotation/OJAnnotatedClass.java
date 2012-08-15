package org.opaeum.java.metamodel.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.opaeum.java.metamodel.OJBlock;
import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.utilities.JavaStringHelpers;
import org.opaeum.java.metamodel.utilities.JavaUtil;
import org.opaeum.java.metamodel.utilities.OJOperationComparator;

public class OJAnnotatedClass extends OJClass implements OJAnnotatedElement{
	Map<OJPathName,OJAnnotationValue> f_annotations = new TreeMap<OJPathName,OJAnnotationValue>();
	private List<String> genericTypeParams = new ArrayList<String>();
	public OJAnnotatedClass(String string){
		setName(string);
	}
	public OJAnnotationValue findAnnotation(OJPathName path){
		return AnnotationHelper.getAnnotation(this, path);
	}
	public boolean addAnnotationIfNew(OJAnnotationValue value){
		if(f_annotations.containsKey(value.getType())){
			return false;
		}else{
			putAnnotation(value);
			return true;
		}
	}
	public Collection<OJAnnotationValue> getAnnotations(){
		return f_annotations.values();
	}
	@Override
	public void release(){
		super.release();
		f_annotations.clear();
		genericTypeParams.clear();
	}
	public OJAnnotationValue putAnnotation(OJAnnotationValue value){
		return f_annotations.put(value.getType(), value);
	}
	public OJAnnotationValue removeAnnotation(OJPathName type){
		return f_annotations.remove(type);
	}
	public OJConstructor findConstructor(OJPathName ... parameter1){
		List<OJPathName> pathNames = Arrays.asList(parameter1);
		return findConstructor(pathNames);
	}
	public OJConstructor findConstructor(OJPathName parameter1){
		List<OJPathName> pathnames = Collections.singletonList(parameter1);
		return findConstructor(pathnames);
	}
	// public?
	public OJConstructor findConstructor(List<OJPathName> pathnames){
		for(OJConstructor con:getConstructors()){
			if(isMatch(pathnames, con.getParamTypes())){
				return con;
			}
		}
		return null;
	}
	private static boolean isMatch(List<OJPathName> pathnames,List<OJPathName> paramTypes){
		if(paramTypes.size() == pathnames.size()){
			for(int i = 0;i < paramTypes.size();i++){
				if(!paramTypes.get(i).equals(pathnames.get(i))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	@Override
	public void calcImports(){
		super.calcImports();
		addToImports(AnnotationHelper.getImportsFrom(getFields()));
		addToImports(AnnotationHelper.getImportsFrom(getOperations()));
		addToImports(AnnotationHelper.getImportsFrom(this));
	}
	@Override
	public String toJavaString(){
		calcImports();
		StringBuilder classInfo = new StringBuilder();
		classInfo.append(getMyPackage().toJavaString());
		classInfo.append("\n");
		classInfo.append(imports());
		classInfo.append("\n");
		addJavaDocComment(classInfo);
		if(getAnnotations().size() > 0){
			classInfo.append(JavaStringHelpers.indent(JavaUtil.collectionToJavaString(getAnnotations(), "\n"), 0));
			classInfo.append("\n");
		}
		if(isAbstract()){
			classInfo.append("abstract ");
		}
		classInfo.append(visToJava(this) + " ");
		classInfo.append("class ");
		classInfo.append(getName());
		Iterator<String> it = this.genericTypeParams.iterator();
		if(it.hasNext()){
			classInfo.append("<");
		}
		while(it.hasNext()){
			classInfo.append(it.next());
			if(it.hasNext()){
				classInfo.append(",");
			}else{
				classInfo.append(">");
			}
		}
		if(getSuperclass() != null){
			classInfo.append(" extends " + getSuperclass().getTypeNameWithTypeArguments());
		}
		classInfo.append(implementedInterfaces());
		classInfo.append(" {\n");
		classInfo.append(JavaStringHelpers.indent(fields(), 1));
		classInfo.append("\n\n");
		classInfo.append(JavaStringHelpers.indent(constructors(), 1));
		classInfo.append("\n");
		classInfo.append(JavaStringHelpers.indent(operations(), 1));
		classInfo.append("\n}");
		return classInfo.toString();
	}
	public StringBuilder operations(){
		List<OJOperation> temp = new ArrayList<OJOperation>(this.getOperations());
		Collections.sort(temp, new OJOperationComparator());
		StringBuilder result = new StringBuilder();
		result.append(JavaUtil.collectionToJavaString(temp, "\n"));
		return result;
	}
	protected StringBuilder constructors(){
		StringBuilder result = new StringBuilder();
		TreeSet<OJConstructor> treeSet = new TreeSet<OJConstructor>(new OJOperationComparator());
		treeSet.addAll(this.getConstructors());
		result.append(JavaUtil.collectionToJavaString(treeSet, "\n"));
		return result;
	}
	public StringBuilder fields(){
		StringBuilder result = new StringBuilder();
		result.append(JavaUtil.collectionToJavaString(this.getFields(), "\n"));
		return result;
	}
	protected StringBuilder implementedInterfaces(){
		StringBuilder result = new StringBuilder();
		if(!this.getImplementedInterfaces().isEmpty())
			result.append(" implements ");
		Iterator<OJPathName> it = getImplementedInterfaces().iterator();
		while(it.hasNext()){
			OJPathName elem = it.next();
			result.append(elem.getLast());
			if(it.hasNext())
				result.append(", ");
		}
		return result;
	}
	public void addGenericTypeParam(String string){
		this.genericTypeParams.add(string);
	}
	@Override
	protected void addJavaDocComment(StringBuilder result){
		if(getComment() != null && !getComment().equals("")){
			String comment = JavaStringHelpers.firstCharToUpper(getComment());
			result.append("/** " + comment);
			result.append("\n */\n");
		}
	}
	public boolean hasAnnotation(OJPathName pathName){
		return this.f_annotations.containsKey(pathName);
	}
	public OJAnnotatedClass getDeepCopy(OJPackage owner){
		OJAnnotatedClass copy = new OJAnnotatedClass(getName());
		copy.setMyPackage(owner);
		copyDeepInfoInto(copy);
		return copy;
	}
	protected void copyDeepInfoInto(OJAnnotatedClass copy){
		super.copyDeepInfoInto(copy);
		Set<OJPathName> interfaces = getImplementedInterfaces();
		for(OJPathName ojInterface:interfaces){
			OJPathName copyInterface = ojInterface.getCopy();
			copy.addToImplementedInterfaces(copyInterface);
		}
		Collection<OJAnnotationValue> annotations = getAnnotations();
		for(OJAnnotationValue ojAnnotationValue:annotations){
			OJAnnotationValue copyAnnotation = ojAnnotationValue.getDeepCopy();
			copy.addAnnotationIfNew(copyAnnotation);
		}
	}
	public void renameAll(Set<OJPathName> renamePathNames,String suffix){
		super.renameAll(renamePathNames, suffix);
		Collection<OJAnnotationValue> annotations = getAnnotations();
		for(OJAnnotationValue ojAnnotationValue:annotations){
			ojAnnotationValue.renameAll(renamePathNames, suffix);
		}
	}
	public String toString(){
		return getPathName().toString();
	}
	public void removeFromOperations(String internalRemover,List<OJPathName> singletonList){
		Set<OJOperation> methodSet = f_operations.get(internalRemover);
		if(methodSet != null){
			Iterator<OJOperation> iterator = methodSet.iterator();
			while(iterator.hasNext()){
				OJOperation ojOperation = (OJOperation) iterator.next();
				if(ojOperation.getName().equals(internalRemover) && ojOperation.paramsEquals(singletonList)){
					iterator.remove();
					break;
				}
			}
		}
	}
	public void removeFromFields(String name){
		super.f_fields.remove(name);
	}
	public String toAbstractSuperclassJavaString(){
		String concreteName = getName();
		String oldName = name;
		setName(oldName + "Generated");
		String result = toJavaString();
		result = result.replaceAll("[\\(]this[\\)]", "((" + concreteName + ")this)");
		setName(oldName);
		return result;
	}
	public static void main(String[] args){
		System.out.println("this,this;this this)this.".replaceAll("\\bthis\\b", "bla"));
	}
	public String toConcreteImplementationJavaString(){
		this.calcImports();
		StringBuilder classInfo = new StringBuilder();
		classInfo.append(this.getMyPackage().toJavaString());
		classInfo.append("\n");
		classInfo.append(this.imports());
		classInfo.append("\n");
		this.addJavaDocComment(classInfo);
		if(this.getAnnotations().size() > 0){
			classInfo.append(JavaStringHelpers.indent(JavaUtil.collectionToJavaString(this.getAnnotations(), "\n"), 0));
			classInfo.append("\n");
		}
		if(this.isAbstract()){
			classInfo.append("abstract ");
		}
		classInfo.append(this.visToJava(this) + " ");
		classInfo.append("class ");
		classInfo.append(this.getName());
		if(suffix == null){
			classInfo.append(" extends " + this.getName() + "Generated");
		}else{
			classInfo.append(" extends " + name + "Generated" + suffix);
		}
		classInfo.append(" {\n");
		final Set<OJConstructor> constructors = getConstructors();
		Collection<OJConstructor> tempConstructors = new ArrayList<OJConstructor>();
		for(OJConstructor ojConstructor:constructors){
			OJConstructor e = ojConstructor.getDeepCopy();
			e.z_internalAddToOwningClass(this);
			e.setBody(new OJBlock());
			final Iterator<OJParameter> iterator = e.getParameters().iterator();
			StringBuilder sb = new StringBuilder("super(");
			while(iterator.hasNext()){
				OJParameter ojParameter = (OJParameter) iterator.next();
				sb.append(ojParameter.getName());
				if(iterator.hasNext()){
					sb.append(",");
				}
			}
			sb.append(")");
			tempConstructors.add(e);
		}
		classInfo.append(JavaStringHelpers.indent(JavaUtil.collectionToJavaString(tempConstructors, "\n"), 1));
		classInfo.append("\n}");
		return classInfo.toString();
	}
}