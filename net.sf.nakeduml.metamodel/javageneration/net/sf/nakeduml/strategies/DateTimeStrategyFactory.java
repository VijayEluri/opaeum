package net.sf.nakeduml.strategies;

import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.sf.nakeduml.javageneration.TestValueStrategy;
import net.sf.nakeduml.javageneration.composition.ConfigurableDataStrategy;
import net.sf.nakeduml.javageneration.persistence.JpaStrategy;
import net.sf.nakeduml.javametamodel.OJBlock;
import net.sf.nakeduml.javametamodel.OJField;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedClass;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedField;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotationValue;
import net.sf.nakeduml.javametamodel.annotation.OJEnumValue;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.workspace.AbstractStrategyFactory;

public class DateTimeStrategyFactory extends AbstractStrategyFactory {
	public static class MyJpaStrategy implements JpaStrategy {

		@Override
		public void annotate(OJAnnotatedField f, INakedProperty p) {
			OJAnnotationValue temporal = new OJAnnotationValue(new OJPathName(Temporal.class.getName()));
			temporal.addEnumValue(new OJEnumValue(new OJPathName(TemporalType.class.getName()), "TIMESTAMP"));
			f.putAnnotation(temporal);
		}

	}

	public static class MyConfigurableDataStrategy implements ConfigurableDataStrategy {

		@Override
		public String getDefaultStringValue(OJAnnotatedClass owner, OJBlock block, INakedProperty p) {
			return "2002-10-10";
		}

		@Override
		public String parseConfiguredValue(OJAnnotatedClass owner, OJBlock block, INakedProperty p, String configuredValue) {
			addSimpleDateFormat(owner,block);
			return "dateTimeFormat.parse(" + configuredValue + ")";
		}

	}
	public static class MyTestValueStrategy implements TestValueStrategy{

		@Override
		public String getDefaultValue(OJAnnotatedClass owner, OJBlock block, INakedProperty p) {
			addSimpleDateFormat(owner,block);
			return "dateTimeFormat.parse(\"2010-01-31\")";
		}
		
	}

	public DateTimeStrategyFactory() {
		super(MyJpaStrategy.class,MyConfigurableDataStrategy.class,MyTestValueStrategy.class);
	}
	private static void addSimpleDateFormat(OJAnnotatedClass owner, OJBlock block) {
		owner.addToImports("java.text.SimpleDateFormat");

		List<OJField> locals = block.getLocals();
		boolean hasField = false;
		for (OJField f : locals) {
			if (f.getName().equals("dateTimeFormat")) {
				hasField = true;
				break;
			}
		}
		if (!hasField) {
			OJAnnotatedField dateTimeFormat = new OJAnnotatedField("dateTimeFormat", new OJPathName("java.text.SimpleDateFormat"));
			dateTimeFormat.setInitExp("new SimpleDateFormat(\"yyyy-MM-dd\")");
			block.addToLocals(dateTimeFormat);
		}
	}

}
