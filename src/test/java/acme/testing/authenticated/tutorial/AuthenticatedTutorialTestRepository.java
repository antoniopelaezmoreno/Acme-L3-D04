
package acme.testing.authenticated.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

public interface AuthenticatedTutorialTestRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.assistant.userAccount.username = :userName")
	Collection<Tutorial> findManyTutorialsByUserName(String userName);

	@Query("select c from Course c where c.code = :code")
	Course findCourseByCode(String code);

}
