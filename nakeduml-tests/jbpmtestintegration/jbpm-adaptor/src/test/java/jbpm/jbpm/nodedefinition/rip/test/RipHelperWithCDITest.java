package jbpm.jbpm.nodedefinition.rip.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jbpm.jbpm.Application;
import jbpm.jbpm.nodedefinition.EISConnection;
import jbpm.jbpm.nodedefinition.NodeDefinitionFactory;
import jbpm.jbpm.nodedefinition.interaction.EISInteractionSpec;
import jbpm.jbpm.nodedefinition.mock.MockNodeDefinitionFactory;
import jbpm.jbpm.nodedefinition.pool.EisPool;
import jbpm.jbpm.nodedefinition.pool.jmx.EisPoolService;
import jbpm.jbpm.rip.MMLCommand;
import jbpm.jbpm.rip.Network;
import jbpm.jbpm.rip.NetworkSoftwareVersion;
import jbpm.jbpm.rip.NodeDefinition;
import jbpm.jbpm.rip.RipHelper;
import net.wimpi.telnetd.BootException;

import org.jboss.seam.scheduling.util.WebBeansManagerUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nakeduml.test.adaptor.AbstractCDITest;
import org.nakeduml.test.adaptor.MockDependentSession;

public class RipHelperWithCDITest extends AbstractCDITest {

	@BeforeClass
	public static void startSshServer() throws IOException, BootException {
		SshTelnetUtil.startSshServer();
	}

	@AfterClass
	public static void stopSshServer() throws InterruptedException {
		SshTelnetUtil.stopSshServer();
	}
	
	@Override
	protected List<Class<? extends Object>> getAdditionalWebBeans() {
		Set<Class<? extends Object>> list = new HashSet<Class<? extends Object>>(1);
		list.addAll(getClasses(MockNodeDefinitionFactory.class.getPackage().getName()));
		list.addAll(getClasses(MockDependentSession.class.getPackage().getName()));
		list.add(RipHelperWithCDITest.class);
		list.addAll(getClasses(EISConnection.class.getPackage().getName()));
		list.addAll(getClasses(EISInteractionSpec.class.getPackage().getName()));
		list.addAll(getClasses(EisPool.class.getPackage().getName()));
		list.addAll(getClasses(EisPoolService.class.getPackage().getName()));
		list.remove(NodeDefinitionFactory.class);
		return new ArrayList<Class<? extends Object>>(list);		
	}

	protected Collection<URL> getBeansXmlFiles() {
		URL u = Thread.currentThread().getContextClassLoader().getResource("test-beans.xml");
		return Arrays.asList(u);
	}

	@Test
	public void testRipping() {
		RipHelper ripHelper = WebBeansManagerUtils.getInstanceByType(manager, RipHelper.class);
		Assert.assertNotNull(ripHelper);
		
		Application application = RipHelperTest.createTestData();
		Set<MMLCommand> mmlCommands = new HashSet<MMLCommand>();
		for (Network network : application.getNetwork()) {
			mmlCommands.addAll(network.getMMLCommand());
			for (NetworkSoftwareVersion networkSoftwareVersion : network.getNetworkSoftwareVersion()) {
				for (NodeDefinition nodeDefinition : networkSoftwareVersion.getNodeDefinition()) {
					ripHelper.rip(nodeDefinition, mmlCommands);
				}
			}
			mmlCommands.clear();
		}

		for (Network network : application.getNetwork()) {
			for (NetworkSoftwareVersion networkSoftwareVersion : network.getNetworkSoftwareVersion()) {
				for (NodeDefinition nodedDefinition : networkSoftwareVersion.getNodeDefinition()) {
					Assert.assertNotNull(nodedDefinition.getLastSuccesfulRip());
					Assert.assertNull(nodedDefinition.getLastUnsuccesfulRip());
				}
			}
		}
	}

}
