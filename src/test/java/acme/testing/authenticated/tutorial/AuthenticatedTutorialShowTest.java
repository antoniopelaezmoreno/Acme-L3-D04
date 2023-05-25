
package acme.testing.authenticated.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AuthenticatedTutorialShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedTutorialTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/tutorial/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String course, final String assistant, final String tutorialAbstract, final String goals, final String estimatedTime) {
		// HINT: this test signs in as an employer, then lists the announcements,
		// HINT+ and checks that the listing shows the expected data.

		super.signIn("administrator", "administrator");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.signOut();

		super.signIn("company1", "company1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.signOut();

		super.signIn("student1", "student1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Authenticated", "List Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("assistant", assistant);
		super.checkInputBoxHasValue("tutorialAbstract", tutorialAbstract);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTime", estimatedTime);
		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it
		// HINT+ doesn't involve any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<Tutorial> tutorials;
		String query;

		tutorials = this.repository.findManyTutorialsByUserName("assistant1");

		for (final Tutorial t : tutorials) {
			query = String.format("id=%d", t.getId());

			super.checkLinkExists("Sign in");
			super.request("/authenticated/tutorial/show", query);
			super.checkPanicExists();
		}
	}
}
