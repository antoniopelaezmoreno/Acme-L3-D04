
package acme.testing.auditor.auditingRecords;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecords.AuditingRecords;
import acme.testing.TestHarness;

public class AuditorAuditingRecordListTest extends TestHarness {

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-records/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100positive(final int auditIndex, final int recordIndex, final String subject, final String periodStart, final String periodEnd, final String mark) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditIndex);
		super.clickOnButton("Show auditing records");

		super.checkListingExists();
		super.checkColumnHasValue(recordIndex, 0, subject);
		super.checkColumnHasValue(recordIndex, 1, periodStart);
		super.checkColumnHasValue(recordIndex, 2, periodEnd);
		super.checkColumnHasValue(recordIndex, 3, mark);

		super.signOut();
	}

	@Test
	public void test300hacking() {
		Collection<AuditingRecords> ars;
		String param;
		ars = this.repository.findAuditingRecordsByAuditor("auditor1");

		for (final AuditingRecords a : ars) {
			param = String.format("masterId=%d", a.getId());
			super.checkLinkExists("Sign in");
			super.request("/auditor/auditingRecords/list", param);
			super.checkPanicExists();

			super.checkLinkExists("Sign in");
			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditingRecords/list", param);
			super.checkPanicExists();
			super.signOut();

			super.checkLinkExists("Sign in");
			super.signIn("student1", "student1");
			super.request("/auditor/auditingRecords/list", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
