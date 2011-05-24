package org.nakeduml.name;
public class NameConverterTest{ 
	public void testIt() throws Exception {
		assertEquals("abc_dfg", NameConverter.toUnderscoreStyle("abcDfg"));
		assertEquals("abc_dfg", NameConverter.toUnderscoreStyle("ABCDfg"));
		assertEquals("abc_dfg", NameConverter.toUnderscoreStyle("abcDFG"));
		assertEquals("abc_dfg", NameConverter.toUnderscoreStyle("AbcDFG"));
		assertEquals("abcDfg", NameConverter.underscoredToCamelCase("abc_dfg"));
		assertEquals("abcDfg", NameConverter.underscoredToCamelCase("abc_dfg_"));
		assertEquals("abcDfg", NameConverter.underscoredToCamelCase("_abc_dfg"));
		assertEquals("abcDfg", NameConverter.underscoredToCamelCase("____abc___dfg"));
		assertEquals("abcDfg", NameConverter.underscoredToCamelCase("AbcDfg"));
		assertEquals("Abc dfg", NameConverter.separateWords("AbcDfg"));
		assertEquals("Abc dfg", NameConverter.separateWords("Abc_dfg"));
	}
	private void assertEquals(String string, String underscoreStyle) {
		assert string.equals(underscoreStyle);
	}
}