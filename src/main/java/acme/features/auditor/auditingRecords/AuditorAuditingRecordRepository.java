
package acme.features.auditor.auditingRecords;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuditorAuditingRecordRepository extends AbstractRepository {

	@Query("select a from AuditingRecords a where a.audit.id = :id")
	Collection<AuditingRecords> findAllAuditingRecordsFromOneAudit(int id);

	@Query("select a from AuditingRecords a where a.id = :id")
	AuditingRecords findOneAuditingRecord(int id);

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

	@Query("select a.audit from AuditingRecords a where a.id=:id")
	Audit findOneAuditByAuditingRecord(int id);

	@Query("select a from AuditingRecords a where a.audit.id = :auditId")
	Collection<AuditingRecords> findManyAuditingRecordsByAuditId(int auditId);

	@Query("select a from Auditor a where a.userAccount.id = :id")
	Auditor findAuditorByUserId(int id);
}
