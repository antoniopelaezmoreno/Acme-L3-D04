
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialPublishTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String published) {
		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("published", published);

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
			super.request("/assistant/tutorial/publish", query);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/tutorial/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/assistant/tutorial/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/tutorial/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/assistant/tutorial/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/tutorial/publish", query);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {

		Collection<Tutorial> tutorials;

		String query;

		tutorials = this.repository.findManyTutorialsByUserName("assistant1");

		super.signIn("assistant1", "assistant1");
		for (final Tutorial t : tutorials)
			if (t.isPublished()) {
				query = String.format("id=%d", t.getId());
				super.request("/assistant/tutorial/publish", query);
				super.checkPanicExists();
			}
		super.signOut();
	}
}
