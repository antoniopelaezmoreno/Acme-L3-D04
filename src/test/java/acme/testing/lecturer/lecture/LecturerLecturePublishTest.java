
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerLecturePublishTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int recordIndex, final String title, final String published) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.clickOnListingRecord(recordIndex);
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");

		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("published", published);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int courseIndex, final int recordIndex) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");

		super.clickOnListingRecord(recordIndex);
		super.checkNotButtonExists("Publish");

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<Course> courses;

		String query;

		courses = this.repository.findManyCoursesByUserName("lecturer1");

		for (final Course c : courses) {
			query = String.format("id=%d", c.getId());
			super.request("/lecturer/course/publish", query);
			super.checkPanicExists();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/course/publish", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/lecturer/course/publish", query);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
