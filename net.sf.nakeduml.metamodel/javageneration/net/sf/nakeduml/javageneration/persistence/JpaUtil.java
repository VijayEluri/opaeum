package net.sf.nakeduml.javageneration.persistence;

import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedClass;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedField;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotationAttributeValue;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotationValue;
import net.sf.nakeduml.javametamodel.annotation.OJEnumValue;
import net.sf.nakeduml.metamodel.core.INakedAssociation;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedComplexStructure;
import net.sf.nakeduml.metamodel.core.INakedEntity;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.core.INakedNameSpace;
import net.sf.nakeduml.metamodel.core.INakedPackage;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.name.NameConverter;

import org.hibernate.annotations.Filter;

public class JpaUtil {

	public static final String BACKTICK = "";

	public static OJAnnotationValue buildTableAnnotation(OJAnnotatedClass owner, String tableName, NakedUmlConfig config) {
		return buildTableAnnotation(owner, tableName, config, null);
	}
	public static INakedPackage getNearestSchema(INakedNameSpace ns){
		while(!(isSchema(ns) || ns==null)){
			ns=ns.getNameSpace();
		}
		if(isSchema(ns)){
			return (INakedPackage) ns;
		}
		return null;
	}
	private static boolean isSchema(INakedNameSpace ns) {
		return (ns instanceof INakedPackage && ((INakedPackage)ns).isSchema());
	}
	public static OJAnnotationValue buildTableAnnotation(OJAnnotatedClass owner, String tableName, NakedUmlConfig config, INakedNameSpace ns) {
		OJAnnotationValue table = new OJAnnotationValue(new OJPathName("javax.persistence.Table"));
		table.putAttribute(new OJAnnotationAttributeValue("name", BACKTICK + tableName + BACKTICK));
		if (config.needsSchema()) {
			INakedPackage schema=getNearestSchema(ns);
			if (schema!=null) {
				table.putAttribute(new OJAnnotationAttributeValue("schema", BACKTICK + schema.getMappingInfo().getPersistentName()+ BACKTICK));
			} else {
				table.putAttribute(new OJAnnotationAttributeValue("schema", BACKTICK + config.getDefaultSchema() + BACKTICK));
			}
		}
		owner.addAnnotationIfNew(table);
		return table;
	}

	public static void fetchLazy(OJAnnotationValue a) {
		a.putAttribute(new OJAnnotationAttributeValue("fetch", new OJEnumValue(new OJPathName("javax.persistence.FetchType"), "LAZY")));
	}

	public static OJAnnotationValue addJoinColumn(OJAnnotatedField f, String fk, boolean nullable) {
		OJAnnotationValue joinColumn = new OJAnnotationValue(new OJPathName("javax.persistence.JoinColumn"));
		joinColumn.putAttribute(new OJAnnotationAttributeValue("name", fk));
		joinColumn.putAttribute(new OJAnnotationAttributeValue("nullable", nullable || JpaAnnotator.DEVELOPMENT_MODE));
		f.addAnnotationIfNew(joinColumn);
		return joinColumn;
	}

	public static OJAnnotationValue addAuditJoinColumns(OJAnnotatedField f, String fk, boolean nullable) {
		OJAnnotationValue joinColumns = new OJAnnotationValue(new OJPathName("javax.persistence.JoinColumns"));
		OJAnnotationValue joinColumn1 = new OJAnnotationValue(new OJPathName("javax.persistence.JoinColumn"));
		joinColumn1.putAttribute(new OJAnnotationAttributeValue("name", fk + "_id"));
		joinColumns.addAnnotationValue(joinColumn1);
		OJAnnotationValue joinColumn2 = new OJAnnotationValue(new OJPathName("javax.persistence.JoinColumn"));
		joinColumn2.putAttribute(new OJAnnotationAttributeValue("name", fk + "_object_version"));
		joinColumns.addAnnotationValue(joinColumn2);
		f.addAnnotationIfNew(joinColumns);
		return joinColumns;
	}

