
package acme.features.student.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.enums.Indication;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentCourseShowService extends AbstractService<Student, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentCourseRepository repository;

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
		int id;
		Course course;

		id = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseById(id);
		status = course != null && course.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		Tuple tuple;
		final String lecturerName = object.getLecturer().getUserAccount().getIdentity().getName();
		final String lecturerSurname = object.getLecturer().getUserAccount().getIdentity().getSurname();

		SelectChoices indicators;

		indicators = SelectChoices.from(Indication.class, object.getIndicator());

		tuple = super.unbind(object, "code", "title", "courseAbstract", "indicator", "retailPrice", "link");
		tuple.put("readonly", true);
		tuple.put("indicators", indicators);
		tuple.put("lecturer", lecturerName + " " + lecturerSurname);

		super.getResponse().setData(tuple);
	}
}
