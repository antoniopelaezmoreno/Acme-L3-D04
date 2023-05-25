
package acme.testing.auditor.audit;

import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class AuditorAuditShowTest extends TestHarness {

	@Autowired
	protected AuditorAuditTestRepository repository;

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/auditor/audit/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100positive(final int recordIndex, final String published, final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String mark) {
	//
	//		super.signIn("auditor1", "auditor1");
	//
	//		super.clickOnMenu("Auditor", "List my audits");
	//		super.checkListingExists();
	//
	//		super.sortListing(0, "asc");
	//
	//		super.clickOnListingRecord(recordIndex);
	//
	//		super.checkInputBoxHasValue("code", code);
	//		super.checkInputBoxHasValue("conclusion", conclusion);
	//		super.checkInputBoxHasValue("strongPoints", strongPoints);
	//		super.checkInputBoxHasValue("weakPoints", weakPoints);
	//		//		super.checkInputBoxHasValue("mark", mark);
	//		//		super.checkInputBoxHasValue("published", published);
	//		//		super.checkInputBoxHasValue("course", course);
	//
	//		super.signOut();
	//	}

	//	@Test
	//	public void test300Hacking() {
	//		final Collection<Audit> audits;
	//		String param;
	//
	//		audits = this.repository.findManyAuditsByAuditor("auditor1");
	//		for (final Audit a : audits)
	//			if (a.isPublished()) {
	//				param = String.format("id=%d", a.getId());
	//
	//				super.checkLinkExists("Sign in");
	//				super.request("/auditor/audit/show", param);
	//				super.checkPanicExists();
	//
	//				super.signIn("administrator1", "administrator1");
	//				super.request("/auditor/audit/show", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("auditor2", "auditor2");
	//				super.request("/auditor/audit/show", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//			}
	//	}

}
