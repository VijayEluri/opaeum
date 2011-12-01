package org.nakeduml.environment.cdi.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import org.jboss.weld.mock.MockServletLifecycle;
import org.junit.Before;
import org.nakeduml.runtime.domain.IntrospectionUtil;
@Deprecated

public abstract class AbstractCdiTest{
	protected MockServletLifecycle lifecycle;
	@Before
	public void beforeClass() throws Throwable{
		try{
			CdiTestEnvironment.getInstance().initializeDeployment(getBeansXmlResources(), getAdditionalWebBeans());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	protected Collection<String> getBeansXmlResources(){
		return Collections.EMPTY_LIST;
	}
	protected List<Class<?>> getClasses(String packageName){
		return IntrospectionUtil.getClasses(packageName);
	}
	/**
	 * Override in your tests to register specific beans with the manager.
	 * 
	 * @return
	 */
	protected List<Class<? extends Object>> getAdditionalWebBeans(){
		return Collections.EMPTY_LIST;
	}
	
}