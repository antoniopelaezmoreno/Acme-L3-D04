
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.enums.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordCreateService extends AbstractService<Auditor, AuditingRecords> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int auditId;
		Audit audit;

		auditId = super.getRequest().getData("masterId", int.class);
		audit = this.repository.findOneAuditById(auditId);
		status = audit != null && super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		// final AuditingRecords auditingRecord = new AuditingRecords();
		AuditingRecords object;
		int auditId;
		Audit audit;

		auditId = super.getRequest().getData("masterId", int.class);
		audit = this.repository.findOneAuditById(auditId);

		object = new AuditingRecords();
		object.setAudit(audit);

		super.getBuffer().setData(object);
		// super.getBuffer().setData(auditingRecord);
	}

	@Override
	public void bind(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		super.bind(auditingRecord, "subject", "assesment", "periodStart", "periodEnd", "mark", "link");

		final Audit audit = this.repository.findOneAuditById(super.getRequest().getData("auditId", int.class));
		auditingRecord.setAudit(audit);
	}

	@Override
	public void validate(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		if (!super.getBuffer().getErrors().hasErrors("audit"))
			super.state(auditingRecord.getAudit() != null, "audit", "auditor.auditingRecord.form.audit.nullError");

		final boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

	}

	@Override
	public void perform(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		this.repository.save(auditingRecord);
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;

		final SelectChoices marks;
		Tuple tuple;

		marks = SelectChoices.from(Mark.class, object.getMark());

		tuple = super.unbind(object, "subject", "assesment", "firstDate", "lastDate", "mark", "link", "draftMode");
		tuple.put("marks", marks);

		super.getResponse().setData(tuple);
	}
}
