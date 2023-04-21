
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordUpdateService extends AbstractService<Auditor, AuditingRecords> {

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

		final Integer id = super.getRequest().getData("id", int.class);
		final AuditingRecords auditingRecord = this.repository.findOneAuditingRecord(id);

		super.getBuffer().setData(auditingRecord);
	}

	@Override
	public void bind(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		super.bind(auditingRecord, "subject", "assesment", "periodStart", "periodEnd", "mark", "link");
	}

	@Override
	public void validate(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

	}

	@Override
	public void perform(final AuditingRecords object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "subject", "assesment", "firstDate", "lastDate", "mark");

		tuple.put("published", object.getAudit().isPublished());

		super.getResponse().setData(tuple);
	}
}
