
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditingRecords.AuditingRecords;
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

		super.getBuffer().setData(auditingRecord);
	}

	@Override
	public void unbind(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		Tuple tuple;

		tuple = super.unbind(auditingRecord, "subject", "assesment", "periodStart", "periodEnd", "mark", "link");

		tuple.put("published", auditingRecord.getAudit().isPublished());

		super.getResponse().setData(tuple);
	}
}
