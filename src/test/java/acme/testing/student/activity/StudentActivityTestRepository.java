
package acme.testing.student.activity;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.activity.Activity;
import acme.entities.enrolment.Enrolment;
import acme.framework.repositories.AbstractRepository;

public interface StudentActivityTestRepository extends AbstractRepository {

	@Query("select e from Enrolment e where e.student.userAccount.username = :username")
	Collection<Enrolment> findManyEnrolmentsByStudentUsername(String username);

	@Query("select a from Activity a where a.enrolment.student.userAccount.username = :username")
	Collection<Activity> findManyActivitiesByStudentUsername(String username);

}
