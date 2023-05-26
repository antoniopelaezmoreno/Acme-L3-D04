
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.session.Session;
import acme.testing.TestHarness;

public class AssistantSessionPublishTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final int recordIndex) {
		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialIndex);
		super.clickOnButton("Sessions");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialIndex);
		super.clickOnButton("Sessions");

		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("published", "true");

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it
		// HINT+ doesn't involve any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<Session> sessions;

		String query;

		sessions = this.repository.findManySessionsByTutorialCode("A1001");

		for (final Session s : sessions) {
			query = String.format("id=%d", s.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/session/publish", query);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/session/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/assistant/session/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/session/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/session/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/assistant/session/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/session/publish", query);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {

		Collection<Session> sessions;

		String query;

		sessions = this.repository.findManySessionsByTutorialCode("A1001");

		super.signIn("assistant1", "assistant1");
		for (final Session s : sessions)
			if (s.isPublished()) {
				query = String.format("id=%d", s.getId());
				super.request("/assistant/session/publish", query);
				super.checkPanicExists();
			}
		super.signOut();
	}
}
