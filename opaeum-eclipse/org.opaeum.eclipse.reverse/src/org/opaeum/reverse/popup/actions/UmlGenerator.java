package org.opaeum.reverse.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.opaeum.linkage.MappedTypeLoader;

public class UmlGenerator{
	private ClassifierFactory factory;
	public void generateUml(Collection<ITypeBinding> selection,Package library) throws Exception{
		IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(library.eResource().getURI().toPlatformString(true)));
		String absolutePath=ifile.getLocation().toFile().getAbsolutePath();
		File mappedTypesFile = new File(absolutePath.substring(0, absolutePath.length() - 3) + MappedTypeLoader.MAPPINGS_EXTENSION);
		factory = new ClassifierFactory(library);
		if(mappedTypesFile.exists()){
			factory.getMappedTypes().load(new FileInputStream(mappedTypesFile));
		}
		for(ITypeBinding t:selection){
			if(library instanceof Profile){
				if(t.isAnnotation() || t.isEnum()){
					Classifier cls = factory.getClassifierFor(t);
					factory.getMappedTypes().put(cls.getQualifiedName(), t.getQualifiedName());
					populateOperations(library, cls, t);
				}
			}else{
				// Only do annotations in profiles
				Classifier cls = factory.getClassifierFor(t);
				factory.getMappedTypes().put(cls.getQualifiedName(), t.getQualifiedName());
				populateAttributes(library, cls, t);
				populateOperations(library, cls, t);
			}
		}
		library.eResource().save(null);
		factory.getMappedTypes().store(new FileOutputStream(mappedTypesFile), "Generated by NakedUml");
	}
	public Package getRootPackage(Resource resource){
		EList<EObject> contents = resource.getContents();
		for(EObject eObject:contents){
			if(eObject instanceof Package){
				return (Package) eObject;
			}
		}
		return null;
	}
	private void populateOperations(Package modelOrProfile,Classifier owner,ITypeBinding beanInfo){
		IMethodBinding[] mds = beanInfo.getDeclaredMethods();
		for(IMethodBinding md:mds){
			if(md.isAnnotationMember()){
				ITypeBinding propertyType = md.getReturnType();
				if(propertyType.isArray()){
					propertyType = propertyType.getComponentType();
				}
				Classifier classifierFor = factory.getClassifierFor(propertyType);
				Property p = ((Stereotype) owner).getOwnedAttribute(md.getName(), classifierFor, true, UMLPackage.eINSTANCE.getProperty(), true);
				if(md.getReturnType().isArray()){
					LiteralUnlimitedNatural lit = (LiteralUnlimitedNatural) p.createUpperValue("upper", null, UMLPackage.eINSTANCE.getLiteralUnlimitedNatural());
					lit.setValue(-1);
				}
			}else if(Modifier.isPublic(md.getModifiers()) && !md.isConstructor() && !hasOperation(modelOrProfile, owner, md) && !isAccessor(md)){
				Operation oper = createOperation(owner, md);
				oper.setIsStatic(Modifier.isStatic(md.getModifiers()));
				ITypeBinding returnType = md.getReturnType();
				if(!returnType.getName().equals("void")){
					Parameter result = oper.getOwnedParameter("result", factory.getClassifierFor(returnType), false, true);
					result.setDirection(ParameterDirectionKind.RETURN_LITERAL);
					if(returnType.isArray() || factory.isCollection(returnType)){
						result.setUpper(LiteralUnlimitedNatural.UNLIMITED);
					}
				}
				ITypeBinding[] parameterTypes = md.getParameterTypes();
				for(int i = 0;i < parameterTypes.length;i++){
					if(parameterTypes[0].isArray() || factory.isCollection(parameterTypes[0])){
						Parameter umlParameter = oper.getOwnedParameters().get(i);
						umlParameter.setUpper(LiteralUnlimitedNatural.UNLIMITED);
						umlParameter.setName("parm" + i);
					}
				}
			}
		}
	}
	private boolean isAccessor(IMethodBinding md){
		// TODO Auto-generated method stub
		boolean returnsVoid = md.getReturnType() == null || md.getReturnType().getName().equals("void");
		boolean setter = md.getName().startsWith("set") && md.getParameterTypes().length == 1 && returnsVoid;
		boolean getter = md.getName().startsWith("get") && !returnsVoid;
		return setter || getter;
	}
	private boolean hasOperation(Package modelOrProfile,Classifier intfce,IMethodBinding md){
		for(Operation oper:intfce.getOperations()){
			List<Parameter> args = new ArrayList<Parameter>();
			for(Parameter parameter:oper.getOwnedParameters()){
				if(parameter.getDirection() == ParameterDirectionKind.IN_LITERAL || parameter.getDirection() == ParameterDirectionKind.INOUT_LITERAL){
					args.add(parameter);
				}
			}
			if(oper.getName().equals(md.getName()) && md.getParameterTypes().length == args.size()){
				boolean paramsMatch = true;
				for(int i = 0;i < md.getParameterTypes().length;i++){
					Parameter parm = args.get(i);
					if(factory.getClassifierFor(md.getParameterTypes()[i]) != parm.getType()){
						paramsMatch = false;
						break;
					}
				}
				if(paramsMatch){
					return true;
				}
			}
		}
		return false;
	}
	private void populateAttributes(Package modelOrProfile,Classifier classifier,ITypeBinding binding){
		Collection<PropertyDescriptor> fields = PropertyDescriptor.getPropertyDescriptors(binding);
		for(PropertyDescriptor pd:fields){
			ITypeBinding propertyType = pd.getType();
			Property found = findProperty(pd, classifier.getAttributes());
			if(found == null){
				Property attr = createAttribute(classifier, pd);
				attr.setIsReadOnly(pd.isReadOnly);
				attr.setIsDerived(pd.isReadOnly);
				if(propertyType.isArray()){
					attr.setUpper(-1);
				}
			}
		}
	}
	private Property findProperty(PropertyDescriptor pd,EList<Property> attributes){
		Property found = null;
		for(Property property:attributes){
			if(property.getName().equalsIgnoreCase(pd.getName())){
				found = property;
				break;
			}
		}
		return found;
	}
	private Operation createOperation(Classifier cls,IMethodBinding md){
		Operation oper = null;
		if(cls instanceof Interface){
			oper = ((Interface) cls).getOwnedOperation(md.getName(), getArgumentNames(md), getArgumentTypes(md), false, true);
		}else if(cls instanceof DataType){
			oper = ((DataType) cls).getOwnedOperation(md.getName(), getArgumentNames(md), getArgumentTypes(md), false, true);
		}else if(cls instanceof org.eclipse.uml2.uml.Class){
			oper = ((org.eclipse.uml2.uml.Class) cls).getOwnedOperation(md.getName(), getArgumentNames(md), getArgumentTypes(md), false, true);
		}
		return oper;
	}
	private Property createAttribute(Classifier cls,PropertyDescriptor pd){
		Property attr = null;
		if(cls instanceof Interface){
			attr = ((Interface) cls).getOwnedAttribute(pd.getName(), factory.getClassifierFor(pd.getType()), false, UMLPackage.eINSTANCE.getProperty(), true);
		}else if(cls instanceof org.eclipse.uml2.uml.Class){
			attr = ((org.eclipse.uml2.uml.Class) cls).getOwnedAttribute(pd.getName(), factory.getClassifierFor(pd.getType()), false, UMLPackage.eINSTANCE.getProperty(),
					true);
		}else if(cls instanceof DataType){
			attr = ((DataType) cls).getOwnedAttribute(pd.getName(), factory.getClassifierFor(pd.getType()), false, UMLPackage.eINSTANCE.getProperty(), true);
		}
		return attr;
	}
	private EList<String> getArgumentNames(IMethodBinding md){
		BasicEList<String> results = new BasicEList<String>();
		for(int i = 0;i < md.getParameterTypes().length;i++){
			results.add("parm" + i);
		}
		return results;
	}
	private EList<Type> getArgumentTypes(IMethodBinding md){
		BasicEList<Type> results = new BasicEList<Type>();
		ITypeBinding[] pds = md.getParameterTypes();
		for(ITypeBinding pd:pds){
			results.add(factory.getClassifierFor(pd));
		}
		return results;
	}
}
