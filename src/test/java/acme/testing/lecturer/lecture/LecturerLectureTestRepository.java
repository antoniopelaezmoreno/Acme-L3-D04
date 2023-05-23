
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;

public interface LecturerLectureTestRepository extends AbstractRepository {

	@Query("select lc.lecture from LectureCourse lc where lc.course.code = :code")
	Collection<Lecture> findManyLecturesByCourseCode(String code);
}
