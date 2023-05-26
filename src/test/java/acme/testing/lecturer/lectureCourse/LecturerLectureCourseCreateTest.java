
package acme.testing.lecturer.lectureCourse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureCourseCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture-course/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int recordIndex, final String course, final String lecture) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "Add Lecture To Course");
		super.checkFormExists();

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("lecture", lecture);
		super.clickOnSubmit("Add");

		super.clickOnMenu("Lecturer", "List my courses");
		super.sortListing(0, "asc");
		super.checkColumnHasValue(courseIndex, 1, course);

		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();

		super.clickOnButton("Lectures");
		super.checkListingExists();
		super.checkColumnHasValue(recordIndex, 0, lecture);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture-course/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final String course, final String lecture) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "Add Lecture To Course");
		super.checkFormExists();

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("lecture", lecture);
		super.clickOnSubmit("Add");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture-course/create");
		super.checkPanicExists();

		super.signIn("student1", "student1");
		super.request("/lecturer/lecture-course/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/lecturer/lecture-course/create");
		super.checkPanicExists();
		super.signOut();
	}
}
