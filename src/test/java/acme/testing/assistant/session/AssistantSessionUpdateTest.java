
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.session.Session;
import acme.testing.TestHarness;

public class AssistantSessionUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final int recordIndex, final String title, final String tutorial, final String sessionAbstract, final String indication, final String periodStart, final String periodEnd, final String link,
		final String published) {
		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialIndex);
		super.clickOnButton("Sessions");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("sessionAbstract", sessionAbstract);
		super.fillInputBoxIn("indication", indication);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

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

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("tutorial", tutorial);
		super.checkInputBoxHasValue("sessionAbstract", sessionAbstract);
		super.checkInputBoxHasValue("indication", indication);
		super.checkInputBoxHasValue("periodStart", periodStart);
		super.checkInputBoxHasValue("periodEnd", periodEnd);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("published", published);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialIndex, final int recordIndex, final String title, final String sessionAbstract, final String indication, final String periodStart, final String periodEnd, final String link) {
		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialIndex);
		super.clickOnButton("Sessions");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("sessionAbstract", sessionAbstract);
		super.fillInputBoxIn("indication", indication);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<Session> sessions;

		String query;

		sessions = this.repository.findManySessionsByTutorialCode("A1001");

		for (final Session s : sessions) {
			query = String.format("id=%d", s.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/session/update", query);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/session/update", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/assistant/session/update", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/session/update", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/session/update", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/assistant/session/update", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/session/update", query);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
