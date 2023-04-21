
package acme.features.auditor.auditingRecords;

import java.util.List;
import java.util.Map;
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

		super.bind(object, "subject", "assessment", "periodStart", "periodEnd", "mark", "link");
	}

	@Override
	public void validate(final AuditingRecords object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditingRecords object) {
		assert object != null;

		this.repository.delete(object);

		// Obtener el objeto Audit asociado al objeto AuditingRecords que se acaba de eliminar
		final Audit audit = object.getAudit();

		// Obtener todos los objetos AuditingRecords asociados a ese objeto Audit
		final List<AuditingRecords> records = (List<AuditingRecords>) this.repository.findAllAuditingRecordsFromOneAudit(audit.getId());

		// Calcular la nueva marca de la auditoría en base a las marcas de los objetos AuditingRecords restantes
		final List<Mark> marks = records.stream().map(AuditingRecords::getMark).collect(Collectors.toList());
		marks.remove(object.getMark());
		final Mark newMark = this.getMode(marks);

		// Actualizar el objeto Audit con la nueva marca
		audit.setMark(newMark);
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

		tuple = super.unbind(object, "subject", "assessment", "periodStart", "periodEnd", "mark", "link");

		tuple.put("published", object.getAudit().isPublished());

		super.getResponse().setData(tuple);
	}
}
