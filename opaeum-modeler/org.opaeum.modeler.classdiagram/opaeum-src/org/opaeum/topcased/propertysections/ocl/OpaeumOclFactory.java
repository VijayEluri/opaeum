package org.opaeum.topcased.propertysections.ocl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.options.EvaluationOptions;
import org.eclipse.ocl.uml.UMLEnvironment;
import org.eclipse.ocl.uml.UMLEnvironmentFactory;
import org.eclipse.ocl.uml.UMLFactory;
import org.eclipse.ocl.uml.Variable;
import org.eclipse.ocl.uml.options.EvaluationMode;
import org.eclipse.ocl.uml.options.UMLEvaluationOptions;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DurationObservation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Pin;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.TimeObservation;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.opaeum.eclipse.EmfBehaviorUtil;
import org.opaeum.eclipse.EmfElementFinder;
import org.opaeum.eclipse.EmfPropertyUtil;
import org.opaeum.eclipse.LibraryImporter;
import org.opaeum.emf.extraction.StereotypesHelper;
import org.opaeum.metamodel.core.internal.StereotypeNames;
import org.topcased.modeler.uml.oclinterpreter.DelegatingPackageRegistry;
import org.topcased.modeler.uml.oclinterpreter.ModelingLevel;
import org.topcased.modeler.uml.oclinterpreter.UMLOCLFactory;