	public static OJAnnotationValue addColumn(OJAnnotatedField f, String name, boolean nullable) {
		OJAnnotationValue column = new OJAnnotationValue(new OJPathName("javax.persistence.Column"));
		column.putAttribute(new OJAnnotationAttributeValue("name", name));
		column.putAttribute(new OJAnnotationAttributeValue("nullable", nullable || JpaAnnotator.DEVELOPMENT_MODE));
		f.addAnnotationIfNew(column);
		return column;
	}

	public static OJAnnotationValue addColumn(OJAnnotatedField f, String name, boolean nullable, int precision, int scale) {
		OJAnnotationValue column = new OJAnnotationValue(new OJPathName("javax.persistence.Column"));
		column.putAttribute(new OJAnnotationAttributeValue("name", name));
		column.putAttribute(new OJAnnotationAttributeValue("nullable", nullable || JpaAnnotator.DEVELOPMENT_MODE));
		column.putAttribute(new OJAnnotationAttributeValue("precision", precision));
		column.putAttribute(new OJAnnotationAttributeValue("scale", scale));
		f.addAnnotationIfNew(column);
		return column;
	}

	public static void cascadeAll(OJAnnotationValue a) {
		a.putAttribute(new OJAnnotationAttributeValue("cascade", new OJEnumValue(new OJPathName("javax.persistence.CascadeType"), "ALL")));
	}

	public static void addAndAnnotatedIdAndVersion(JpaIdStrategy jpaIdStrategy, OJAnnotatedClass ojClass, INakedComplexStructure complexType) {
		OJUtil.addProperty(ojClass, "id", new OJPathName(Long.class.getName()), true);
		JpaUtil.annotateId(jpaIdStrategy, complexType, ojClass);
		JpaUtil.annotateVersion(ojClass);
	}

	private static void annotateId(JpaIdStrategy jpaIdStrategy, INakedComplexStructure complexType, OJAnnotatedClass javaRoot) {
		OJAnnotatedField idField = (OJAnnotatedField) javaRoot.findField("id");
		jpaIdStrategy.annotate(idField);
		jpaIdStrategy.annotate(javaRoot, complexType);
	}

	private static void annotateVersion(OJAnnotatedClass javaRoot) {
		OJUtil.addProperty(javaRoot, "objectVersion", new OJPathName("int"), true);
		OJAnnotatedField versionField = (OJAnnotatedField) javaRoot.findField("objectVersion");
		OJAnnotationValue version = new OJAnnotationValue(new OJPathName("javax.persistence.Version"));
		versionField.putAnnotation(version);
		OJAnnotationValue column = new OJAnnotationValue(new OJPathName("javax.persistence.Column"));
		column.putAttribute(new OJAnnotationAttributeValue("name", "object_version"));
		versionField.putAnnotation(column);
	}

	public static void addEntity(OJAnnotatedClass ojClass) {
		OJAnnotationValue entity = new OJAnnotationValue(new OJPathName("javax.persistence.Entity"));
		entity.putAttribute(new OJAnnotationAttributeValue("name", ojClass.getName()));
		ojClass.addAnnotationIfNew(entity);
	}

	public static void addJoinTable(INakedClassifier umlOwner, NakedStructuralFeatureMap map, OJAnnotatedField field) {
		// ManyToMany or non-navigable XToMany
		INakedProperty f=map.getProperty();
		String tableName = calculateTableName(umlOwner, f);
		String keyToParentTable = calculateKeyToOwnerTable(f);
		OJAnnotationValue joinTable = new OJAnnotationValue(new OJPathName("javax.persistence.JoinTable"));
		joinTable.putAttribute(new OJAnnotationAttributeValue("name", tableName));
		OJAnnotationValue otherJoinColumn = new OJAnnotationValue(new OJPathName("javax.persistence.JoinColumn"));
		otherJoinColumn.putAttribute(new OJAnnotationAttributeValue("name", f.getMappingInfo().getPersistentName().getAsIs()));
		joinTable.putAttribute(new OJAnnotationAttributeValue("inverseJoinColumns", otherJoinColumn));
		OJAnnotationValue joinColumn = new OJAnnotationValue(new OJPathName("javax.persistence.JoinColumn"));
		joinColumn.putAttribute(new OJAnnotationAttributeValue("name", keyToParentTable));
		joinTable.putAttribute(new OJAnnotationAttributeValue("joinColumns", joinColumn));

		if(map.isOneToMany() && !(map.getProperty().getBaseType() instanceof INakedInterface)){
			//NB!!! unique==true messes the manyToAny mapping up
			otherJoinColumn.putAttribute(new OJAnnotationAttributeValue("unique", Boolean.TRUE));
			otherJoinColumn.putAttribute(new OJAnnotationAttributeValue("nullable", false));
			joinColumn.putAttribute(new OJAnnotationAttributeValue("nullable", false));
		}
		
		field.addAnnotationIfNew(joinTable);
	}

