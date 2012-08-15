package org.opaeum.javageneration.basicjava;

import nl.klasse.octopus.codegen.umlToJava.maps.ClassifierMap;
import nl.klasse.octopus.codegen.umlToJava.maps.PropertyMap;

import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.opaeum.feature.StepDependency;
import org.opaeum.feature.visit.VisitBefore;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.opaeum.javageneration.JavaTransformationPhase;
import org.opaeum.javageneration.util.OJUtil;

@StepDependency(phase = JavaTransformationPhase.class,requires = {Java6ModelGenerator.class},after = {Java6ModelGenerator.class})
public class SuperTypeGenerator extends AbstractStructureVisitor{
	@VisitBefore(matchSubclasses = true)
	protected void visitComplexStructure(Classifier c){
		if(c.getGeneralizations().size() == 1){
			OJAnnotatedClass myClass = findJavaClass(c);
			if(myClass != null){
				for(Generalization g:c.getGeneralizations()){
					ClassifierMap map = ojUtil.buildClassifierMap(g.getGeneral(),(CollectionKind)null);
					myClass.setSuperclass(map.javaTypePath());
					myClass.addToImports(map.javaTypePath());
					OJConstructor constructor = myClass.getDefaultConstructor();
					constructor.getBody().addToStatements("super()");
				}
			}
		}else if(c.getGeneralizations().size() > 1){
		}
		if(ojUtil.hasOJClass(c) && c instanceof BehavioredClassifier){
			for(InterfaceRealization ir:((BehavioredClassifier)c).getInterfaceRealizations()){
				OJAnnotatedClass myClass = findJavaClass(c);
				myClass.addToImplementedInterfaces(ojUtil.classifierPathname(ir.getContract()));
			}
		}
	}
	@VisitBefore(matchSubclasses = true)
	public void visitInterface(Interface c){
		if(c.getGeneralizations().isEmpty())
			return;
		OJAnnotatedInterface myIntf = (OJAnnotatedInterface) findJavaClass(c);
		if(myIntf != null){
			for(Generalization g:c.getGeneralizations()){
				OJPathName pathname = ojUtil.classifierPathname(g.getGeneral());
				myIntf.getSuperInterfaces().add(pathname);
				myIntf.addToImports(pathname);
			}
		}
	}
	@Override
	protected void visitProperty(Classifier owner,PropertyMap buildStructuralFeatureMap){
		
	}
}
