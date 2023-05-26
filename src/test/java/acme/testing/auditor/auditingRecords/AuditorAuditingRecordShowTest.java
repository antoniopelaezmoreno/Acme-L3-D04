
package acme.testing.auditor.auditingRecords;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecords.AuditingRecords;
import acme.testing.TestHarness;

public class AuditorAuditingRecordShowTest extends TestHarness {

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-records/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100positive(final int auditIndex, final int recordIndex, final String subject, final String assessment, final String periodStart, final String periodEnd, final String mark, final String published, final String link) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditIndex);
		super.clickOnButton("Show auditing records");
		super.checkListingExists();
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assessment", assessment);
		super.checkInputBoxHasValue("periodStart", periodStart);
		super.checkInputBoxHasValue("periodEnd", periodEnd);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("published", published);
		super.checkInputBoxHasValue("link", link);
		super.signOut();
	}

	@Test
	public void test300hacking() {
		final Collection<AuditingRecords> ars;
		String param;

		ars = this.repository.findAuditingRecordsByAuditor("auditor1");

		for (final AuditingRecords record : ars)
			if (record.isPublished()) {
				param = String.format("id=%d", record.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/auditingRecords/show", param);
				super.checkPanicExists();

				super.signIn("auditor2", "auditor2");
				super.request("/auditor/auditingRecords/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator1", "administrator1");
				super.request("/auditor/auditingRecords/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/auditingRecords/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
