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

package acme.testing.authenticated.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuthenticatedTutorialListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/tutorial/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String estimatedTime) {

		super.signIn("administrator", "administrator");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, estimatedTime);
		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list all of the jobs using 
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/authenticated/tutorial/list");
		super.checkPanicExists();
	}

}
