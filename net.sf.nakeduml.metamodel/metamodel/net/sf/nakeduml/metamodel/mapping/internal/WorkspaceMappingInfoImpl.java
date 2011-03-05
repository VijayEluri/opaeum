package net.sf.nakeduml.metamodel.mapping.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import net.sf.nakeduml.feature.SortedProperties;
import net.sf.nakeduml.metamodel.mapping.IMappingInfo;
import net.sf.nakeduml.metamodel.mapping.IWorkspaceMappingInfo;

public class WorkspaceMappingInfoImpl implements IWorkspaceMappingInfo {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.000000");
	private Properties properties;
	private Map<String, IMappingInfo> mappingInfoMap = new HashMap<String, IMappingInfo>();
	private File file;
	private int currentRevision;
	private float currentVersion;
	private int nakedUmlIdMaxValue;

	private WorkspaceMappingInfoImpl() {
		this.properties = new SortedProperties();
	}

	public WorkspaceMappingInfoImpl(File file) {
		this();
		this.file = file;
		if (file.exists()) {
			try {
				load(new FileReader(file));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public WorkspaceMappingInfoImpl(Reader reader) {
		this();
		if (reader!=null) {
			load(reader);
		}
	}

	private void load(Reader reader) {
		try {
			if (reader != null) {
				final String CURRENT_VERSION = getClass().getName() + ".currentVersion";
				final String CURRENT_REVISION = getClass().getName() + ".currentRevision";
				final String NAKED_UML_MAX_VALUE = getClass().getName() + ".nakedUmlIdMaxValue";
				Set<String> knownProperties = new HashSet<String>();
				knownProperties.add(CURRENT_VERSION);
				knownProperties.add(CURRENT_REVISION);
				knownProperties.add(NAKED_UML_MAX_VALUE);
				properties.load(reader);
				currentVersion = DECIMAL_FORMAT.parse(properties.getProperty(CURRENT_VERSION)).floatValue();
				currentRevision = Integer.parseInt(properties.getProperty(CURRENT_REVISION));
				nakedUmlIdMaxValue = Integer.parseInt(properties.getProperty(NAKED_UML_MAX_VALUE));
				Set<Entry<Object, Object>> entrySet = properties.entrySet();
				for (Entry<Object, Object> entry : entrySet) {
					String id = (String) entry.getKey();
					if (!knownProperties.contains(id)) {
						mappingInfoMap.put(id, new MappingInfoImpl(id, (String) entry.getValue()));
					}
				}
			}
		} catch (RuntimeException r) {
			throw r;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public IMappingInfo getMappingInfo(String modelId, boolean generated) {
		IMappingInfo mappingInfo = (IMappingInfo) this.mappingInfoMap.get(modelId);
		if (mappingInfo == null) {
			this.nakedUmlIdMaxValue++;
			mappingInfo = new MappingInfoImpl(generated);
			mappingInfo.setIdInModel(modelId);
			mappingInfo.setNakedUmlId(new Integer(this.nakedUmlIdMaxValue));
			mappingInfo.setSinceRevision(currentRevision);
			mappingInfo.setSinceVersion(currentVersion);
			this.mappingInfoMap.put(mappingInfo.getIdInModel(), mappingInfo);
		}
		return mappingInfo;
	}

	@Override
	public void store() {
		Writer writer;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		store(writer);

	}

	public void store(Writer writer) {
		try {
			properties.put(getClass().getName() + ".currentVersion", DECIMAL_FORMAT.format(currentVersion));
			properties.put(getClass().getName() + ".nakedUmlIdMaxValue", "" + nakedUmlIdMaxValue);
			properties.put(getClass().getName() + ".currentRevision", "" + currentRevision);
			Set<Entry<String, IMappingInfo>> entrySet = mappingInfoMap.entrySet();
			for (Entry<String, IMappingInfo> entry : entrySet) {
				String id = (String) entry.getKey();
				if (entry.getValue().isGenerated()) {
					properties.put(id, entry.getValue().toString());
				}
			}
			properties.store(writer, "Generated by NakedUML");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void incrementRevision() {
		currentRevision++;

	}

	public int getCurrentRevision() {
		return currentRevision;
	}

	public void setCurrentRevision(int currentRevision) {
		this.currentRevision = currentRevision;
	}

	public float getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(float currentVersion) {
		this.currentVersion = currentVersion;
	}

	public int getNakedUmlIdMaxValue() {
		return this.nakedUmlIdMaxValue;
	}

}
