
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionCreateTest extends TestHarness {

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final int recordIndex, final String title, final String sessionAbstract, final String periodStart, final String periodEnd, final String link) {
		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");
		if (practicumIndex == 0)
			super.clickOnButton("Create addendum");
		else
			super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("sessionAbstract", sessionAbstract);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("link", link);
		if (practicumIndex == 0)
			super.fillInputBoxIn("confirmation", "true");
		super.clickOnSubmit("Create");
		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, periodStart);
		super.checkColumnHasValue(recordIndex, 2, periodEnd);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("sessionAbstract", sessionAbstract);
		super.checkInputBoxHasValue("periodStart", periodStart);
		super.checkInputBoxHasValue("periodEnd", periodEnd);
		super.checkInputBoxHasValue("link", link);
		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumIndex, final int recordIndex, final String title, final String sessionAbstract, final String periodStart, final String periodEnd, final String link) {
		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");
		if (practicumIndex == 0)
			super.clickOnButton("Create addendum");
		else
			super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("sessionAbstract", sessionAbstract);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("link", link);
		if (practicumIndex == 0)
			super.fillInputBoxIn("confirmation", "true");
		super.clickOnSubmit("Create");
		super.checkErrorsExist();
		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<Practicum> practicums;
		String id;
		practicums = this.repository.findPracticumsByUsername("company1");
		for (final Practicum p : practicums) {
			id = String.format("masterId=%d", p.getId());
			super.checkLinkExists("Sign in");
			super.request("/company/practicum-session/create", id);
			super.checkPanicExists();
			super.signIn("student1", "student1");
			super.request("/company/practicum-session/create", id);
			super.checkPanicExists();
			super.signOut();
			super.signIn("lecturer1", "lecturer1");
			super.request("/company/practicum-session/create", id);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
