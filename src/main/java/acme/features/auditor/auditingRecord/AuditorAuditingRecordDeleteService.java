
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordDeleteService extends AbstractService<Auditor, AuditingRecords> {

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
		boolean status;

		final AuditingRecords auditingRecord = this.repository.findOneAuditingRecord(super.getRequest().getData("id", int.class));
		status = auditingRecord != null;

		final Audit audit = auditingRecord.getAudit();
		if (audit != null)
			status = audit.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecords auditingRecord;

		auditingRecord = this.repository.findOneAuditingRecord(super.getRequest().getData("id", int.class));

		super.getBuffer().setData(auditingRecord);
	}

	@Override
	public void bind(final AuditingRecords object) {
		assert object != null;

		super.bind(object, "subject", "assesment", "periodStart", "periodEnd", "mark", "link");
	}

	@Override
	public void validate(final AuditingRecords object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditingRecords object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "subject", "assesment", "periodStart", "periodEnd", "mark", "link");

		tuple.put("published", object.getAudit().isPublished());

		super.getResponse().setData(tuple);
	}
}
