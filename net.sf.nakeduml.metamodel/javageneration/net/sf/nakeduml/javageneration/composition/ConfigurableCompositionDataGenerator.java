package net.sf.nakeduml.javageneration.composition;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.nakeduml.feature.NakedUmlConfig;
import net.sf.nakeduml.feature.visit.VisitAfter;
import net.sf.nakeduml.feature.visit.VisitBefore;
import net.sf.nakeduml.javageneration.AbstractTestDataGenerator;
import net.sf.nakeduml.javageneration.CharArrayTextSource;
import net.sf.nakeduml.javageneration.JavaTextSource;
import net.sf.nakeduml.javageneration.NakedStructuralFeatureMap;
import net.sf.nakeduml.javageneration.persistence.JpaStrategy;
import net.sf.nakeduml.javageneration.util.OJUtil;
import net.sf.nakeduml.javametamodel.OJBlock;
import net.sf.nakeduml.javametamodel.OJField;
import net.sf.nakeduml.javametamodel.OJForStatement;
import net.sf.nakeduml.javametamodel.OJIfStatement;
import net.sf.nakeduml.javametamodel.OJPackage;
import net.sf.nakeduml.javametamodel.OJParameter;
import net.sf.nakeduml.javametamodel.OJPathName;
import net.sf.nakeduml.javametamodel.OJSimpleStatement;
import net.sf.nakeduml.javametamodel.OJVisibilityKind;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedClass;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedField;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotatedOperation;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotationAttributeValue;
import net.sf.nakeduml.javametamodel.annotation.OJAnnotationValue;
import net.sf.nakeduml.javametamodel.annotation.OJEnum;
import net.sf.nakeduml.javametamodel.annotation.OJEnumValue;
import net.sf.nakeduml.metamodel.core.INakedClassifier;
import net.sf.nakeduml.metamodel.core.INakedEntity;
import net.sf.nakeduml.metamodel.core.INakedHelperClass;
import net.sf.nakeduml.metamodel.core.INakedInterface;
import net.sf.nakeduml.metamodel.core.INakedInterfaceRealization;
import net.sf.nakeduml.metamodel.core.INakedPrimitiveType;
import net.sf.nakeduml.metamodel.core.INakedProperty;
import net.sf.nakeduml.metamodel.core.INakedSimpleType;
import net.sf.nakeduml.metamodel.models.INakedModel;
import net.sf.nakeduml.metamodel.workspace.INakedModelWorkspace;
import net.sf.nakeduml.name.NameConverter;
import net.sf.nakeduml.textmetamodel.PropertiesSource;
import net.sf.nakeduml.textmetamodel.TextOutputRoot;
import net.sf.nakeduml.textmetamodel.TextWorkspace;
import nl.klasse.octopus.model.IEnumerationType;

public class ConfigurableCompositionDataGenerator extends AbstractTestDataGenerator {

	private Map<String, String> properties = new HashMap<String, String>();

	@Override
	public void initialize(INakedModelWorkspace workspace, OJPackage javaModel, NakedUmlConfig config, TextWorkspace textWorkspace) {
		super.initialize(workspace, javaModel, config, textWorkspace);
	}

