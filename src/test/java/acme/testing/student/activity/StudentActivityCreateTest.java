
package acme.testing.student.activity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class StudentActivityCreateTest extends TestHarness {

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String activityAbstract, final String indicator, final String periodStart, final String periodEnd, final String link, final String enrolment) {
		// HINT: this test authenticates as an student, list his or her activities and create new activities with correct data.

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "My activities");
		super.checkListingExists();

		super.clickOnButton("Register activity");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("activityAbstract", activityAbstract);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("enrolment", enrolment);
		super.clickOnSubmit("Register activity");

		super.clickOnMenu("Student", "My activities");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, indicator);
		super.checkColumnHasValue(recordIndex, 2, enrolment);

		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("activityAbstract", activityAbstract);
		super.checkInputBoxHasValue("periodStart", periodStart);
		super.checkInputBoxHasValue("periodEnd", periodEnd);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("enrolment", enrolment);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String activityAbstract, final String indicator, final String periodStart, final String periodEnd, final String link, final String enrolment) {

		// HINT: this test attempts to create activities using wrong data.

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "My activities");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnButton("Register activity");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("activityAbstract", activityAbstract);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("enrolment", enrolment);
		super.clickOnSubmit("Register activity");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create an activity for an enrolment as a principal without the "Student" role.

		super.checkLinkExists("Sign in");
		super.request("/student/activity/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

	}

}