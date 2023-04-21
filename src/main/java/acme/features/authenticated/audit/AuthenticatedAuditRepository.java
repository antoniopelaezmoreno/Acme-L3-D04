
package acme.features.authenticated.audit;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuthenticatedAuditRepository extends AbstractRepository {

	@Query("select a from Audit a where a.course != null")
	Collection<Audit> findAllAssociatedAudits();

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select a.course from Audit a")
	List<Course> findAllCoursesFromAudit();

	@Query("SELECT a FROM Auditor a WHERE a.id = (SELECT audit.auditor.id FROM Audit audit WHERE audit.id = :id)")
	Auditor findAuditorByAuditId(@Param("id") Integer id);

}
