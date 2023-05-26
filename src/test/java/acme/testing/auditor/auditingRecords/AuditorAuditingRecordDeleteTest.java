
package acme.testing.auditor.auditingRecords;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecords.AuditingRecords;
import acme.testing.TestHarness;

public class AuditorAuditingRecordDeleteTest extends TestHarness {

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-records/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100positive(final int auditIndex, final int recordIndex, final String subject, final String assessment, final String startDate, final String finishDate, final String mark, final String published, final String link,
		final String addendum) {

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditIndex);
		super.clickOnButton("Show auditing records");
		super.checkListingExists();
		super.clickOnListingRecord(recordIndex);
		super.clickOnSubmit("Delete");
		super.checkNotPanicExists();
		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<AuditingRecords> ars;

		String param;

		ars = this.repository.findAuditingRecordsByAuditor("auditor1");

		super.signIn("auditor1", "auditor1");
		for (final AuditingRecords a : ars)
			if (a.isPublished()) {
				param = String.format("id=%d", a.getId());
				super.request("/auditor/auditingRecords/delete", param);
				super.checkPanicExists();
			}
	}

	@Test
	public void test301Hacking() {
		final Collection<AuditingRecords> ars;
		String params;

		super.signIn("auditor1", "auditor1");
		ars = this.repository.findAuditingRecordsByAuditor("auditor1");
		for (final AuditingRecords a : ars)
			if (!a.getAudit().isPublished() && a.isPublished()) {
				params = String.format("auditId=%d", a.getAudit().getId());
				super.request("/auditor/auditing-record/delete", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {

		Collection<AuditingRecords> ars;

		String param;

		ars = this.repository.findAuditingRecordsByAuditor("auditor1");

		for (final AuditingRecords a : ars) {
			param = String.format("id=%d", a.getId());
			super.request("/auditor/auditingRecords/delete", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/auditor/auditing-record/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditingRecords/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/auditor/auditingRecords/delete", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test303Hacking() {
		Collection<AuditingRecords> ars;
		String params;

		super.signIn("auditor2", "auditor2");
		ars = this.repository.findAuditingRecordsByAuditor("auditor1");
		for (final AuditingRecords a : ars)
			if (!a.getAudit().isPublished() && a.isPublished()) {
				params = String.format("auditId=%d", a.getAudit().getId());
				super.request("/auditor/auditing-record/delete", params);
			}
		super.signOut();
	}

}
