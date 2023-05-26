
package acme.testing.auditor.auditingRecords;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecords.AuditingRecords;
import acme.testing.TestHarness;

public class AuditorAuditingRecordPublishTest extends TestHarness {

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-records/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100positive(final int auditIndex, final int recordIndex, final String subject) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditIndex);
		super.clickOnButton("Show auditing records");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, subject);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();
		super.signOut();
	}

	@Test
	public void test300hacking() {
		Collection<AuditingRecords> ars;
		String params;

		ars = this.repository.findAuditingRecordsByAuditor("auditor1");
		for (final AuditingRecords a : ars)
			if (a.isPublished()) {
				params = String.format("id=%d", a.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/auditingRecords/publish", params);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/auditor/auditingRecords/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/auditor/auditingRecords/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
