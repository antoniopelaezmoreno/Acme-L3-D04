
package acme.testing.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String practicumAbstract, final String goals, final String course) {
		// HINT: this test authenticates as an employer, lists his or her applications,
		// HINT+ changes their status and checks that it's been updated.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("practicumAbstract", practicumAbstract);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.clickOnSubmit("Update");

		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);
		super.checkColumnHasValue(recordIndex, 1, title);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("practicumAbstract", practicumAbstract);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("course", course);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String practicumAbstract, final String goals, final String course) {
		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("practicumAbstract", practicumAbstract);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);

		super.clickOnSubmit("Update");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<Practicum> practicums;

		String id;

		practicums = this.repository.findPracticumsByUsername("company1");

		for (final Practicum p : practicums) {
			id = String.format("id=%d", p.getId());
			super.request("/company/practicum/update", id);
			super.checkPanicExists();

			super.signIn("student1", "student1");
			super.request("/company/practicum/update", id);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/company/practicum/update", id);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
