/*
 * EmployerJobListMineTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantSessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final int recordIndex, final String title, final String indication, final String periodStart, final String periodEnd, final String published) {
		// HINT: this test authenticates as an assistant, lists his or her tutorials only,
		// HINT+ and then checks that the listing has the expected data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialIndex);
		super.clickOnButton("Sessions");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, indication);
		super.checkColumnHasValue(recordIndex, 2, periodStart);
		super.checkColumnHasValue(recordIndex, 3, periodEnd);
		super.checkColumnHasValue(recordIndex, 4, published);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<Tutorial> tutorials;

		String query;

		tutorials = this.repository.findManyTutorialsByUserName("assistant1");

		for (final Tutorial t : tutorials) {

			query = String.format("masterId=%d", t.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/session/list", query);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/session/list", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/assistant/session/list", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/session/list", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/session/list", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/assistant/session/list", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/session/list", query);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
