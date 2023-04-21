
package acme.features.auditor.audit;

import java.util.Collection;
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
public class AuditorAuditShowService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRepository repository;

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
		Audit audit;
		Auditor auditor;

		masterId = super.getRequest().getData("id", int.class);
		audit = this.repository.findOneAuditById(masterId);
		auditor = audit == null ? null : audit.getAuditor();
		status = super.getRequest().getPrincipal().hasRole(auditor);

		super.getResponse().setAuthorised(status);
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
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;
		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "mark", "published");

		final Collection<Course> courses = this.repository.findAllCourses().stream().filter(x -> x.isPublished()).collect(Collectors.toList());

		final SelectChoices courses2 = SelectChoices.from(courses, "title", object.getCourse());
		tuple.put("courses", courses2);

		final SelectChoices marks = SelectChoices.from(Mark.class, object.getMark());
		tuple.put("marks", marks);

		super.getResponse().setData(tuple);
	}

}
