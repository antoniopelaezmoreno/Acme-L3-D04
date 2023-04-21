
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

		super.getResponse().setAuthorised(status && !auditingRecord.isPublished());
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

		super.bind(auditingRecord, "subject", "assessment", "periodStart", "periodEnd", "mark", "published", "link");
	}

	@Override
	public void validate(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

	}

	@Override
	public void perform(final AuditingRecords auditingRecord) {
		assert auditingRecord != null;

		// Actualizamos la mark del auditingRecord
		auditingRecord.setMark(Mark.B);

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

		Tuple tuple;

		tuple = super.unbind(object, "subject", "assessment", "periodStart", "periodEnd", "mark", "published");

		tuple.put("published", object.getAudit().isPublished());

		super.getResponse().setData(tuple);
	}
}
