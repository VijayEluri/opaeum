package net.sf.nakeduml.javageneration.accesscontrol;

import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitAfter;
import net.sf.nakeduml.javageneration.AbstractJavaProducingVisitor;
import net.sf.nakeduml.javageneration.JavaTransformationPhase;
import net.sf.nakeduml.javageneration.composition.CompositionNodeImplementor;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javageneration.util.ReflectionUtil;
import net.sf.nakeduml.metamodel.core.INakedEntity;

import org.nakeduml.java.metamodel.OJBlock;
import org.nakeduml.java.metamodel.OJClass;
import org.nakeduml.java.metamodel.OJForStatement;
import org.nakeduml.java.metamodel.OJIfStatement;
import org.nakeduml.java.metamodel.OJOperation;
import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedClass;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedOperation;
import org.nakeduml.runtime.domain.CompositionNode;

@StepDependency(phase = JavaTransformationPhase.class,requires = {
	CompositionNodeImplementor.class
},after = {
	CompositionNodeImplementor.class
})
public class SecureObjectImplementor extends AbstractJavaProducingVisitor{
	private static final OJPathName BUSINESS_ROLE = new OJPathName("org.nakeduml.bpm.BusinessRole");
	private static final OJPathName NUML_USER = new OJPathName("org.nakeduml.bpm.NakedUmlUser");
	public static OJPathName SECURE_OBJECT = new OJPathName("org.nakeduml.bpm.ISecureObject");
	@VisitAfter(matchSubclasses = true)
	public void visitClass(INakedEntity entity){
		OJAnnotatedClass ojClass = findJavaClass(entity);
		ojClass.addToImplementedInterfaces(SECURE_OBJECT);
		ojClass.addToImports(BUSINESS_ROLE);
		addIsOwnedByUser(ojClass, entity);
		addCanBeOwnedByUser(ojClass, entity);
		addIsGroupOwnershipValid(ojClass, entity);
		addIsUserOwnershipValid(ojClass);
	}
	// TODO Come up with a new solution to overriding user ownership
	private void addIsUserOwnershipValid(OJAnnotatedClass ojClass){
		OJAnnotatedOperation isUserOwnershipValid = new OJAnnotatedOperation("isUserOwnershipValid");
		isUserOwnershipValid.addParam("user", NUML_USER);
		isUserOwnershipValid.setBody(new OJBlock());
		ojClass.addToOperations(isUserOwnershipValid);
		OJIfStatement ifCan = new OJIfStatement("canBeOwnedByUser(user)", "return isOwnedByUser(user)");
		ifCan.addToElsePart("return false");
		isUserOwnershipValid.setReturnType(new OJPathName("boolean"));
		isUserOwnershipValid.getBody().addToStatements(ifCan);
		isUserOwnershipValid.setComment("User Ownership is bypassed if the current user does not share the role required for ownership");
	}
	private void addCanBeOwnedByUser(OJClass owner,INakedEntity entity){
		OJOperation canBeOwnedByUser = OJUtil.findOperation(owner, "canBeOwnedByUser");
		if(canBeOwnedByUser == null || canBeOwnedByUser.getParameters().size() > 1){
			canBeOwnedByUser = new OJAnnotatedOperation("canBeOwnedByUser");
			canBeOwnedByUser.addParam("user", NUML_USER);
			canBeOwnedByUser.setBody(new OJBlock());
			canBeOwnedByUser.setReturnType(new OJPathName("boolean"));
			OJForStatement forRoles = new OJForStatement("", "", "role", "user.getRoles()");
			forRoles.setBody(new OJBlock());
			forRoles.setElemType(BUSINESS_ROLE);
			OJIfStatement ifEquals = new OJIfStatement("role instanceof " + entity.getMappingInfo().getJavaName(), "return true");
			forRoles.getBody().addToStatements(ifEquals);
			canBeOwnedByUser.getBody().addToStatements(forRoles);
			OJIfStatement ifIsSecureObject = new OJIfStatement("getOwningObject() instanceof " + SECURE_OBJECT.getLast() + "&&((" + SECURE_OBJECT.getLast()
					+ ")getOwningObject()).canBeOwnedByUser(user)", "return true");
			ifEquals.addToElsePart(ifIsSecureObject);
			canBeOwnedByUser.getBody().addToStatements("return false");
			owner.addToOperations(canBeOwnedByUser);
		}
		canBeOwnedByUser.setReturnType(new OJPathName("boolean"));
	}
	private void addIsOwnedByUser(OJClass owner,INakedEntity entity){
		OJOperation isOwnedByUser = OJUtil.findOperation(owner, "isOwnedByUser");
		if(isOwnedByUser == null || isOwnedByUser.getParameters().size() > 1){
			isOwnedByUser = new OJAnnotatedOperation("isOwnedByUser");
			isOwnedByUser.addParam("user", NUML_USER);
			isOwnedByUser.setBody(new OJBlock());
			isOwnedByUser.setReturnType(new OJPathName("boolean"));
			OJForStatement forRoles = new OJForStatement("", "", "role", "user.getRoles()");
			forRoles.setBody(new OJBlock());
			forRoles.setElemType(BUSINESS_ROLE);
			OJIfStatement ifEquals = new OJIfStatement("this.equals(role)", "return true");
			forRoles.getBody().addToStatements(ifEquals);
			isOwnedByUser.getBody().addToStatements(forRoles);
			OJIfStatement ifIsSecureObject = new OJIfStatement("getOwningObject() instanceof " + SECURE_OBJECT.getLast() + "&&((" + SECURE_OBJECT.getLast()
					+ ")getOwningObject()).isOwnedByUser(user)", "return true");
			ifEquals.addToElsePart(ifIsSecureObject);
			isOwnedByUser.getBody().addToStatements("return false");
			owner.addToOperations(isOwnedByUser);
		}
		isOwnedByUser.setReturnType(new OJPathName("boolean"));
	}
	private void addIsGroupOwnershipValid(OJClass owner,INakedEntity entity){
		OJOperation isGroupOwnershipValid = OJUtil.findOperation(owner, "isGroupOwnershipValid");
		if(isGroupOwnershipValid == null || isGroupOwnershipValid.getParameters().size() > 0){
			isGroupOwnershipValid = new OJAnnotatedOperation("isGroupOwnershipValid");
			isGroupOwnershipValid.addParam("user", NUML_USER);
			OJForStatement forRoles = new OJForStatement("", "", "role", "user.getRoles()");
			forRoles.setBody(new OJBlock());
			forRoles.setElemType(BUSINESS_ROLE);
			OJForStatement forGroups = new OJForStatement("", "", "group", "role.getGroupsForSecurity()");
			forGroups.setBody(new OJBlock());
			forGroups.setElemType(ReflectionUtil.getUtilInterface(CompositionNode.class));
			forRoles.getBody().addToStatements(forGroups);
			OJIfStatement ifEquals = new OJIfStatement("group.equals(this)", "return true");
			forGroups.getBody().addToStatements(ifEquals);
			OJIfStatement ifIsSecureObject = new OJIfStatement("getOwningObject() instanceof " + SECURE_OBJECT.getLast() + "&&((" + SECURE_OBJECT.getLast()
					+ ")getOwningObject()).isGroupOwnershipValid(user)", "return true");
			ifEquals.addToElsePart(ifIsSecureObject);
			isGroupOwnershipValid.getBody().addToStatements(forRoles);
			isGroupOwnershipValid.getBody().addToStatements("return false");
			owner.addToOperations(isGroupOwnershipValid);
		}
		isGroupOwnershipValid.setReturnType(new OJPathName("boolean"));
	}
}
