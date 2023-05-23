
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int recordIndex, final String title, final String lectureAbstract, final String indicator, final String published) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.clickOnButton("Lectures");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, lectureAbstract);
		super.checkColumnHasValue(recordIndex, 2, indicator);
		super.checkColumnHasValue(recordIndex, 3, published);
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<Lecture> lectures;

		String query;

		lectures = this.repository.findManyLecturesByCourseCode("AB193");

		for (final Lecture l : lectures) {

			query = String.format("masterId=%d", l.getId());
			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture/list-mine", query);
			super.checkPanicExists();

			super.checkLinkExists("Sign in");
			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/lecture/list-mine", query);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("student1", "student1");
			super.request("/lecturer/lecture/list-mine", query);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("assistant1", "assistant1");
			super.request("/lecturer/lecture/list-mine", query);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("auditor1", "auditor1");
			super.request("/lecturer/lecture/list-mine", query);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("company1", "company1");
			super.request("/lecturer/course/list-mine", query);
			super.checkPanicExists();
			super.signOut();
		}

	}
}
