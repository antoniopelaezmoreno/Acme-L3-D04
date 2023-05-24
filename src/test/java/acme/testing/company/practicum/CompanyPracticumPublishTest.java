
package acme.testing.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumPublishTest extends TestHarness {

	@Autowired
	protected CompanyPracticumTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code) {
		// HINT: this test authenticates as an employer, lists his or her jobs,
		// HINT: then selects one of them, and publishes it.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, code);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code) {
		// HINT: this test attempts to publish a job that cannot be published, yet.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List Practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, code);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkAlertExists(false);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a job with a role other than "Employer".

		Collection<Practicum> practicums;
		String id;

		practicums = this.repository.findPracticumsByUsername("company1");
		for (final Practicum p : practicums)
			if (!p.isPublished()) {
				id = String.format("id=%d", p.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum/publish", id);
				super.checkPanicExists();

				super.signIn("company1", "company1");
				super.request("/company/practicum/publish", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum/publish", id);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published job that was registered by the principal.

		Collection<Practicum> practicums;
		String id;

		super.signIn("company1", "company1");
		practicums = this.repository.findPracticumsByUsername("company1");
		for (final Practicum p : practicums)
			if (p.isPublished()) {
				id = String.format("id=%d", p.getId());
				super.request("/company/practicum/publish", id);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a job that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		Collection<Practicum> practicums;
		String id;

		super.signIn("company2", "company2");
		practicums = this.repository.findPracticumsByUsername("company1");
		for (final Practicum p : practicums) {
			id = String.format("id=%d", p.getId());
			super.request("/company/practicum/publish", id);
		}
		super.signOut();
	}
}
