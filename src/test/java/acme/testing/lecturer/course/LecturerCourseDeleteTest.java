
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerCourseDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String courseAbstract, final String indicator, final String retailPrice, final String link, final String published) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("courseAbstract", courseAbstract);
		super.checkInputBoxHasValue("indicator", indicator);
		super.checkInputBoxHasValue("retailPrice", retailPrice);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("published", published);

		super.clickOnSubmit("Delete");

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Course> courses;

		String query;

		courses = this.repository.findManyCoursesByUserName("lecturer1");

		for (final Course c : courses)
			if (c.isPublished()) {
				super.signIn("lecturer1", "lecturer1");
				query = String.format("id=%d", c.getId());
				super.request("/lecturer/course/delete", query);
				super.checkPanicExists();
			}
	}

	@Test
	public void test301Hacking() {

		Collection<Course> courses;

		String query;

		courses = this.repository.findManyCoursesByUserName("lecturer1");

		for (final Course c : courses) {
			query = String.format("id=%d", c.getId());
			super.request("/lecturer/course/delete", query);
			super.checkPanicExists();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/course/delete", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/lecturer/course/delete", query);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
