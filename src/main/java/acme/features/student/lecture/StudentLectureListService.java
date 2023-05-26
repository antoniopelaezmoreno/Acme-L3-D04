
package acme.features.student.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentLectureListService extends AbstractService<Student, Lecture> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Lecture> objects;

		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findManyLecturesByCourseId(masterId);
		super.getResponse().setGlobal("masterId", masterId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		//int masterId;
		//masterId = super.getRequest().getData("masterId", int.class);

		Tuple tuple;

		tuple = super.unbind(object, "title", "lectureAbstract", "indicator");
		//super.getResponse().setGlobal("masterId", masterId);

		super.getResponse().setData(tuple);
	}
}
