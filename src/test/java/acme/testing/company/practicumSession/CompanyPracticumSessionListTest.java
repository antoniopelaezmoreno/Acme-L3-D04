
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicumSession.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionListTest extends TestHarness {

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final int recordIndex, final String title, final String periodStart, final String periodEnd) {
		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, periodStart);
		super.checkColumnHasValue(recordIndex, 2, periodEnd);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<PracticumSession> sessions;
		String id;

		sessions = this.repository.findPracticumSessionsByUsername("company1");

		for (final PracticumSession ps : sessions) {
			id = String.format("masterId=%d", ps.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/practicum-session/list", id);
			super.checkPanicExists();

			super.checkLinkExists("Sign in");
			super.signIn("administrator1", "administrator1");
			super.request("/company/practicum-session/list", id);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("student1", "student1");
			super.request("/company/practicum-session/list", id);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("assistant1", "assistant1");
			super.request("/company/practicum-session/list", id);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("auditor1", "auditor1");
			super.request("/company/practicum-session/list", id);
			super.checkPanicExists();
			super.signOut();
		}

	}
}
