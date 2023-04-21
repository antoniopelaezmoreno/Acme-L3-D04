
package acme.features.auditor.auditingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordListService extends AbstractService<Auditor, AuditingRecords> {

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
		status = audit != null && super.getRequest().getPrincipal().hasRole(audit.getAuditor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Collection<AuditingRecords> auditingRecords = this.repository.findAllAuditingRecordsFromOneAudit(super.getRequest().getData("masterId", Integer.class));
		final int auditId;

		auditId = super.getRequest().getData("masterId", Integer.class);
		super.getResponse().setGlobal("masterId", auditId);
		super.getBuffer().setData(auditingRecords);
	}

	@Override
	public void unbind(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;
		Tuple tuple;

		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		tuple = super.unbind(auditingRecord, "subject", "periodStart", "periodEnd", "mark", "published");

		super.getResponse().setGlobal("masterId", masterId);

		super.getResponse().setData(tuple);
	}

	//	@Override
	//	public void unbind(final Collection<AuditingRecords> objects) {
	//		assert objects != null;
	//
	//		int auditId;
	//		Audit audit;
	//		final boolean showCreate;
	//		final boolean exceptionalCreate;
	//
	//		auditId = super.getRequest().getData("auditId", int.class);
	//		audit = this.repository.findOneAuditById(auditId);
	//		showCreate = super.getRequest().getPrincipal().hasRole(audit.getAuditor());
	//		exceptionalCreate = audit.isPublished();
	//
	//		super.getResponse().setGlobal("auditId", auditId);
	//		super.getResponse().setGlobal("showCreate", showCreate);
	//		super.getResponse().setGlobal("exceptionalCreate", exceptionalCreate);
	//	}
}
