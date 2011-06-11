package org.nakeduml.tinker.basicjava.tinker;

import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.metamodel.core.INakedEntity;

import org.nakeduml.java.metamodel.OJPathName;
import org.nakeduml.java.metamodel.annotation.OJAnnotatedClass;
import org.nakeduml.name.NameConverter;

public class TinkerUtil {

	public static String tinkeriseUmlName(String umlName) {
		return umlName.replace("::", "__");
	}
	
	
	public static final OJPathName tinkerIdUtilPathName = new OJPathName("org.nakeduml.nakeduml.tinker.runtime.TinkerIdUtil");
	public static final OJPathName BASE_AUDIT_SOFT_DELETE_TINKER = new OJPathName("org.nakeduml.runtime.domain.BaseTinkerSoftDelete");
	public static final OJPathName BASE_TINKER = new OJPathName("org.nakeduml.runtime.domain.BaseTinker");
	public static final String BASE_AUDIT_TINKER = "org.nakeduml.runtime.domain.BaseTinkerAuditable";
	public static OJPathName oGraphDatabase = new OJPathName("com.orientechnologies.orient.core.db.graph.OGraphDatabase");
	public static OJPathName schemaPathName = new OJPathName("com.orientechnologies.orient.core.metadata.schema.OSchema");
	public static OJPathName vertexPathName = new OJPathName("com.tinkerpop.blueprints.pgm.Vertex");
	public static OJPathName orientVertexPathName = new OJPathName("com.tinkerpop.blueprints.pgm.impls.orientdb.OrientVertex");
	public static OJPathName tinkerFormatter = new OJPathName("org.util.TinkerFormatter");
	public static OJPathName transactionThreadVar = new OJPathName("org.util.TransactionThreadVar");
	public static OJPathName transactionThreadEntityVar = new OJPathName("org.util.TransactionThreadEntityVar");
	public static OJPathName graphDbPathName = new OJPathName("org.util.GraphDb");
	public static OJPathName tinkerAuditNodePathName = new OJPathName("org.nakeduml.runtime.domain.TinkerAuditNode");
	public static OJPathName tinkerUtil = new OJPathName("org.util.TinkerUtil");
	public static OJPathName tinkerHashSetImpl = new OJPathName("org.util.TinkerHashSet");
	public static OJPathName tinkerArrayListImpl = new OJPathName("org.util.TinkerArrayList");
	public static OJPathName tinkerEmbeddedHashSetImpl = new OJPathName("org.util.TinkerEmbeddedHashSet");
	public static OJPathName tinkerEmbeddedArrayListImpl = new OJPathName("org.util.TinkerEmbeddedArrayList");
	public static OJPathName tinkerSet = new OJPathName("org.util.TinkerSet");
	public static OJPathName tinkerList = new OJPathName("org.util.TinkerList");
	public static OJPathName edgePathName = new OJPathName("com.tinkerpop.blueprints.pgm.Edge");
	public static OJPathName storagePathName = new OJPathName("com.orientechnologies.orient.core.storage.OStorage");
	public static String graphDbAccess = "GraphDb.getDb()";
	public static OJPathName tinkerSchemaHelperPathName = new OJPathName("org.util.TinkerSchemaHelper");

	public static String constructSelfToAuditEdgeLabel(INakedEntity entity) {
		return "audit";
	}

	public static boolean calculateDirection(NakedStructuralFeatureMap map, boolean isComposite) {
		if (map.getProperty().getOtherEnd() == null) {
			return isComposite = true;
		} else if (map.isOneToOne() && !isComposite && !map.getProperty().getOtherEnd().isComposite()) {
			isComposite = map.getProperty().getMultiplicity().getLower() == 1 && map.getProperty().getMultiplicity().getUpper() == 1;
		} else if (map.isOneToMany() && !isComposite && !map.getProperty().getOtherEnd().isComposite()) {
			isComposite = map.getProperty().getMultiplicity().getUpper() > 1;
		} else if (map.isManyToMany() && !isComposite && !map.getProperty().getOtherEnd().isComposite()) {
			isComposite = 0 > map.getProperty().getName().compareTo(map.getProperty().getOtherEnd().getName());
		}
		return isComposite;
	}

	public static String constructTinkerCollectionInit(OJAnnotatedClass owner, NakedStructuralFeatureMap map) {
		if (map.getProperty().getOtherEnd() != null) {
			return map.umlName() + " = new " + (map.getProperty().isOrdered() ? "TinkerArrayList" : "TinkerHashSet") + "<" + map.javaBaseTypePath().getLast()
					+ ">(" + map.javaBaseTypePath().getLast() + ".class, this, " + owner.getName() + ".class.getMethod(\"addTo"
					+ NameConverter.capitalize(map.umlName()) + "\", new Class[]{" + map.javaBaseTypePath().getLast() + ".class}), " + owner.getName()
					+ ".class.getMethod(\"removeFrom" + NameConverter.capitalize(map.umlName()) + "\", new Class[]{" + map.javaBaseTypePath().getLast()
					+ ".class}))";
		} else {
			return map.umlName() + " = new " + (map.getProperty().isOrdered() ? "TinkerEmbeddedArrayList" : "TinkerEmbeddedHashSet") + "<"
					+ map.javaBaseTypePath().getLast() + ">(this, " + owner.getName() + ".class.getMethod(\"addTo" + NameConverter.capitalize(map.umlName())
					+ "\", new Class[]{" + map.javaBaseTypePath().getLast() + ".class}), " + owner.getName() + ".class.getMethod(\"removeFrom"
					+ NameConverter.capitalize(map.umlName()) + "\", new Class[]{" + map.javaBaseTypePath().getLast() + ".class}))";
		}
	}

	public static String getEdgeName(NakedStructuralFeatureMap map) {
		if (map.getProperty().getAssociation() != null) {
			return map.getProperty().getAssociation().getName();
		} else {
			return tinkeriseUmlName(map.getProperty().getMappingInfo().getQualifiedUmlName());
		}
	}

	public static String constructNameForInternalCreateAuditToOne(NakedStructuralFeatureMap map) {
		return "z_internalCreateAuditToOne" + NameConverter.capitalize(map.umlName());
	}

	public static String constructNameForInternalCreateAuditManies(NakedStructuralFeatureMap map) {
		return "z_internalCreateAuditToMany" + NameConverter.toPlural(NameConverter.capitalize(map.umlName()));
	}

	public static String constructNameForInternalCreateAuditMany(NakedStructuralFeatureMap map) {
		return "z_internalCreateAuditToMany" + NameConverter.capitalize(map.umlName());
	}

	public static String constructNameForInternalManiesRemoval(NakedStructuralFeatureMap map) {
		return "z_internalRemoveAllFrom" + NameConverter.capitalize(map.umlName());
	}

}
