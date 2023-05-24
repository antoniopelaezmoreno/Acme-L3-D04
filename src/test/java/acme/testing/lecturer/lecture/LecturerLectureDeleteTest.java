
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int recordIndex, final String title, final String lectureAbstract, final String estimatedTime, final String body, final String indicator, final String link, final String published) {
		// HINT: this test signs in as an employer, then lists the announcements,
		// HINT+ and checks that the listing shows the expected data.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("lectureAbstract", lectureAbstract);
		super.checkInputBoxHasValue("estimatedTime", estimatedTime);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("indicator", indicator);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("published", published);

		super.clickOnSubmit("Delete");

		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Lecture> lectures;

		String query;

		lectures = this.repository.findManyLecturesByUserName("lecturer1");

		super.signIn("lecturer1", "lecturer1");
		for (final Lecture l : lectures)
			if (l.isPublished()) {
				query = String.format("id=%d", l.getId());
				super.request("/lecturer/lecture/delete", query);
				super.checkPanicExists();
			}
	}

	@Test
	public void test301Hacking() {

		Collection<Lecture> lectures;

		String query;

		lectures = this.repository.findManyLecturesByUserName("lecturer1");

		for (final Lecture l : lectures) {
			query = String.format("id=%d", l.getId());
			super.request("/lecturer/lecture/delete", query);
			super.checkPanicExists();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/lecture/delete", query);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/lecturer/course/delete", query);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
