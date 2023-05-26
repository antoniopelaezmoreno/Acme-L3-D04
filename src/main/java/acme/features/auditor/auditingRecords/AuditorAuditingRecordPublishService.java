
package acme.features.auditor.auditingRecords;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordPublishService extends AbstractService<Auditor, AuditingRecords> {

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
		int id;
		Audit audit;
		Auditor auditor;
		AuditingRecords ar;

		auditor = this.repository.findAuditorByUserId(super.getRequest().getPrincipal().getAccountId());
		id = super.getRequest().getData("id", int.class);
		ar = this.repository.findOneAuditingRecord(id);
		audit = ar.getAudit();
		status = audit != null && ar != null && super.getRequest().getPrincipal().hasRole(Auditor.class) && audit.getAuditor().getId() == auditor.getId();

		super.getResponse().setAuthorised(status && !audit.isPublished() && !ar.isPublished());
	}

	@Override
	public void load() {
		AuditingRecords object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditingRecord(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecords object) {
		assert object != null;

		super.bind(object, "subject", "assessment", "periodStart", "periodEnd", "mark", "link", "published");
	}

	@Override
	public void validate(final AuditingRecords object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("periodEnd")) {
			Date minEnd;

			minEnd = MomentHelper.deltaFromMoment(object.getPeriodStart(), 1, ChronoUnit.HOURS);
			super.state(MomentHelper.isAfterOrEqual(object.getPeriodEnd(), minEnd), "periodEnd", "audior.auditingRecords.form.error.periodEnd");
		}
	}

	@Override
	public void perform(final AuditingRecords object) {
		assert object != null;
		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "subject", "assessment", "periodStart", "periodEnd", "mark", "link", "published");

		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("audit", object.getAudit().getCode());

		super.getResponse().setData(tuple);
	}
}
