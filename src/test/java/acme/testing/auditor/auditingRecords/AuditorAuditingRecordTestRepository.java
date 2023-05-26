
package acme.testing.auditor.auditingRecords;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audit.Audit;
import acme.entities.auditingRecords.AuditingRecords;
import acme.framework.repositories.AbstractRepository;

public interface AuditorAuditingRecordTestRepository extends AbstractRepository {

	@Query("select a from Audit a where a.auditor.userAccount.username = :username")
	Collection<Audit> findAuditsByAuditor(String username);

	@Query("select ar from AuditingRecords ar where ar.audit.auditor.userAccount.username = :username")
	Collection<AuditingRecords> findAuditingRecordsByAuditor(String username);

}