	static String calculateKeyToOwnerTable(INakedProperty f) {
		String keyToParentTable = null;
		if (f instanceof INakedProperty && (f).getAssociation() != null) {
			INakedProperty p = f;
			keyToParentTable = p.getOtherEnd().getMappingInfo().getPersistentName().getAsIs();
		} else {
			INakedClassifier nakedOwner = f.getOwner();
			keyToParentTable = nakedOwner.getMappingInfo().getPersistentName().toString();
		}
		return keyToParentTable;
	}

	static String calculateTableName(INakedClassifier umlOwner, INakedProperty f) {
		String tableName = null;
		if (f instanceof INakedProperty && (f).getAssociation() != null &&!(f.getOwner() instanceof INakedInterface)) {
			//For interfaces, create an association table per realization of the association.
			INakedProperty p = f;
			tableName = ((INakedAssociation) p.getAssociation()).getMappingInfo().getPersistentName().toString();
		} else {
			INakedClassifier nakedOwner = umlOwner;
			tableName = nakedOwner.getMappingInfo().getPersistentName() + "_" + f.getMappingInfo().getPersistentName().getWithoutId();
		}
		return tableName;
	}

	public static void addNamedQueryForUniquenessConstraints(OJAnnotatedClass ojClass, INakedEntity entity) {
		for (INakedProperty p : entity.getUniquenessConstraints()) {
			if (!p.getOtherEnd().getQualifiers().isEmpty()) {
				OJAnnotationValue namedQueries = ojClass.findAnnotation(new OJPathName("javax.persistence.NamedQueries"));
				if (namedQueries==null) {
					namedQueries = new OJAnnotationValue(new OJPathName("javax.persistence.NamedQueries"));
					ojClass.addAnnotationIfNew(namedQueries);
				}
				OJAnnotationAttributeValue oneToManyNamedQueryAttr = namedQueries.findAttribute("value");
				if (oneToManyNamedQueryAttr==null) {
					oneToManyNamedQueryAttr = new OJAnnotationAttributeValue("value");
					namedQueries.putAttribute(oneToManyNamedQueryAttr);
				}				
				
				OJAnnotationValue namedQuery = new OJAnnotationValue(new OJPathName("javax.persistence.NamedQuery"));
				NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(p);
				String queryName = "from " + entity.getMappingInfo().getJavaName() + " a where a." + map.umlName() + " = :" + map.umlName();
				for (INakedProperty q : p.getOtherEnd().getQualifiers()) {
					NakedStructuralFeatureMap qualifiedMap = new NakedStructuralFeatureMap(q);
					namedQuery.putAttribute("name", "Query" + entity.getMappingInfo().getJavaName() + "With" + NameConverter.capitalize(qualifiedMap.umlName())+"For"+NameConverter.capitalize(map.umlName()));
					queryName += " and a."+ qualifiedMap.umlName() + " = :" + qualifiedMap.umlName();
				}
				namedQuery.putAttribute("query", queryName);
				oneToManyNamedQueryAttr.addAnnotationValue(namedQuery);
				namedQueries.putAttribute(oneToManyNamedQueryAttr);
			}
		}
	}
	public static OJAnnotationValue buildFilterAnnotation(String name) {
		OJAnnotationValue filter = new OJAnnotationValue(new OJPathName(Filter.class.getName()));
		filter.putAttribute("name", "noDeletedObjects");
		return filter;
	}

}
