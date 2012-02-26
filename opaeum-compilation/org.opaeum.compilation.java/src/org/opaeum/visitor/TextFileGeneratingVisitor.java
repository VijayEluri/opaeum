package org.opaeum.visitor;

import java.util.List;
import java.util.Set;

import org.opaeum.feature.OpaeumConfig;
import org.opaeum.metamodel.workspace.INakedModelWorkspace;
import org.opaeum.textmetamodel.ISourceFolderIdentifier;
import org.opaeum.textmetamodel.SourceFolder;
import org.opaeum.textmetamodel.SourceFolderDefinition;
import org.opaeum.textmetamodel.TextFile;
import org.opaeum.textmetamodel.TextOutputNode;
import org.opaeum.textmetamodel.TextProject;
import org.opaeum.textmetamodel.TextWorkspace;

public abstract class TextFileGeneratingVisitor extends NakedElementOwnerVisitor{
	protected TextWorkspace textWorkspace;
	protected Set<TextOutputNode> textFiles;
	protected OpaeumConfig config;
	protected INakedModelWorkspace workspace;
	protected String getProjectName(SourceFolderDefinition outputRoot){
		String projectPrefix = null;
		switch(outputRoot.getProjectNameStrategy()){
		case QUALIFIED_WORKSPACE_NAME_AND_SUFFIX:
			projectPrefix = config.getMavenGroupId() + "." + workspace.getIdentifier();
			break;
		case SUFFIX_ONLY:
			projectPrefix = "";
			break;
		case MODEL_NAME_AND_SUFFIX:
			projectPrefix = getCurrentRootObject().getIdentifier();
			break;
		case WORKSPACE_NAME_AND_SUFFIX:
			projectPrefix = workspace.getIdentifier();
			break;
		case QUALIFIED_WORKSPACE_NAME_AND_SUFFIX_PREFIX_MODEL_NAME_TO_SOURCE_FOLDER:
			projectPrefix = config.getMavenGroupId() + "." + workspace.getIdentifier();
			break;
		case WORKSPACE_NAME_AND_SUFFIX_PREFIX_MODEL_NAME_TO_SOURCE_FOLDER:
			projectPrefix = workspace.getIdentifier();
			break;
		}
		return projectPrefix + outputRoot.getProjectSuffix();
	}
	public Set<TextOutputNode> getTextFiles(){
		return textFiles;
	}
	protected TextFile createTextPath(ISourceFolderIdentifier id,List<String> names){
		SourceFolderDefinition outputRoot = config.getSourceFolderDefinition(id);
		SourceFolder or = getSourceFolder(outputRoot);
		TextFile file = or.findOrCreateTextFile(names, outputRoot.overwriteFiles());
		this.textFiles.add(file);
		return file;
	}
	protected synchronized SourceFolder getSourceFolder(SourceFolderDefinition outputRoot){
		TextProject textProject = textWorkspace.findOrCreateTextProject(getProjectName(outputRoot));
		String sourceFolder = outputRoot.getSourceFolder();
		if(outputRoot.prefixModelIdentifierToSourceFolder()){
			// force multiple source folders per model
			sourceFolder = getCurrentRootObject().getIdentifier() + "/" + outputRoot.getSourceFolder();
		}
		SourceFolder or = textProject.findOrCreateSourceFolder(sourceFolder, outputRoot.cleanDirectories());
		return or;
	}
	public void release(){
		super.release();
		textFiles = null;
		textWorkspace = null;
		workspace = null;
	}
}
