
package acme.features.lecturer.lectureCourse;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.LectureCourse;
import acme.entities.lecture.Lecture;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureCourseCreateService extends AbstractService<Lecturer, LectureCourse> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureCourseRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Lecturer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		LectureCourse object;

		object = new LectureCourse();
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final LectureCourse object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);

		int lectureId;
		Lecture lecture;

		lectureId = super.getRequest().getData("lecture", int.class);
		lecture = this.repository.findLectureById(lectureId);

		object.setCourse(course);
		object.setLecture(lecture);
	}

	@Override
	public void validate(final LectureCourse object) {
		assert object != null;
		int lecturerId;
		lecturerId = super.getRequest().getPrincipal().getActiveRoleId();

		Collection<Course> courses;
		courses = this.repository.findManyCoursesByLecturerId(lecturerId).stream().filter(x -> !x.isPublished()).collect(Collectors.toList());

		super.state(courses.contains(object.getCourse()), "course", "lecturer.lecturerCourse.form.error.courseProperty");

		Collection<Lecture> lectures;
		lectures = this.repository.findManyLecturesByLecturerId(lecturerId);

		super.state(lectures.contains(object.getLecture()), "lecture", "lecturer.lecturerCourse.form.error.lectureProperty");

		final Optional<LectureCourse> existing = this.repository.findOneLectureCourseByCourseIdAndLectureId(object.getCourse().getId(), object.getLecture().getId());

		super.state(!existing.isPresent(), "lecture", "lecturer.lecturerCourse.form.error.alreadyExists");

		super.state(!object.getCourse().isPublished(), "course", "lecturer.lecturerCourse.form.error.published");


	}

	@Override
	public void perform(final LectureCourse object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final LectureCourse object) {
		assert object != null;
		final SelectChoices choicesCourses;
		final SelectChoices choicesLectures;
		final Tuple tuple;
		final List<Course> allCourses;
		final List<Course> coursesPublished;
		final Set<Lecture> lectures = new HashSet<Lecture>();
		int lecturerId;
		lecturerId = super.getRequest().getPrincipal().getActiveRoleId();

		allCourses = this.repository.findManyCoursesByLecturerId(lecturerId);

		coursesPublished = allCourses.stream().filter(x -> !x.isPublished()).collect(Collectors.toList());

		for (final Course c : allCourses) {
			final List<Lecture> lecturesFromCourse = this.repository.findManyLecturesByCourseId(c.getId());
			lectures.addAll(lecturesFromCourse);
		}

		choicesCourses = SelectChoices.from(coursesPublished, "title", object.getCourse());
		choicesLectures = SelectChoices.from(lectures, "title", object.getLecture());

		tuple = super.unbind(object, "course", "lecture");
		tuple.put("choicesCourses", choicesCourses);
		tuple.put("choicesLectures", choicesLectures);

		super.getResponse().setData(tuple);
	}
}
