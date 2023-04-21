
package acme.features.auditor.audit;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditUpdateService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRepository repository;

	// AbstractService interface ----------------------------------------------ç


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
		Audit audit;
		Auditor auditor;

		masterId = super.getRequest().getData("id", int.class);
		audit = this.repository.findOneAuditById(masterId);
		auditor = audit == null ? null : audit.getAuditor();
		status = super.getRequest().getPrincipal().hasRole(auditor);

		super.getResponse().setAuthorised(status && !audit.isPublished());
	}

	@Override
	public void load() {
		Audit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean valid;
			Optional<Audit> existing;

			existing = this.repository.findOneAuditByCode(object.getCode());
			if (!existing.isPresent())
				valid = true;
			else if (existing.get().getId() == object.getId())
				valid = true;
			else
				valid = false;
			super.state(valid, "code", "Auditor.Audit.form.error.duplicated");
		}

	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		// SelectChoices marks;
		// final List<Course> auditsCourse = this.repository.findAllCoursesFromAudit();
		// courses.removeAll(auditsCourse);
		// courses.add(object.getCourse());
		// marks = SelectChoices.from(Mark.class, object.getMark());
		// tuple.put("marks", marks);

		Tuple tuple;

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "published");

		final Collection<Course> courses = this.repository.findAllCourses().stream().filter(x -> x.isPublished()).collect(Collectors.toList());

		final SelectChoices courses2 = SelectChoices.from(courses, "title", object.getCourse());
		tuple.put("courses", courses2);

		super.getResponse().setData(tuple);
	}

}
