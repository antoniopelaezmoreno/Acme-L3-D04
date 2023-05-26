
package acme.features.auditor.auditingRecords;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.enums.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
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
		Auditor auditor;
		Collection<AuditingRecords> ars;

		auditor = this.repository.findAuditorByUserId(super.getRequest().getPrincipal().getAccountId());

		auditId = super.getRequest().getData("masterId", int.class);
		audit = this.repository.findOneAuditById(auditId);

		ars = this.repository.findManyAuditingRecordsByAuditId(auditId);

		status = audit != null && super.getRequest().getPrincipal().hasRole(Auditor.class) && audit.getAuditor().getId() == auditor.getId();
		super.getResponse().setAuthorised(status && !audit.isPublished() || status && audit.isPublished() && ars.stream().filter(AuditingRecords::isAddendum).collect(Collectors.toList()).isEmpty());

	}

	@Override
	public void load() {
		AuditingRecords object;
		int auditId;
		Audit audit;

		auditId = super.getRequest().getData("masterId", int.class);
		audit = this.repository.findOneAuditById(auditId);

		object = new AuditingRecords();
		object.setAudit(audit);

		object.setPublished(audit.isPublished() ? true : false);
		object.setAddendum(audit.isPublished() ? true : false);

		super.getResponse().setGlobal("masterId", auditId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		super.bind(auditingRecord, "subject", "assessment", "periodStart", "periodEnd", "mark", "published", "link");

		final int id = super.getRequest().getData("masterId", int.class);

		final Audit audit = this.repository.findOneAuditById(id);
		auditingRecord.setAudit(audit);
	}

	@Override
	public void validate(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		if (!super.getBuffer().getErrors().hasErrors("audit"))
			super.state(auditingRecord.getAudit() != null, "audit", "auditor.auditingRecords.form.audit.nullError");

		if (!super.getBuffer().getErrors().hasErrors("periodStart"))
			super.state(auditingRecord.getPeriodStart() != null, "periodStart", "auditor.auditingRecords.form.audit.periodStart.nullError");

		if (!super.getBuffer().getErrors().hasErrors("periodEnd"))
			super.state(auditingRecord.getPeriodEnd() != null, "periodEnd", "auditor.auditingRecords.form.audit.periodEnd.nullError");

		if (auditingRecord.getAudit().isPublished()) {
			final boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
		}

		if (!super.getBuffer().getErrors().hasErrors("mark"))
			super.state(auditingRecord.getMark() != null, "mark", "auditor.auditingRecords.form.audit.mark.nullError");

		if (!super.getBuffer().getErrors().hasErrors("periodStart") && !super.getBuffer().getErrors().hasErrors("periodEnd"))
			if (auditingRecord.getPeriodStart() != null && auditingRecord.getPeriodEnd() != null)
				if (!MomentHelper.isBefore(auditingRecord.getPeriodStart(), auditingRecord.getPeriodEnd()))
					super.state(false, "periodStart", "auditor.auditingRecords.error.date.startAfterFinish");
				else
					super.state(!(auditingRecord.getHoursFromStart() < 1), "periodStart", "auditor.auditingRecords.error.date.shortPeriod");
	}

	@Override
	public void perform(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		this.repository.save(auditingRecord);

		final Audit audit = auditingRecord.getAudit();
		final List<AuditingRecords> records = (List<AuditingRecords>) this.repository.findAllAuditingRecordsFromOneAudit(audit.getId());
		final List<Mark> marks = records.stream().map(AuditingRecords::getMark).collect(Collectors.toList());
		Mark mode = this.getMode(marks);

		// si hay empate, elegir uno al azar
		if (mode == null) {
			final Random rand = new Random();
			mode = marks.get(rand.nextInt(marks.size()));
		}

		audit.setMark(mode);
		this.repository.save(audit);
	}

	private Mark getMode(final List<Mark> marks) {
		final Map<Mark, Long> freqMap = marks.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		final long maxFreq = freqMap.values().stream().max(Long::compareTo).orElse(0L);

		if (maxFreq == 1)
			return null; // no hay moda

		return freqMap.entrySet().stream().filter(entry -> entry.getValue() == maxFreq).map(Map.Entry::getKey).findFirst().orElse(null);
	}

	@Override
	public void unbind(final AuditingRecords object) {
		assert object != null;

		final SelectChoices marks;
		Tuple tuple;

		marks = SelectChoices.from(Mark.class, object.getMark());

		tuple = super.unbind(object, "subject", "assessment", "periodStart", "periodEnd", "mark", "published", "link");
		tuple.put("marks", marks);
		tuple.put("audit", object.getAudit().getCode());
		tuple.put("publishedAudit", object.getAudit().isPublished());

		super.getResponse().setData(tuple);
	}
}
