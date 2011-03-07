package net.sf.nakeduml.jbpm.test;

import jbpm.jbpm.TheBoss;

import org.junit.Assert;
import org.testng.annotations.Test;

public class TheBossOclTest {
	@Test
	public void test() {
		TheBoss boss = new TheBoss();
		boss.setName1("name1");
		boss.setName2("name2");
		Assert.assertEquals("name3name1name2", boss.testConcatNames("name3"));
	}

}