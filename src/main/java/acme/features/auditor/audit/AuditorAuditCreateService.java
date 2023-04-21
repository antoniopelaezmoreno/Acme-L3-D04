
package acme.features.auditor.audit;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.enums.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditCreateService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Audit object = new Audit();
		Auditor auditor;

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());

		object.setPublished(false);
		object.setAuditor(auditor);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		final Course course = this.repository.findCourse(super.getRequest().getData("course", int.class));

		super.bind(object, "code", "conclusion", "strongPoints", "weakPoints", "published");

		object.setCourse(course);

	}

	@Override
	public void validate(final Audit object) {
		assert object != null;

		//		if (!super.getBuffer().getErrors().hasErrors("course"))
		//			super.state(object.getCourse() != null, "courseId", "auditor.audit.form.course.nullError");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Optional<Audit> existing;

			existing = this.repository.findOneAuditByCode(object.getCode());
			if (existing.isPresent())
				super.state(existing == null, "code", "auditor.audit.form.error.duplicated");

		}

		if (!super.getBuffer().getErrors().hasErrors("course")) {

		}

		//		if (!super.getBuffer().getErrors().hasErrors("course")) {
		//			boolean emptyCourse;
		//
		//			emptyCourse = this.repository.checkEmptyCourseById(object.getCourse().getId());
		//			super.state(emptyCourse, "course", "javax.validation.constraints.AssertTrue.message");
		//		}

		//		if (!super.getBuffer().getErrors().hasErrors("course")) {
		//			boolean emptyCourse;
		//
		//			emptyCourse = this.repository.checkEmptyCourseById(object.getCourse().getId());
		//			super.state(emptyCourse, "course", "javax.validation.constraints.AssertTrue.message");
		//		}

	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		object.setMark(Mark.N);

		this.repository.save(object);

	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "published");

		final Collection<Course> courses = this.repository.findAllCourses().stream().filter(x -> x.isPublished()).collect(Collectors.toList());

		//		final Collection<Audit> auditsWithCourses = this.repository.findAuditsWithCourses();

		//		final Collection<Course> coursesAux = null;
		//		Course c;
		//		for (final Audit a : auditsWithCourses) {
		//			c = this.repository.findCourseByAudit(a.getId());
		//			coursesAux.add(c);
		//		}
		//		
		//		courses.removeAll()

		final SelectChoices courses2 = SelectChoices.from(courses, "title", object.getCourse());
		tuple.put("courses", courses2);

		super.getResponse().setData(tuple);
	}

}
