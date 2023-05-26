
package acme.testing.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumDeleteTest extends TestHarness {

	@Autowired
	protected CompanyPracticumTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String practicumAbstract, final String goals, final String course) {
		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("practicumAbstract", practicumAbstract);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("course", course);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();
		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String practicumAbstract, final String goals, final String course) {
		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("practicumAbstract", practicumAbstract);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("course", course);
		super.checkNotButtonExists("Delete");
		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<Practicum> practicums;
		String id;
		practicums = this.repository.findPracticumsByUsername("company1");
		for (final Practicum p : practicums) {
			id = String.format("id=%d", p.getId());
			super.checkLinkExists("Sign in");
			super.request("/company/practicum/delete", id);
			super.checkPanicExists();
			super.signIn("student1", "student1");
			super.request("/company/practicum/delete", id);
			super.checkPanicExists();
			super.signOut();
			super.signIn("company2", "company2");
			super.request("/company/practicum/delete", id);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
