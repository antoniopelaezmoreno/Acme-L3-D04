
package acme.testing.any.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive100.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String message, final String link, final String email, final String nick) {

		super.signIn("student1", "student1");
		super.clickOnMenu("Authenticated", "List Peeps");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("nick", nick);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Authenticated", "List Peeps");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, nick);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("nick", nick);

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive101.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test101Positive(final int recordIndex, final String title, final String message, final String link, final String email, final String nick) {

		super.clickOnMenu("Anonymous", "List Peeps");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("nick", nick);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Anonymous", "List Peeps");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, nick);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("nick", nick);

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String nick, final String message, final String email, final String link) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Authenticated", "List Peeps");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("nick", nick);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

}
