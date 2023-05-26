
package acme.testing.any.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepShowTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String instantiationMoment, final String message, final String link, final String email, final String nick) {
		super.signIn("company1", "company1");
		super.clickOnMenu("Authenticated", "List Peeps");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("instantiationMoment", instantiationMoment);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("nick", nick);
		super.signOut();
	}
}
