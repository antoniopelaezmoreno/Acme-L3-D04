
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int recordIndex, final String title, final String lectureAbstract, final String estimatedTime, final String body, final String indicator, final String link, final String published) {
		// HINT: this test authenticates as an employer, lists his or her applications,
		// HINT+ changes their status and checks that it's been updated.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
		super.fillInputBoxIn("estimatedTime", estimatedTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("indicator", indicator);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

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
	@CsvFileSource(resources = "/lecturer/lecture/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int courseIndex, final int recordIndex, final String title, final String lectureAbstract, final String estimatedTime, final String body, final String indicator, final String link) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
		super.fillInputBoxIn("estimatedTime", estimatedTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("indicator", indicator);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<Lecture> lectures;

		String query;

		lectures = this.repository.findManyLecturesByCourseCode("AB001");

		for (final Lecture l : lectures) {
			query = String.format("id=%d", l.getId());
			super.request("/lecturer/lecture/update", query);
			super.checkPanicExists();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/lecture/update", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/lecturer/lecture/update", query);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {
		Collection<Lecture> lectures;

		String query;

		lectures = this.repository.findManyLecturesByCourseCode("AB001");

		for (final Lecture l : lectures) {
			query = String.format("id=%d", l.getId());
			super.signIn("lecturer1", "lecturer1");
			super.request("/lecturer/course/update", query);
			super.checkPanicExists();
			super.signOut();
		}

	}
}
