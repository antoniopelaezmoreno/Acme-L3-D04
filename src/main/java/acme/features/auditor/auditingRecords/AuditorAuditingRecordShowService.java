
package acme.features.auditor.auditingRecords;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditingRecords.AuditingRecords;
import acme.enums.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordShowService extends AbstractService<Auditor, AuditingRecords> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AuditingRecords auditingRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		auditingRecord = this.repository.findOneAuditingRecord(id);
		// super.getResponse().setGlobal("id", id);

		super.getBuffer().setData(auditingRecord);
	}

	@Override
	public void unbind(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;
		final SelectChoices marks;

		Tuple tuple;

		marks = SelectChoices.from(Mark.class, auditingRecord.getMark());

		tuple = super.unbind(auditingRecord, "subject", "assessment", "periodStart", "periodEnd", "mark", "published", "link");
		tuple.put("marks", marks);
		// tuple.put("published", auditingRecord.getAudit().isPublished());

		super.getResponse().setData(tuple);
	}
}
