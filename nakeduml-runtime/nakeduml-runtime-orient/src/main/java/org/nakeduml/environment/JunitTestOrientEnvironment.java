package org.nakeduml.environment;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;

import org.apache.commons.io.FileUtils;
import org.nakeduml.nakeduml.tinker.runtime.NakedOrientGraph;
import org.util.NakedGraph;
import org.util.TransactionThreadEntityVar;

import com.tinkerpop.blueprints.pgm.TransactionalGraph.Mode;
import com.tinkerpop.blueprints.pgm.impls.orientdb.OrientGraph;

public class JunitTestOrientEnvironment extends Environment {

	@Override
	public <T> Class<T> getImplementationClass(T o) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getComponent(Class<T> clazz) {
		if (clazz == NakedGraph.class) {
			File dir = new File(this.properties.getProperty("tinkerdb"));
			if (dir.exists()) {
				try {
					FileUtils.deleteDirectory(dir);
				} catch (IOException e) {
					throw new RuntimeException(e);
				} 
			}			
			OrientGraph db = new OrientGraph(this.properties.getProperty("tinkerdb"));
			db.setTransactionMode(Mode.valueOf(this.properties.getProperty("tinkerdb.transactionmode", "MANUAL")));
			TransactionThreadEntityVar.clear();
			NakedGraph nakedGraph = new NakedOrientGraph(db, new Boolean(this.properties.getProperty("tinkerdb.withschema", "false")));
			nakedGraph.registerListeners();
			return (T)nakedGraph;
		}
		return null;
	}

	@Override
	public <T> T getComponent(Class<T> clazz, Annotation qualifiers) {
		return null;
	}

	@Override
	public void reset() {
	}

}