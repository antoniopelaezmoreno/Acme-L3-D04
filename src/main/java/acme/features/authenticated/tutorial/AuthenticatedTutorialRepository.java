
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedTutorialRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.id = :id")
	Tutorial findTutorialById(int id);

	@Query("SELECT t FROM Tutorial t JOIN t.course c WHERE t.published = true AND c.published = true")
	Collection<Tutorial> findManyAccessibleTutorials();

	@Query("SELECT c FROM Course c WHERE c.published = true")
	Collection<Course> findManyAccessibleCourses();

	@Query("SELECT t FROM Tutorial t WHERE t.assistant.id = :id")
	Collection<Tutorial> findManyTutorialsByAssistantId(int id);
}
