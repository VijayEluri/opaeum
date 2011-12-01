package net.sf.nakeduml.javageneration;

import net.sf.nakeduml.feature.ISourceFolderIdentifier;

public enum JavaSourceFolderIdentifier implements ISourceFolderIdentifier{
	DOMAIN_TEST_SRC,
	DOMAIN_SRC,
	INTEGRATED_ADAPTOR_TEST_SRC,
	INTEGRATED_ADAPTOR_SRC,
	ADAPTOR_TEST_SRC,
	DOMAIN_GEN_TEST_SRC,
	DOMAIN_GEN_SRC,
	WEBAPP_GEN_TEST_SRC,
	ADAPTOR_GEN_SRC,
	INTEGRATED_ADAPTOR_GEN_SRC,
	ADAPTOR_GEN_TEST_SRC,
	INTEGRATED_ADAPTOR_GEN_TEST_SRC;
}