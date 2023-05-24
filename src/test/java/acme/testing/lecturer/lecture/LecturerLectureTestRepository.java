
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;

public interface LecturerLectureTestRepository extends AbstractRepository {

	@Query("select lc.lecture from LectureCourse lc where lc.course.code = :code")
	Collection<Lecture> findManyLecturesByCourseCode(String code);

	@Query("select c from Course c where c.lecturer.userAccount.username = :userName")
	Collection<Course> findManyCoursesByUserName(String userName);

	@Query("select lc.lecture from LectureCourse lc where lc.course.lecturer.userAccount.username = :userName")
	Collection<Lecture> findManyLecturesByUserName(String userName);
}
