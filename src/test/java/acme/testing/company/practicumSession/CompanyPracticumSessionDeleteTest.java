
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicumSession.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionDeleteTest extends TestHarness {

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final int recordIndex, final String title, final String sessionAbstract, final String periodStart, final String periodEnd, final String link) {
		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("sessionAbstract", sessionAbstract);
		super.checkInputBoxHasValue("periodStart", periodStart);
		super.checkInputBoxHasValue("periodEnd", periodEnd);
		super.checkInputBoxHasValue("link", link);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();
		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumIndex, final int recordIndex, final String title, final String sessionAbstract, final String periodStart, final String periodEnd, final String link) {
		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("sessionAbstract", sessionAbstract);
		super.checkInputBoxHasValue("periodStart", periodStart);
		super.checkInputBoxHasValue("periodEnd", periodEnd);
		super.checkInputBoxHasValue("link", link);
		super.checkNotButtonExists("Delete");
		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<PracticumSession> sessions;
		String id;
		sessions = this.repository.findPracticumSessionsByUsername("company1");
		for (final PracticumSession ps : sessions) {
			id = String.format("id=%d", ps.getId());
			super.checkLinkExists("Sign in");
			super.request("/company/practicum-session/delete", id);
			super.checkPanicExists();
			super.signIn("student1", "student1");
			super.request("/company/practicum-session/delete", id);
			super.checkPanicExists();
			super.signOut();
			super.signIn("company2", "company2");
			super.request("/company/practicum-session/delete", id);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
