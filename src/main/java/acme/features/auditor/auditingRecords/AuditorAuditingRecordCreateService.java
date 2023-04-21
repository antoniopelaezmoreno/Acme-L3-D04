
package acme.features.auditor.auditingRecords;

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
		AuditingRecords object;
		int auditId;
		Audit audit;

		auditId = super.getRequest().getData("masterId", int.class);
		audit = this.repository.findOneAuditById(auditId);

		object = new AuditingRecords();
		object.setAudit(audit);

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
			super.state(auditingRecord.getAudit() != null, "audit", "auditor.auditingRecord.form.audit.nullError");

		final boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

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

		super.getResponse().setData(tuple);
	}
}
