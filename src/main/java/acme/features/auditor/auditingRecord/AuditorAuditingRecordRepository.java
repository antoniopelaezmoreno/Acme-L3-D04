
package acme.features.auditor.auditingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorAuditingRecordRepository extends AbstractRepository {

	@Query("select a from AuditingRecords a where a.audit.id = :id")
	Collection<AuditingRecords> findAllAuditingRecordsFromOneAudit(int id);

	@Query("select a from AuditingRecords a where a.id = :id")
	AuditingRecords findOneAuditingRecord(int id);

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

}