public final class OpaeumOclFactory extends UMLOCLFactory{
	private final class OpaeumEnvironmentFactory extends UMLEnvironmentFactory{
		private OpaeumEnvironmentFactory(Registry registry,ResourceSet rset){
			super(registry, rset);
		}
		@Override
		public Environment<Package,Classifier,Operation,Property,EnumerationLiteral,Parameter,State,CallOperationAction,SendSignalAction,Constraint,Class,EObject> createEnvironment(
				Environment<Package,Classifier,Operation,Property,EnumerationLiteral,Parameter,State,CallOperationAction,SendSignalAction,Constraint,Class,EObject> parent){
			if(!(parent instanceof UMLEnvironment)){
				throw new IllegalArgumentException("Parent environment must be a UML environment: " + parent); //$NON-NLS-1$
			}
			OpaeumEnvironment result = new OpaeumEnvironment(parent);
			result.setFactory(this);
			return result;
		}
		@Override
		public UMLEnvironment createEnvironment(){
			OpaeumEnvironment result = new OpaeumEnvironment(getResourceSet().getPackageRegistry(), getResourceSet());
			result.setFactory(this);
			Classifier nearestClassifier = EmfElementFinder.getNearestClassifier(context);
			Model simpleTypes = LibraryImporter.findLibrary(context.getModel(), StereotypeNames.OPIUM_SIMPLE_TYPES);
			Model bpmLib = LibraryImporter.findLibrary(context.getModel(), StereotypeNames.OPIUM_BPM_LIBRARY);
			if(simpleTypes != null){
				Type dateTime = simpleTypes.getOwnedType("DateTime");
				if(dateTime != null){
					if(nearestClassifier instanceof StateMachine){
						addTimeObservations(result, nearestClassifier, dateTime, StereotypeNames.BUSINES_STATE_MACHINE);
					}else if(nearestClassifier instanceof Activity){
						addTimeObservations(result, nearestClassifier, dateTime, StereotypeNames.BUSINES_PROCESS);
					}else{
						Element element = context;
						while(!(element instanceof Activity || element == null)){
							if(element instanceof StructuredActivityNode){
								addTimeObservations(result, element, dateTime, StereotypeNames.STRUCTURED_BUSINESS_PROCESS_NODE);
							}
							element = (Element) EmfElementFinder.getContainer(element);
						}
					}
				}
			}
			if(bpmLib != null){
				Type duration = bpmLib.getNestedPackage("businesscalendar").getOwnedType("Duration");
				if(duration != null){
					if(nearestClassifier instanceof StateMachine){
						addDurationObservations(result, nearestClassifier, duration, StereotypeNames.BUSINES_STATE_MACHINE);
					}else if(nearestClassifier instanceof Activity){
						addDurationObservations(result, nearestClassifier, duration, StereotypeNames.BUSINES_PROCESS);
					}else{
						Element element = context;
						while(!(element instanceof Activity || element == null)){
							if(element instanceof StructuredActivityNode){
								addDurationObservations(result, element, duration, StereotypeNames.STRUCTURED_BUSINESS_PROCESS_NODE);
							}
							element = (Element) EmfElementFinder.getContainer(element);
						}
					}
				}
			}
			if(nearestClassifier instanceof Behavior){
				Classifier contextObject = EmfBehaviorUtil.getContext(context);
				if(contextObject != null){
					Variable var = UMLFactory.eINSTANCE.createVariable();
					var.setType(contextObject);
					var.setName("contextObject");
					result.addElement(var.getName(), var, true);
				}
			}else if(!(nearestClassifier == null || nearestClassifier.isAbstract())){
				if(EmfPropertyUtil.getEndToComposite(nearestClassifier) == null){
					Classifier owningObject = null;
					owningObject = EmfElementFinder.getNearestClassifier(nearestClassifier.getOwner());
					if(owningObject != null){
						Variable var = UMLFactory.eINSTANCE.createVariable();
						var.setType((Type) owningObject);
						var.setName("owningObject");
						result.addElement(var.getName(), var, true);
					}
				}
			}
			List<TypedElement> tes = EmfElementFinder.getTypedElementsInScope(context);
			for(TypedElement te:tes){
				if(te instanceof org.eclipse.uml2.uml.Variable || te instanceof Parameter || te instanceof Pin){
					Variable var = UMLFactory.eINSTANCE.createVariable();
					var.setType(te.getType());
					var.setName(te.getName());
					result.addElement(var.getName(), var, true);
				}
			}
			if(bpmLib != null){
				Type br = bpmLib.getOwnedType("BusinessRole");
				if(br != null){
					Variable var = UMLFactory.eINSTANCE.createVariable();
					var.setType(br);
					var.setName("currentUser");
					result.addElement(var.getName(), var, true);
				}
			}
			if(simpleTypes != null){
				Type br = simpleTypes.getOwnedType("DateTime");
				if(br != null){
					Variable var = UMLFactory.eINSTANCE.createVariable();
					var.setType(br);
					var.setName("now");
					result.addElement(var.getName(), var, true);
				}
			}
			for(Entry<String,Classifier> entry:variables.entrySet()){
				Variable var = UMLFactory.eINSTANCE.createVariable();
				var.setType(entry.getValue());
				var.setName(entry.getKey());
				result.addElement(var.getName(), var, true);
			}
			return result;
		}
		private void addDurationObservations(OpaeumEnvironment result,Element element,Type duration,String businesStateMachine){
			Stereotype s = StereotypesHelper.getStereotype(element, businesStateMachine);
			if(s != null){
				EList<DurationObservation> obs = (EList<DurationObservation>) element.getValue(s, "durationObservations");
				for(DurationObservation timeObservation:obs){
					Variable var = UMLFactory.eINSTANCE.createVariable();
					var.setType(duration);
					var.setName(timeObservation.getName());
					result.addElement(var.getName(), var, true);
				}
			}
		}
		protected void addTimeObservations(OpaeumEnvironment result,Element element,Type br,String businesStateMachine){
			Stereotype s = StereotypesHelper.getStereotype(element, businesStateMachine);
			if(s != null){
				EList<TimeObservation> obs = (EList<TimeObservation>) element.getValue(s, "timeObservations");
				for(TimeObservation timeObservation:obs){
					Variable var = UMLFactory.eINSTANCE.createVariable();
					var.setType(br);
					var.setName(timeObservation.getName());
					result.addElement(var.getName(), var, true);
				}
			}
		}
	}
	private Element context;
	private Map<String,Classifier> variables = new HashMap<String,Classifier>();
	@Override
	public void setContext(EObject context){
		this.context = (Element) context;
		super.setContext(EmfBehaviorUtil.getSelf((Element) context));
	}
	@Override
	public Object getContextClassifier(EObject object){
		return EmfBehaviorUtil.getSelf((Element) object);
	}
	@Override
	public OCL<?,?,?,?,?,?,?,?,?,?,?,?> createOCL(ModelingLevel level){
		try{
			UMLEnvironmentFactory factory = new OpaeumEnvironmentFactory(new DelegatingPackageRegistry(getContext().eResource().getResourceSet().getPackageRegistry(),
					EPackage.Registry.INSTANCE), getContext().eResource().getResourceSet());
			OCL<?,?,?,?,?,?,?,?,?,?,?,?> result = OCL.newInstance(factory);
			switch(level){
			case M2:
				EvaluationOptions.setOption(result.getEvaluationEnvironment(), UMLEvaluationOptions.EVALUATION_MODE, EvaluationMode.RUNTIME_OBJECTS);
				break;
			default:
				EvaluationOptions.setOption(result.getEvaluationEnvironment(), UMLEvaluationOptions.EVALUATION_MODE, EvaluationMode.INSTANCE_MODEL);
				break;
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Override
	public OCL<?,?,?,?,?,?,?,?,?,?,?,?> createOCL(ModelingLevel level,Resource res){
		// TODO Auto-generated method stub
		return super.createOCL(level, res);
	}
	public void addVariable(String name,Classifier type){
		variables.put(name, type);
	}
}