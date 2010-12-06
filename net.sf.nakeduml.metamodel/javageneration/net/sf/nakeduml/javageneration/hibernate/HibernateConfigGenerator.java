package net.sf.nakeduml.javageneration.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.StepDependency;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.AbstractTextProducingVisitor;
import net.sf.nakeduml.javageneration.CharArrayTextSource;
import net.sf.nakeduml.javametamodel.OJClass;
import net.sf.nakeduml.javametamodel.OJPackage;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedClass;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import net.sf.nakeduml.textmetamodel.PropertiesSource;
import net.sf.nakeduml.textmetamodel.TextWorkspace;
import net.sf.nakeduml.visitor.AbstractOJVisitor;

@StepDependency(phase = StandaloneHibernatePhase.class,requires=PersistenceUsingHibernateStep.class)
public class HibernateConfigGenerator extends AbstractTextProducingVisitor implements StandaloneHibernateStep {
	public static final class OJVisitor extends AbstractOJVisitor {
		private final HashSet<OJClass> classes;

		private OJVisitor(HashSet<OJClass> classes) {
			this.classes = classes;
		}

		@VisitBefore(matchSubclasses = true)
		public void visitPackage(OJPackage p) {
			for (OJClass c : p.getClasses()) {
				if (c instanceof OJAnnotatedClass && ((OJAnnotatedClass) c).hasAnnotation(new OJPathName("javax.persistence.Table"))) {
					classes.add(c);
				}
			}
		}
	}

	public static final String RESOURCE_DIR = "gen-src";

	public void generate() {
		HashMap<String, Object> vars = new HashMap<String, Object>();
		Set<OJClass> classesRecursively = getClassesRecursively();
		List<OJClass> sortedClasses = new ArrayList<OJClass>(classesRecursively);
		Collections.sort(sortedClasses, new Comparator<OJClass>() {
			@Override
			public int compare(OJClass o1, OJClass o2) {
				if (o1.getName().endsWith("Audit")) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		vars.put("persistentClasses", sortedClasses);
		// TODO parameterize this!/ later

		vars.put("requiresAuditing", true);
		vars.put("config", this.config);
		processTemplate(this.workspace.getGeneratingModelsOrProfiles().get(0), "templates/Model/Jbpm4HibernateConfig.vsl",
				"hibernate.cfg.xml", PropertiesSource.GEN_RESOURCE, vars);
	}

	private Set<OJClass> getClassesRecursively() {
		final HashSet<OJClass> classes = new HashSet<OJClass>();
		new OJVisitor(classes).startVisiting(super.javaModel);
		return classes;
	}

	@Override
	public void initialize(INakedModelWorkspace workspace, NakedUmlConfig config, TextWorkspace textWorkspace, OJPackage javaModel) {
		super.initialize(workspace, config, textWorkspace);
		this.javaModel=javaModel;

	}

}
