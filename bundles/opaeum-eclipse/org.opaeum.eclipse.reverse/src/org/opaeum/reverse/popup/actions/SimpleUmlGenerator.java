package org.opaeum.reverse.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.opaeum.linkage.MappedTypeLoader;

public class SimpleUmlGenerator extends AbstractUmlGenerator{
	@Override
	public Collection<Element> generateUml(Map<ITypeBinding,AbstractTypeDeclaration> selection,Package library,IProgressMonitor m) throws Exception{
		m.beginTask("Importing Java classes", selection.size());
		if(library instanceof Model){
			IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(library.eResource().getURI().toPlatformString(true)));
			String absolutePath = ifile.getLocation().toFile().getAbsolutePath();
			File mappedTypesFile = new File(absolutePath.substring(0, absolutePath.length() - 3) + MappedTypeLoader.MAPPINGS_EXTENSION);
			factory = new ClassifierFactory(library);
			if(mappedTypesFile.exists()){
				factory.getMappedTypes().load(new FileInputStream(mappedTypesFile));
			}
			for(Entry<ITypeBinding,AbstractTypeDeclaration> t:selection.entrySet()){
				m.worked(1);
				if(!t.getKey(). isAnnotation()){
					// Only do annotations in profiles
					Classifier cls = factory.getClassifierFor(t.getKey());
					factory.getMappedTypes().put(cls.getQualifiedName(), t.getKey().getQualifiedName());
					populateAttributes(library, cls, t);
					populateOperations(library, cls, t);
				}
			}
			library.eResource().save(null);
			factory.getMappedTypes().store(new FileOutputStream(mappedTypesFile), "Generated by Opaeum");
		}
		m.done();
		return Collections.emptySet();

	}
	
}