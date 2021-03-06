package org.opaeum.test.structure;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.opaeum.test.Aunt;
import org.opaeum.test.Brother;
import org.opaeum.test.Family;
import org.opaeum.test.FamilyMemberHasRelation;
import org.opaeum.test.FamilyStepChild;
import org.opaeum.test.Father;
import org.opaeum.test.Marriage;
import org.opaeum.test.Mother;
import org.opaeum.test.StepBrother;
public class AssociationClassTest {

	@Test
	public void testManyToManySideOne() {
		Aunt aunt = new Aunt();
		Family family = new Family();
		Brother brother = new Brother(family, "John");
		try {
			aunt.addToFamilyMember(family, "John", brother);
			Assert.fail("IllegalStateException was not thrown");
		} catch (IllegalStateException e) {
			Assert.assertTrue("IllegalStateException was thrown", true);
		}
		aunt.setFirstName("Clotilda");
		aunt.setSurname("McGilliguddy");
		aunt.setDateOfBirth(new Date());
		aunt.addToFamilyMember(family, "John", brother);
		assertManyToManyConditions(aunt, brother);
	}

	@Test
	public void testManyToManySideTwo() {
		Aunt aunt = new Aunt();
		Family family = new Family();
		Brother brother = new Brother(family, "John");
		brother.addToRelation("Clotilda", "McGilliguddy", new Date(), aunt);
		assertManyToManyConditions(aunt, brother);
	}

	protected void assertManyToManyConditions(Aunt aunt, Brother brother) {
		Assert.assertEquals(1, aunt.getFamilyMember().size());
		Assert.assertEquals(1, aunt.getFamilyMemberHasRelation_familyMember()
				.size());
		FamilyMemberHasRelation link1 = aunt
				.getFamilyMemberHasRelation_familyMemberFor(brother);
		Assert.assertNotNull(link1);
		Assert.assertEquals(brother, link1.getFamilyMember());
		FamilyMemberHasRelation link2 = brother
				.getFamilyMemberHasRelation_relationFor(aunt);
		Assert.assertSame(link1, link2);
		Assert.assertNotNull(link2);
		Assert.assertEquals(aunt, link2.getRelation());
		Assert.assertTrue(aunt.getFamilyMember().contains(brother));
		Assert.assertTrue(aunt.getFamilyMemberHasRelation_familyMember()
				.contains(link2));
		Assert.assertTrue(brother.getFamilyMemberHasRelation_relation()
				.contains(link2));
	}

	@Test
	public void testCompositeManyToOne() {
		Family family = new Family();
		StepBrother sb = new StepBrother();
		try {
			sb.setFamily(family);
			Assert.fail("IllegalStateException was not thrown");
		} catch (IllegalStateException e) {
			Assert.assertTrue("IllegalStateException was thrown", true);
		}
		sb.setName("Peter Peter pumpkin eater");
		sb.setFamily(family);
		FamilyStepChild link = assertStepBrotherAdded(family, sb);
		family.removeFromStepChild(sb.getName(), sb);
		assertStepBrotherRemoved(family, sb, link);
		Family family2 = new Family();
		sb.setFamily(family2);
		assertStepBrotherAddedToNewFamily(family, sb, family2, link);
	}

	@Test
	public void testCompositeOneToMany() {
		Family family = new Family();
		StepBrother sb = new StepBrother();
		family.addToStepChild("Peter Peter pumpkin eater", sb);
		sb.setFamily(family);
		FamilyStepChild link = assertStepBrotherAdded(family, sb);
		family.removeFromStepChild(sb.getName(), sb);
		assertStepBrotherRemoved(family, sb, link);
		Family family2 = new Family();
		family2.addToStepChild(sb.getName(), sb);

		assertStepBrotherAddedToNewFamily(family, sb, family2, link);
	}

	protected void assertStepBrotherAddedToNewFamily(Family family,
			StepBrother sb, Family family2, FamilyStepChild oldLink) {
		Assert.assertNotSame(sb.getFamily(), family);
		Assert.assertNotSame(sb.getFamilyStepChild_family(), oldLink);
		Assert.assertEquals(0, family.getFamilyStepChild_stepChild().size());
		Assert.assertEquals(0, family.getStepChild().size());
		Assert.assertNotSame(sb.getFamilyStepChild_family(), family);
	}

	protected void assertStepBrotherRemoved(Family family, StepBrother sb,
			FamilyStepChild link) {
		Assert.assertNull(sb.getFamilyStepChild_family());
		Assert.assertNull(link.getFamily());
		Assert.assertNull(link.getStepChild());
		Assert.assertEquals(0, family.getFamilyStepChild_stepChild().size());
		Assert.assertEquals(0, family.getStepChild().size());
		Assert.assertNull(sb.getFamily());
	}

	protected FamilyStepChild assertStepBrotherAdded(Family family,
			StepBrother sb) {
		FamilyStepChild link = sb.getFamilyStepChild_family();
		Assert.assertNotNull(link);
		Assert.assertSame(family, link.getFamily());
		Assert.assertSame(family, sb.getFamily());
		Assert.assertEquals(1, family.getFamilyStepChild_stepChild().size());
		Assert.assertEquals(1, family.getStepChild().size());
		return link;
	}

	@Test
	public void testOneToOne() {
		Mother mother = new Mother();
		Father father = new Father();
		mother.setSurnameProvider(father);
		Marriage marriage_spouse = father.getMarriage_spouse();
		assertOneToOneConditions(mother, father);
		Father father2 = new Father();
		mother.setSurnameProvider(father2);
		Assert.assertNull(marriage_spouse.getSpouse());
		Assert.assertNull(marriage_spouse.getSurnameProvider());
		Assert.assertNull(father.getSpouse());
		Assert.assertNull(father.getMarriage_spouse());
		assertOneToOneConditions(mother, father2);

	}

	protected void assertOneToOneConditions(Mother mother, Father father) {
		Assert.assertEquals(mother, father.getSpouse());
		Assert.assertEquals(father, mother.getSurnameProvider());
		Assert.assertNotNull(father.getMarriage_spouse());
		Assert.assertEquals(father, mother.getMarriage_surnameProvider()
				.getSurnameProvider());
		Assert.assertEquals(mother, father.getMarriage_spouse().getSpouse());
	}
}
