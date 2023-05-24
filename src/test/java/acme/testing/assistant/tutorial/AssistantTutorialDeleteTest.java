
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;


	// Test methods -----------------------------------------------------------
	public void test100Positive() {
		// HINT: this test signs in as an employer, then lists the announcements,
		// HINT+ and checks that the listing shows the expected data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(1);

		super.clickOnSubmit("Delete");

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

		for (final Tutorial t : tutorials)
			if (t.isPublished()) {
				super.signIn("assistant1", "assistant1");
				query = String.format("id=%d", t.getId());
				super.request("/assistant/tutorial/delete", query);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {

		Collection<Tutorial> tutorials;

		String query;

		tutorials = this.repository.findManyTutorialsByUserName("assistant1");

		for (final Tutorial t : tutorials) {
			query = String.format("id=%d", t.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/tutorial/delete", query);
			super.checkPanicExists();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/tutorial/delete", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("administrator", "administrator");
			super.request("/assistant/tutorial/delete", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/assistant/tutorial/delete", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial/delete", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/tutorial/delete", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/assistant/tutorial/delete", query);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
