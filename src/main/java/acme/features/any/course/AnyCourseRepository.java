
package acme.features.any.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnyCourseRepository extends AbstractRepository {

	@Query("select c from Course c where c.published = true")
	Collection<Course> findAllPublishedCourses();

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

}
