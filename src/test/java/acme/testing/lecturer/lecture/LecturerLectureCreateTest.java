
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerLectureCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int recordIndex, final String title, final String lectureAbstract, final String estimatedTime, final String body, final String indicator, final String link, final String published) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
		super.fillInputBoxIn("estimatedTime", estimatedTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("indicator", indicator);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, lectureAbstract);
		super.checkColumnHasValue(recordIndex, 2, indicator);
		super.checkColumnHasValue(recordIndex, 3, published);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("lectureAbstract", lectureAbstract);
		super.checkInputBoxHasValue("estimatedTime", estimatedTime);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("indicator", indicator);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("published", published);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int courseIndex, final String title, final String lectureAbstract, final String estimatedTime, final String body, final String indicator, final String link) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");

		super.clickOnButton("Create");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
		super.fillInputBoxIn("estimatedTime", estimatedTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("indicator", indicator);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();

		super.signIn("student1", "student1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();
	}

	@Test
	public void test301Hacking() {

		Collection<Course> courses;
		String query;

		super.checkLinkExists("Sign in");
		super.signIn("lecturer1", "lecturer1");
		courses = this.repository.findManyCoursesByUserName("lecturer1");
		for (final Course c : courses)
			if (c.isPublished()) {
				query = String.format("masterId=%d", c.getId());
				super.request("/lecturer/lecture/create", query);
				super.checkPanicExists();
			}
	}

	@Test
	public void test302Hacking() {

		Collection<Course> courses;
		String query;

		super.checkLinkExists("Sign in");
		super.signIn("lecturer1", "lecturer1");
		courses = this.repository.findManyCoursesByUserName("lecturer2");
		for (final Course c : courses) {
			query = String.format("masterId=%d", c.getId());
			super.request("/lecturer/lecture/create", query);
			super.checkPanicExists();
		}
	}
}
