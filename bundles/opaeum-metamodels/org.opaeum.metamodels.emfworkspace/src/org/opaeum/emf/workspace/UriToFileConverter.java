package org.opaeum.emf.workspace;

import java.io.File;

import org.eclipse.emf.common.util.URI;

public interface UriToFileConverter{
	File resolveUri(URI uri);
}
