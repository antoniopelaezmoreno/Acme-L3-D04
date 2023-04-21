
package acme.features.lecturer.lecturerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface LecturerDashboardRepository extends AbstractRepository {

	@Query("select count(l) from Lecture l where l.indicator =acme.enums.Indication.THEORETICAL")
	Integer numberOfLectureTheorical(Integer userId);

	@Query("select count(l) from Lecture l where l.indicator =acme.enums.Indication.HANDS_ON")
	Integer numberOfLectureHandsOn(Integer userId);

	@Query("select AVG(lc.lecture.estimatedTime) from LectureCourse lc where lc.course.lecturer.id = :userId")
	Double lectureLearningTimeAverage(Integer userId);

	@Query("select lc.lecture.estimatedTime from LectureCourse lc where lc.course.lecturer.id = :userId")
	Collection<Integer> getLectureEstimatedTimes(Integer userId);

	@Query("select MAX(lc.lecture.estimatedTime) from LectureCourse lc where lc.course.lecturer.id = :userId")
	Double lectureLearningTimeMax(Integer userId);

	@Query("select MIN(lc.lecture.estimatedTime) from LectureCourse lc where lc.course.lecturer.id = :userId")
	Double lectureLearningTimeMin(Integer userId);

	//	@Query("SELECT MAX(SUM(l.estimatedTime)) FROM LectureCourse lc JOIN lc.course c JOIN lc.lecture l WHERE c.lecturer.id = :userId GROUP BY c.id")
	//	Double courseLearningTimeMax(Integer userId);
	//
	//	@Query("SELECT MIN(SUM(l.estimatedTime)) FROM LectureCourse lc JOIN lc.course c JOIN lc.lecture l WHERE c.lecturer.id = :userId GROUP BY c.id")
	//	Double courseLearningTimeMin(Integer userId);
	//
	//	@Query("SELECT AVG(SUM(l.estimatedTime)) FROM LectureCourse lc JOIN lc.course c JOIN lc.lecture l WHERE c.lecturer.id = :userId GROUP BY c.id")
	//	Double courseLearningTimeAverage(Integer userId);

	@Query("select count(lc) from LectureCourse lc where lc.course.lecturer.id = :userId")
	Integer numberOfLectures(Integer userId);

}
