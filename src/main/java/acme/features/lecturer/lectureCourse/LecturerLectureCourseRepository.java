
package acme.features.lecturer.lectureCourse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.course.LectureCourse;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface LecturerLectureCourseRepository extends AbstractRepository {

	@Query("select c from Course c where c.lecturer.id = :lecturerId")
	List<Course> findManyCoursesByLecturerId(int lecturerId);

	@Query("select lc.lecture from LectureCourse lc where lc.course.lecturer.id = :lecturerId")
	List<Lecture> findManyLecturesByLecturerId(int lecturerId);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select l from Lecture l where l.id = :id")
	Lecture findLectureById(int id);

	@Query("select lc.lecture from LectureCourse lc where lc.course.id = :courseId")
	List<Lecture> findManyLecturesByCourseId(int courseId);

	@Query("select lc from LectureCourse lc where lc.course.id = :courseId and lc.lecture.id = :lectureId")
	Optional<LectureCourse> findOneLectureCourseByCourseIdAndLectureId(int courseId, int lectureId);

}
