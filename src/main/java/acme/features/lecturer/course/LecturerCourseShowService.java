
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.enums.Indication;
import acme.enums.IndicationLecture;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseShowService extends AbstractService<Lecturer, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Course course;
		Lecturer lecturer;

		masterId = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseById(masterId);
		lecturer = course == null ? null : course.getLecturer();
		status = super.getRequest().getPrincipal().hasRole(lecturer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(id);

		final Collection<Lecture> lectures = this.repository.findManyLecturesByCourseId(id);
		int numTeoricos = 0;
		int numPracticos = 0;
		for (final Lecture lecture : lectures)
			if (lecture.getIndicator().equals(IndicationLecture.THEORETICAL))
				numTeoricos++;
			else if (lecture.getIndicator().equals(IndicationLecture.HANDS_ON))
				numPracticos++;
		if (numTeoricos > numPracticos)
			object.setIndicator(Indication.THEORETICAL);
		else if (numPracticos > numTeoricos)
			object.setIndicator(Indication.HANDS_ON);
		else
			object.setIndicator(Indication.BALANCED);

		this.repository.save(object);
		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		SelectChoices indicators;
		Tuple tuple;

		indicators = SelectChoices.from(Indication.class, object.getIndicator());

		tuple = super.unbind(object, "code", "title", "courseAbstract", "indicator", "retailPrice", "link", "published");
		tuple.put("indicators", indicators);

		super.getResponse().setData(tuple);
	}
}