	@VisitAfter
	public void visit(INakedModel model) {

		if (this.config.getDataGeneration()) {
			// Out the properties file
			Properties props = new Properties();
			props.putAll(this.properties);
			try {
				props.store(new FileWriter(CharArrayTextSource.EJB_JAR_RESOURCE), "auto generated");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			TextOutputRoot outputRoot = textWorkspace.findOrCreateTextOutputRoot(PropertiesSource.GEN_RESOURCE);
			List<String> path = Arrays.asList("data.generation.properties");
			outputRoot.findOrCreateTextFile(path, new PropertiesSource(props));
		}

	}

	@VisitBefore(matchSubclasses = true)
	public void visit(INakedEntity c) {

		if (OJUtil.hasOJClass(c) && !c.getIsAbstract()) {
			OJAnnotatedClass testDataClass = new OJAnnotatedClass();
			OJAnnotatedClass theClass = findJavaClass(c);
			testDataClass.setName(getTestDataName(c));
			theClass.getMyPackage().addToClasses(testDataClass);

			annotateClass(c, testDataClass);

			addChildrenFields(c, testDataClass);
			addPropertyUtil(testDataClass);
			addCreate(c, testDataClass);

			addPopulateData(c, testDataClass, false);

			addPopulateData(c, testDataClass, true);

			super.createTextPath(testDataClass, JavaTextSource.GEN_SRC);
		}

	}

	private void annotateClass(INakedEntity c, OJAnnotatedClass testDataClass) {
		if (config.isSeamAnnotations()) {
			OJAnnotationValue name = new OJAnnotationValue(new OJPathName("org.jboss.seam.annotations.Name"), NameConverter.decapitalize(getTestDataName(c)));
			testDataClass.putAnnotation(name);
			OJAnnotationValue autoCreate = new OJAnnotationValue(new OJPathName("org.jboss.seam.annotations.AutoCreate"));
			testDataClass.putAnnotation(autoCreate);

			OJAnnotationValue scope = new OJAnnotationValue(new OJPathName("org.jboss.seam.annotations.Scope"));
			scope.putAttribute(new OJAnnotationAttributeValue("value", new OJEnumValue(new OJPathName("org.jboss.seam.ScopeType"), "EVENT")));
			testDataClass.putAnnotation(scope);
		} else {

		}
	}

	private void addPropertyUtil(OJAnnotatedClass testDataClass) {
		OJAnnotatedField propertyUtil = new OJAnnotatedField();
		propertyUtil.setName("dataGeneratorProperty");
		propertyUtil.setType(new OJPathName("net.sf.nakeduml.util.DataGeneratorProperty"));
		if (config.isSeamAnnotations()) {
			OJAnnotationValue in = new OJAnnotationValue(new OJPathName("org.jboss.seam.annotations.In"));
			propertyUtil.putAnnotation(in);
		} else {
			OJAnnotationValue in = new OJAnnotationValue(new OJPathName("javax.inject.Inject"));
			propertyUtil.putAnnotation(in);
		}
		testDataClass.addToFields(propertyUtil);
	}

	private void addPopulateData(INakedEntity c, OJAnnotatedClass testDataClass, boolean forExport) {
		final String FUNCTION = forExport ? "export" : "populate";

		OJAnnotatedOperation populate = new OJAnnotatedOperation();
		populate.setName(FUNCTION + c.getMappingInfo().getJavaName());
		populate.setBody(new OJBlock());

		OJField count = new OJField();
		count.setName("count");
		count.setType(new OJPathName("java.util.Integer"));
		count.setInitExp("0");
		populate.getBody().addToLocals(count);

		OJBlock block;
		INakedProperty parent = c.getEndToComposite();
		if (parent != null) {
			OJParameter owner = new OJParameter();
			owner.setName(parent.getMappingInfo().getJavaName().toString());
			NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(parent);
			OJPathName javaTypePath = map.javaTypePath();
			owner.setType(javaTypePath);
			populate.addToParameters(owner);
			OJPathName instancePath = new OJPathName(c.getMappingInfo().getQualifiedJavaName());
			NakedStructuralFeatureMap otherMap = new NakedStructuralFeatureMap(parent.getOtherEnd());

			if (otherMap.isMany()) {
				if (forExport) {
					populate.getBody().addToStatements(
							"dataGeneratorProperty.putExportProperty(" + parent.getMappingInfo().getJavaName().toString() + ".getName() + " + "\"."
									+ c.getMappingInfo().getJavaName().getDecapped() + ".size\"," + parent.getMappingInfo().getJavaName().toString() + "."
									+ otherMap.getter() + "().size())");
				}
				OJForStatement forX = new OJForStatement();
				forX.setElemType(otherMap.javaBaseTypePath());
				forX.setElemName("iter");
				forX.setCollection(parent.getMappingInfo().getJavaName().toString() + "." + otherMap.getter() + "()");

				OJIfStatement ifStatement = new OJIfStatement();
				ifStatement.setCondition("iter instanceof " + instancePath.getLast());
				OJBlock ifBlock = new OJBlock();
				ifStatement.setThenPart(ifBlock);

				OJField instance = new OJField();
				instance.setName(c.getMappingInfo().getJavaName().getDecapped().toString());
				instance.setType(instancePath);
				instance.setInitExp("(" + instancePath.getLast() + ")iter");
				ifBlock.addToLocals(instance);

				OJBlock forBlock = new OJBlock();
				forX.setBody(forBlock);
				forBlock.addToStatements(ifStatement);

				block = ifBlock;
				populate.getBody().addToStatements(forX);

			} else {
				OJField field = new OJField();
				field.setName(NameConverter.decapitalize(c.getName()));
				field.setType(instancePath);
				field.setInitExp("(" + instancePath.getLast() + ")" + parent.getMappingInfo().getJavaName().toString() + "." + otherMap.getter() + "()");
				populate.getBody().addToLocals(field);

				OJIfStatement checkNull = new OJIfStatement(field.getName() + "==null", "return");
				populate.getBody().addToStatements(checkNull);

				block = populate.getBody();
			}
		} else {
			OJParameter owner = new OJParameter();
			owner.setName(c.getMappingInfo().getJavaName().getDecapped().toString());
			OJPathName javaTypePath = new OJPathName(c.getMappingInfo().getQualifiedJavaName());
			owner.setType(javaTypePath);
			populate.addToParameters(owner);
			block = populate.getBody();
		}
		testDataClass.addToOperations(populate);
		if (forExport) {
			populateSelf(testDataClass, c, block, true);
			populateToOneOrManyToMany(testDataClass, c, block);
		} else {
			populateSourcePopulationSelf(testDataClass, c, block);
		}

		block.addToStatements(new OJSimpleStatement("count++"));

		addPopulateChildren(c, testDataClass, block, forExport);
	}

	private void populateToOneOrManyToMany(OJAnnotatedClass test, INakedEntity c, OJBlock populate) {

		for (INakedProperty f : c.getEffectiveAttributes()) {
			NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(f);
			if (f instanceof INakedProperty) {
				boolean isEndToComposite = f.getOtherEnd() != null && f.getOtherEnd().isComposite();
				if (f.getInitialValue() == null && !isEndToComposite) {
					if ((map.isManyToMany() || map.isOne()) && !(f.isDerived() || f.isReadOnly() || f.isInverse())) {
						if (map.couldBasetypeBePersistent()) {
							if (map.isManyToMany()) {

								OJForStatement forMany = new OJForStatement(f.getMappingInfo().getJavaName().getDecapped().toString(), map.javaBaseTypePath(),
										c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter() + "()");

								INakedProperty parent = c.getEndToComposite();
								NakedStructuralFeatureMap otherMap = null;
								if (parent != null) {
									otherMap = new NakedStructuralFeatureMap(parent.getOtherEnd());
								}
								String varName = otherMap != null && otherMap.isMany() ? "iter" : c.getMappingInfo().getJavaName().getDecapped().toString();
								forMany.getBody().addToStatements(
										"dataGeneratorProperty.putExportProperty(" + varName + ".getName() + \"." + map.setter() + "_\" + count, "
												+ f.getMappingInfo().getJavaName().getDecapped().toString() + ".getName())");

								populate.addToStatements(forMany);

							} else {
								String javaType = map.javaType();
								if (map.isManyToMany()) {
									javaType = javaType.replace("<", "");
									javaType = javaType.replace(">", "");
								}
								String property = c.getMappingInfo().getJavaName().getDecapped().toString() + ".getName()" + "+\"."
										+ NameConverter.decapitalize(javaType) + "\"";
								populate.addToStatements(new OJIfStatement(c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter()
										+ "()!=null", "dataGeneratorProperty.putExportProperty(" + property + ", "
										+ c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter() + "().getName())"));
							}

						} else if (f.getNakedBaseType() instanceof INakedInterface) {
							populate.addToStatements("!!!" + f.getNakedBaseType().getName()
									+ " does not have any persistent implementations. Source population is not resolvable");

						}
					}
				}
			}
		}
	}

	private void populateSelf(OJAnnotatedClass test, INakedEntity c, OJBlock populate, boolean forExport) {
		for (INakedProperty f : c.getEffectiveAttributes()) {
			NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(f);
			boolean isReadOnly = (f instanceof INakedProperty && (f).isReadOnly());
			if (f instanceof INakedProperty) {
				INakedProperty p = f;
				boolean isEndToComposite = p.getOtherEnd() != null && p.getOtherEnd().isComposite();
				if (p.getInitialValue() == null && !isEndToComposite) {
					if (map.isOne() && !(f.isDerived() || isReadOnly || f.isInverse())) {
						String defaultValue = calculateDefaultStringValue(test, populate, f);
						if (!(map.couldBasetypeBePersistent())) {
							if (!forExport) {
								String str = calcConfiguredValue(test, populate, c, f, defaultValue);
								populate.addToStatements(c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.setter() + "(" + str + ")");
								constructPropertyName(c, p, defaultValue);
							} else {
								String result = "";
								if (c.getEndToComposite() != null) {
									result = c.getEndToComposite().getName() + ".getName() + \"." + NameConverter.decapitalize(c.getName()) + "." + p.getName()
											+ "_\" + String.valueOf(count)";
									populate.addToStatements("dataGeneratorProperty.putExportProperty(" + result + ", "
											+ c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter() + "())");
								} else {
									result = NameConverter.decapitalize(c.getName()) + "." + p.getName() + "_\" + String.valueOf(count)";
									populate.addToStatements("dataGeneratorProperty.putExportProperty(\"" + result + ", "
											+ c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter() + "())");
								}
							}
						}
					}
				}
			}
		}
	}

	private void populateSourcePopulationSelf(OJAnnotatedClass test, INakedEntity c, OJBlock populate) {
		for (INakedProperty f : c.getEffectiveAttributes()) {
			NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(f);
			if (f instanceof INakedProperty) {
				boolean isEndToComposite = f.getOtherEnd() != null && f.getOtherEnd().isComposite();
				if (f.getInitialValue() == null && !isEndToComposite) {
					if ((map.isManyToMany() || map.isOne()) && !(f.isDerived() || f.isReadOnly() || f.isInverse())) {
						if (map.couldBasetypeBePersistent()) {
							String defaultValue = super.lookup(test, f);
							OJIfStatement ifSourcePopulationNotEmpty = new OJIfStatement();
							ifSourcePopulationNotEmpty.setCondition("!" + c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter()
									+ "SourcePopulation().isEmpty()");
							OJBlock ifBlock = new OJBlock();
							OJForStatement forSourcePopulation = new OJForStatement();
							forSourcePopulation.setElemType(OJUtil.classifierPathname(f.getNakedBaseType()));
							test.addToImports(OJUtil.classifierPathname(f.getNakedBaseType()));
							// TODO use map.javaBaseType()
							String javaType = map.javaType();
							if (map.isManyToMany()) {
								javaType = javaType.replace("<", "");
								javaType = javaType.replace(">", "");
							}
							forSourcePopulation.setElemName(NameConverter.decapitalize(javaType) + "Internal");
							forSourcePopulation.setCollection(c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter()
									+ "SourcePopulation()");
							ifBlock.addToStatements(forSourcePopulation);
							ifSourcePopulationNotEmpty.setThenPart(ifBlock);
							OJBlock forSourcePopulationBlock = new OJBlock();
							forSourcePopulation.setBody(forSourcePopulationBlock);
							if (map.isManyToMany()) {
								OJBlock forInternalPropertyBlock = createRetrievalOfMany(c, map, defaultValue, forSourcePopulationBlock);
								OJIfStatement ifMatch = createIfMatch(c, map, defaultValue, javaType);
								forInternalPropertyBlock.addToStatements(ifMatch);
							} else {
								OJIfStatement ifStatement2 = new OJIfStatement();
								ifStatement2.setCondition(NameConverter.decapitalize(javaType + "Internal")
										+ ".getName().equals(dataGeneratorProperty.getProperty(" + constructSourcePopulationProperty(c, map, defaultValue)
										+ "\"))");
								OJBlock forBlock2 = new OJBlock();

								forBlock2.addToStatements(c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.setter() + "("
										+ NameConverter.decapitalize(javaType + "Internal") + ")");
								ifStatement2.setThenPart(forBlock2);
								forSourcePopulationBlock.addToStatements(ifStatement2);
							}
							populate.addToStatements(ifSourcePopulationNotEmpty);

						} else if (f.getNakedBaseType() instanceof INakedInterface) {
							populate.addToStatements("!!!" + f.getNakedBaseType().getName()
									+ " does not have any persistent implementations. Source population is not resolvable");

						}
					}
				}
			}
		}
	}

	public OJIfStatement createIfMatch(INakedEntity c, NakedStructuralFeatureMap map, String defaultValue, String javaType) {
		OJIfStatement ifMatch = new OJIfStatement();
		ifMatch.setCondition("propertyKey.equals(" + constructSourcePopulationProperty(c, map, defaultValue) + "_\"+i ) && dataGeneratorProperty.getProperty("
				+ constructSourcePopulationProperty(c, map, defaultValue) + "_\"+i ).equals(" + NameConverter.decapitalize(javaType + "Internal")
				+ ".getName())");
		OJBlock ifMatchThenPart = new OJBlock();
		ifMatchThenPart.addToStatements(c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.adder() + "("
				+ NameConverter.decapitalize(javaType + "Internal") + ")");
		ifMatchThenPart.addToStatements("break");
		ifMatch.setThenPart(ifMatchThenPart);
		return ifMatch;
	}

	public OJBlock createRetrievalOfMany(INakedEntity c, NakedStructuralFeatureMap map, String defaultValue, OJBlock forBlock) {
		OJField propertyKeys = new OJField();
		propertyKeys.setName("propertyKeys");
		OJPathName stringSet = new OJPathName("java.util.List");
		List<OJPathName> elementTypes = new ArrayList<OJPathName>();
		elementTypes.add(new OJPathName("java.lang.String"));
		stringSet.setElementTypes(elementTypes);
		propertyKeys.setType(stringSet);
		propertyKeys.setInitExp("dataGeneratorProperty.getPropertiesThatStartsWith(" + constructSourcePopulationProperty(c, map, defaultValue) + "\")");
		forBlock.addToLocals(propertyKeys);

		OJForStatement propertyKeysFor = new OJForStatement("", "", "propertyKey", "propertyKeys");
		propertyKeysFor.setElemType(new OJPathName("java.lang.String"));
		propertyKeysFor.setBody(new OJBlock());
		forBlock.addToStatements(propertyKeysFor);

		OJForStatement internalPropertyLoop = new OJForStatement("", "", "i", "dataGeneratorProperty.getSizeListOfPropertiesThatStartsWith("
				+ constructSourcePopulationProperty(c, map, defaultValue) + "\")");
		internalPropertyLoop.setElemType(new OJPathName("java.lang.Integer"));
		internalPropertyLoop.setBody(new OJBlock());

		propertyKeysFor.getBody().addToStatements(internalPropertyLoop);

		return internalPropertyLoop.getBody();
	}

	private String calculateDefaultStringValue(OJAnnotatedClass test, OJBlock block, INakedProperty f) {
		if (f.getNakedBaseType() instanceof IEnumerationType) {
			OJEnum javaType = (OJEnum) findJavaClass(f.getNakedBaseType());
			if (javaType.getLiterals().size() > 0) {
				return javaType.getLiterals().get(0).getName();
			} else {
				return javaType.getName() + ".has no literals!!!!";
			}
		} else if (f.getNakedBaseType() instanceof INakedSimpleType) {
			INakedSimpleType baseType = (INakedSimpleType) f.getNakedBaseType();
			if (baseType.hasStrategy(ConfigurableDataStrategy.class)) {
				return baseType.getStrategy(ConfigurableDataStrategy.class).getDefaultStringValue(test, block, f);
			} else if (f.getNakedBaseType() instanceof INakedPrimitiveType) {
				String calculateDefaultValue = calculateDefaultValue(test, block, f);
				if (calculateDefaultValue.startsWith("\"") && calculateDefaultValue.endsWith("\"")) {
					calculateDefaultValue = calculateDefaultValue.substring(1, calculateDefaultValue.length() - 1);
				}
				return calculateDefaultValue;
			} else {
				return "no ConfigurableDataStrategy strategy!!!";
			}
		}
		return "BLASDFASDFadsf";
		// throw new RuntimeException(f.getBaseType().getPathName() +
		// " not supported");
	}

	private String calcConfiguredValue(OJAnnotatedClass clazz, OJBlock block, INakedEntity c, INakedProperty f, String defaultStringValue) {
		String configuredValue = "dataGeneratorProperty.getProperty(" + calcMapKey(c, f) + "+i, \"" + defaultStringValue + "\")";
		if (f.getNakedBaseType() instanceof INakedSimpleType) {
			INakedSimpleType baseType = (INakedSimpleType) f.getNakedBaseType();
			if (baseType.hasStrategy(JpaStrategy.class)) {
				return baseType.getStrategy(ConfigurableDataStrategy.class).parseConfiguredValue(clazz, block, f, configuredValue);
			} else if (f.getNakedBaseType() instanceof INakedPrimitiveType) {
				INakedPrimitiveType t = (INakedPrimitiveType) f.getNakedBaseType();
				if (t.getOclType().getName().equals("Integer")) {
					return "Integer.valueOf(" + configuredValue + ")";
				} else if (t.getOclType().getName().equals("Real")) {
					return "new Double(" + configuredValue + ")";
				} else if (t.getOclType().getName().equals("Boolean")) {
					return "Boolean.valueOf(" + configuredValue + ")";
				} else if (t.getOclType().getName().equals("String")) {
					return configuredValue;
				} else if (f.getName().equals("name")) {
					return configuredValue;
				}
			}
			return "no configurable data generation strategy!!!";

		} else if (f.getNakedBaseType() instanceof IEnumerationType) {
			OJEnum javaType = (OJEnum) findJavaClass(f.getNakedBaseType());
			clazz.addToImports(javaType.getPathName());
			if (javaType.getLiterals().size() > 0) {
				return javaType.getName() + ".valueOf(" + configuredValue + ")";
			} else {
				return javaType.getName() + ".has no literals!!!!";
			}

		} else if (f.getBaseType() instanceof INakedHelperClass) {
			return "new " + f.getBaseType().getName() + "()";
		} else {
			throw new RuntimeException("Not implemented");
			// return "\"" + f.getOwner().getName() + "::" + f.getName() + new
			// Double(value).intValue() + "\"";
		}
	}

	private void addPopulateChildren(INakedEntity c, OJAnnotatedClass testDataClass, OJBlock block, boolean forExport) {
		final String FUNCTION = forExport ? "export" : "populate";
		for (INakedProperty a : c.getEffectiveAttributes()) {
			INakedProperty p = (INakedProperty) a;
			if (p.isComposite() && !isHierarchical(c, p)) {

				NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(p);
				
				if (p.getNakedBaseType() instanceof INakedInterface && map.isOne()) {
					//TODO sourcePopulation needs to handle redefinition and looks like WorkspaceElement.name has duplicates
//					INakedInterface inf = (INakedInterface) p.getNakedBaseType();
//					Collection<INakedClassifier> classifiers = inf.getImplementingClassifiers();
//					for (INakedClassifier iNakedClassifier : classifiers) {
//						if (!iNakedClassifier.getIsAbstract()) {
//							
//							OJIfStatement ifInstanceOf = new OJIfStatement(c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter() + "()!=null && " + c.getMappingInfo().getJavaName().getDecapped().toString() + "." + map.getter() + "() instanceof " + iNakedClassifier.getMappingInfo().getJavaName().toString());
//							String s = "dataGeneratorProperty.putExportProperty(" + c.getMappingInfo().getJavaName().getDecapped().toString() + ".getName() + \"" + p.getMappingInfo().getJavaName().getDecapped().toString() + "\", \"" + iNakedClassifier.getMappingInfo().getQualifiedJavaName() + "\")";
//							ifInstanceOf.addToThenPart(new OJSimpleStatement(s));
//							s = NameConverter.decapitalize(getTestDataName(iNakedClassifier)) + "." + FUNCTION
//							+ iNakedClassifier.getMappingInfo().getJavaName() + "(" + c.getMappingInfo().getJavaName().getDecapped().toString() + ")";
//							ifInstanceOf.addToThenPart(new OJSimpleStatement(s));
//							
//							block.addToStatements(ifInstanceOf);
//							testDataClass.addToImports(OJUtil.classifierPathname(iNakedClassifier));
//						}
//					}
					
				} else {
					if (!p.getNakedBaseType().getIsAbstract()) {
						block.addToStatements(new OJSimpleStatement(NameConverter.decapitalize(getTestDataName(p.getNakedBaseType())) + "." + FUNCTION
								+ p.getNakedBaseType().getMappingInfo().getJavaName() + "(" + c.getMappingInfo().getJavaName().getDecapped().toString() + ")"));
					}
				}

			} else if (p.isComposite() && !p.getNakedBaseType().getIsAbstract() && isHierarchical(c, p)) {
				block.addToStatements(new OJSimpleStatement(FUNCTION + p.getNakedBaseType().getMappingInfo().getJavaName() + "("
						+ c.getMappingInfo().getJavaName().getDecapped().toString() + ")"));
			}
		}
	}

	private OJAnnotatedOperation addCreate(INakedEntity c, OJAnnotatedClass testDataClass) {

		OJPathName element = new OJPathName(c.getMappingInfo().getQualifiedJavaName());
		if (isHierachical(c)) {
			// Build createNonHierarchicalXXX
			OJAnnotatedOperation createNonHierarchical = new OJAnnotatedOperation();
			createNonHierarchical.addToThrows(new OJPathName("java.text.ParseException"));

			createNonHierarchical.setName("createNonHierarchical" + c.getMappingInfo().getJavaName());
			OJPathName returnType = element;
			createNonHierarchical.setReturnType(returnType);
			createNonHierarchical.setVisibility(OJVisibilityKind.PRIVATE);
			testDataClass.addToOperations(createNonHierarchical);
			INakedProperty parent = c.getEndToComposite();
			OJBlock block = new OJBlock();
			createNonHierarchical.setBody(block);
			OJField local = new OJField();
			local.setName(c.getMappingInfo().getJavaName().getDecapped().toString());
			local.setType(returnType);
			if (parent != null) {
				OJParameter owner = new OJParameter();
				owner.setName(parent.getMappingInfo().getJavaName().toString());
				NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(parent);
				OJPathName javaTypePath = map.javaTypePath();
				owner.setType(javaTypePath);
				createNonHierarchical.addToParameters(owner);

				OJParameter localI = new OJParameter();
				localI.setType(new OJPathName("java.lang.Integer"));
				localI.setName("i");
				createNonHierarchical.addToParameters(localI);

				local.setInitExp("new " + c.getMappingInfo().getJavaName() + "(" + parent.getMappingInfo().getJavaName().toString() + ")");
			} else {
				local.setInitExp("new " + c.getMappingInfo().getJavaName() + "()");
			}
			block.addToLocals(local);

			populateSelf(testDataClass, c, block, false);

			addCompositeChildren(c, block);
			OJSimpleStatement returnStatement = new OJSimpleStatement("return " + local.getName());
			block.addToStatements(returnStatement);

			// Create method
			OJAnnotatedOperation create = new OJAnnotatedOperation();
			create.addToThrows(new OJPathName("java.text.ParseException"));
			testDataClass.addToImports(new OJPathName("java.text.ParseException"));
			create.setName("create" + c.getMappingInfo().getJavaName());
			testDataClass.addToOperations(create);
			parent = c.getEndToComposite();
			block = new OJBlock();
			create.setBody(block);

			OJPathName integerPath = new OJPathName("java.util.Integer");
			OJForStatement forX = new OJForStatement();
			forX.setElemType(integerPath);
			forX.setElemName("i");

			forX.setCollection("dataGeneratorProperty.getIterationListForSizeProperty(\"" + constructSizePropertyName(c) + "\",\"0\")");
			OJBlock forXBLock = new OJBlock();
			block.addToStatements(forX);
			forX.setBody(forXBLock);

			local = new OJField();
			local.setName(c.getMappingInfo().getJavaName().getDecapped().toString());
			local.setType(returnType);

			if (parent != null) {
				OJParameter owner = new OJParameter();
				owner.setName(parent.getMappingInfo().getJavaName().toString());
				NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(parent);
				OJPathName javaTypePath = map.javaTypePath();
				owner.setType(javaTypePath);
				create.addToParameters(owner);
				local.setInitExp("createNonHierarchical" + c.getMappingInfo().getJavaName() + "(" + parent.getMappingInfo().getJavaName().toString() + ",i)");
			} else {
				throw new RuntimeException("Not supported!");
			}
			forXBLock.addToLocals(local);
			forXBLock.addToStatements(new OJSimpleStatement(local.getName() + " = createNonHierarchical" + c.getMappingInfo().getJavaName() + "("
					+ local.getName() + ",i)"));
			forXBLock.addToStatements("result.add(" + c.getMappingInfo().getJavaName().getDecapped() + ")");

			OJPathName returnList = new OJPathName("java.util.List");
			returnList.addToElementTypes(element);
			create.setReturnType(returnList);
			OJField listReturn = new OJField();
			listReturn.setType(returnList);
			listReturn.setName("result");
			listReturn.setInitExp("new ArrayList<" + element.getLast() + ">()");
			testDataClass.addToImports(new OJPathName("java.util.ArrayList"));
			create.getBody().addToLocals(listReturn);

			create.getBody().addToStatements("return " + listReturn.getName());

			return create;

		} else {
			OJAnnotatedOperation create = new OJAnnotatedOperation();
			create.setName("create" + c.getMappingInfo().getJavaName());
			testDataClass.addToOperations(create);
			OJBlock block = new OJBlock();
			create.setBody(block);
			create.addToThrows(new OJPathName("java.text.ParseException"));
			testDataClass.addToImports(new OJPathName("java.text.ParseException"));

			INakedProperty parent = c.getEndToComposite();
			OJPathName integerPath = new OJPathName("java.util.Integer");

			OJField local = new OJField();
			local.setName(c.getMappingInfo().getJavaName().getDecapped().toString());
			local.setType(element);
			if (parent != null) {
				OJParameter owner = new OJParameter();
				owner.setName(parent.getMappingInfo().getJavaName().toString());
				NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(parent);
				OJPathName javaTypePath = map.javaTypePath();
				owner.setType(javaTypePath);
				create.addToParameters(owner);
				local.setInitExp("new " + c.getMappingInfo().getJavaName() + "(" + parent.getMappingInfo().getJavaName().toString() + ")");

				NakedStructuralFeatureMap otherMap = new NakedStructuralFeatureMap(parent.getOtherEnd());
				if (otherMap.isMany()) {
					// Have for loop
					OJForStatement forX = new OJForStatement();
					forX.setElemType(integerPath);
					forX.setElemName("i");
					constructSizePropertyName(c);
					forX.setCollection("dataGeneratorProperty.getIterationListForSizeProperty(" + parent.getMappingInfo().getJavaName().toString()
							+ ".getName()+\"." + c.getMappingInfo().getJavaName().getDecapped().toString() + ".size\",\"0\")");
					OJBlock forXBLock = new OJBlock();
					forX.setBody(forXBLock);

					block.addToStatements(forX);
					block = forXBLock;
				} else {
					// Add variable i
					OJField variableI = new OJField();
					variableI.setType(integerPath);
					variableI.setName("i");
					variableI.setInitExp("0");
					block.addToLocals(variableI);
				}

			} else {
				local.setInitExp("new " + c.getMappingInfo().getJavaName() + "()");

				OJForStatement forX = new OJForStatement();
				forX.setElemType(integerPath);
				forX.setElemName("i");
				forX.setCollection("dataGeneratorProperty.getIterationListForSizeProperty(\"" + constructSizePropertyName(c) + "\",\"0\")");
				OJBlock forXBLock = new OJBlock();
				forX.setBody(forXBLock);

				block.addToStatements(forX);
				block = forXBLock;
				// Always have for loop
			}
			OJPathName returnList = new OJPathName("java.util.List");
			returnList.addToElementTypes(element);
			create.setReturnType(returnList);
			OJField listReturn = new OJField();
			listReturn.setType(returnList);
			listReturn.setName("result");
			listReturn.setInitExp("new ArrayList<" + element.getLast() + ">()");
			testDataClass.addToImports(new OJPathName("java.util.ArrayList"));
			create.getBody().addToLocals(listReturn);
			block.addToLocals(local);

			populateSelf(testDataClass, c, block, false);
			block.addToStatements("result.add(" + c.getMappingInfo().getJavaName().getDecapped() + ")");

			addCompositeChildren(c, block);
			create.getBody().addToStatements("return " + listReturn.getName());
			return create;
		}
	}

	private String constructSourcePopulationProperty(INakedEntity entity, NakedStructuralFeatureMap map, String defaultValue) {
		String javaType = map.javaType();
		if (map.isManyToMany()) {
			javaType = javaType.replace("<", "");
			javaType = javaType.replace(">", "");
		}
		String result = entity.getMappingInfo().getJavaName().getDecapped().toString() + ".getName()" + "+\"." + NameConverter.decapitalize(javaType);
		properties.put(result, defaultValue);
		return result;
	}

	private String constructPropertyName(INakedEntity owner, INakedProperty property, String defaultValue) {
		String result;
		if (owner.getEndToComposite() != null) {
			result = owner.getEndToComposite().getName() + "." + NameConverter.decapitalize(owner.getName()) + "." + property.getName() + "_0";
		} else {
			result = NameConverter.decapitalize(owner.getName()) + "." + property.getName() + "_0";
		}
		properties.put(result, defaultValue);
		return result;
	}

	private String constructSizePropertyName(INakedEntity entity) {
		String property;
		if (entity.getEndToComposite() != null) {
			NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(entity.getEndToComposite().getOtherEnd());
			if (map.isMany()) {
				property = entity.getEndToComposite().getName() + "." + NameConverter.decapitalize(entity.getName()) + ".size";
			} else {
				throw new RuntimeException("Code should not come here");
			}
		} else {
			property = NameConverter.decapitalize(entity.getName()) + ".size";
		}
		properties.put(property, "0");
		return property;
	}

	private boolean isHierachical(INakedEntity c) {
		for (INakedProperty a : c.getEffectiveAttributes()) {
			if (isHierarchical(c, a)) {
				return true;
			}
		}
		return false;
	}

	private boolean isHierarchical(INakedEntity c, INakedProperty p) {
		if (p.isComposite() && p.getNakedBaseType().equals(c)) {
			return true;
		}
		return false;
	}

	private void addCompositeChildren(INakedEntity c, OJBlock block) {
		for (INakedProperty a : c.getEffectiveAttributes()) {
			INakedProperty p = (INakedProperty) a;
			NakedStructuralFeatureMap map = new NakedStructuralFeatureMap(p);
			if (p.isComposite() && !isHierarchical(c, p)) {

				if (p.getNakedBaseType() instanceof INakedInterface && map.isOne()) {
					//TODO sourcePopulation needs to handle redefinition and looks like WorkspaceElement.name has duplicates

//					INakedInterface inf = (INakedInterface) p.getNakedBaseType();
//					Collection<INakedClassifier> classifiers = inf.getImplementingClassifiers();
//					for (INakedClassifier iNakedClassifier : classifiers) {
//						String result = c.getMappingInfo().getJavaName().getDecapped().toString() + ".getName() + \"." +p.getMappingInfo().getJavaName().getDecapped().toString() + "\"";
//						OJIfStatement ifInstanceOf = new OJIfStatement("dataGeneratorProperty.getProperty(" + result + ",\"\").equals(\""
//								+ iNakedClassifier.getMappingInfo().getQualifiedJavaName().toString() + "\")", iNakedClassifier.getMappingInfo().getJavaName()
//								.getDecapped().toString()
//								+ "DataGenerator.create"
//								+ iNakedClassifier.getMappingInfo().getJavaName().toString()
//								+ "("
//								+ c.getMappingInfo().getJavaName().getDecapped().toString() + ")");
//						block.addToStatements(ifInstanceOf);
//					}

				} else {
					if (!p.getNakedBaseType().getIsAbstract()) {
						OJPathName baseTypePath = getTestDataPath(p.getNakedBaseType());
						OJSimpleStatement ojSimpleStatement = new OJSimpleStatement();
						ojSimpleStatement.setExpression(NameConverter.decapitalize(baseTypePath.getLast()) + ".create"
								+ p.getNakedBaseType().getMappingInfo().getJavaName() + "(" + c.getMappingInfo().getJavaName().getDecapped() + ")");
						block.addToStatements(ojSimpleStatement);
					}
				}
			}
		}
	}

	private void addChildrenFields(INakedEntity c, OJAnnotatedClass testDataClass) {
		for (INakedProperty f : c.getEffectiveAttributes()) {
			INakedProperty p = f;
			if (f.isComposite() && !isHierarchical(c, p)) {
				//TODO sourcePopulation needs to handle redefinition and looks like WorkspaceElement.name has duplicates
				
				if (f.getNakedBaseType() instanceof INakedInterface) {
//					INakedInterface inf = (INakedInterface) p.getNakedBaseType();
//					Collection<INakedClassifier> classifiers = inf.getImplementingClassifiers();
//					for (INakedClassifier iNakedClassifier : classifiers) {
//						if (!iNakedClassifier.getIsAbstract()) {
//							addChildField(testDataClass, iNakedClassifier);
//						}
//					}

				} else {
					if (!f.getNakedBaseType().getIsAbstract()) {
						addChildField(testDataClass, p.getNakedBaseType());
					}
				}
			}
		}
	}

	private void addChildField(OJAnnotatedClass testDataClass, INakedClassifier type) {
		OJPathName baseTypePath = getTestDataPath(type);
		OJAnnotatedField childController = new OJAnnotatedField();
		childController.setName(NameConverter.decapitalize(baseTypePath.getLast()));
		childController.setType(baseTypePath);
		if (config.isSeamAnnotations()) {
			OJAnnotationValue in = new OJAnnotationValue(new OJPathName("org.jboss.seam.annotations.In"));
			childController.putAnnotation(in);
		} else {
			OJAnnotationValue in = new OJAnnotationValue(new OJPathName("javax.inject.Inject"));
			childController.putAnnotation(in);
		}
		testDataClass.addToFields(childController);
	}

	@Override
	protected String getTestDataName(INakedClassifier child) {
		return OJUtil.classifierPathname(child).getLast() + "DataGenerator";
	}

	protected String calcMapKey(INakedEntity c, INakedProperty f) {
		INakedClassifier classifier = (INakedClassifier) f.getOwner();
		String result = c.getMappingInfo().getJavaName().getDecapped() + "." + f.getName() + "_";
		// TODO endToComposite needs to work for interfaces
		if (classifier instanceof INakedEntity && ((INakedEntity) classifier).getEndToComposite() != null) {
			NakedStructuralFeatureMap other = new NakedStructuralFeatureMap(((INakedEntity) classifier).getEndToComposite());
			result = c.getMappingInfo().getJavaName().getDecapped() + "." + other.getter() + "().getName() + " + "\"" + "." + result + "\"";
		} else {
			result = "\"" + result + "\"";
		}
		return result;
	}
}
