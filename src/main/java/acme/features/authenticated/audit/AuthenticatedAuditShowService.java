
package acme.features.authenticated.audit;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.enums.Mark;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuthenticatedAuditShowService extends AbstractService<Authenticated, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAuditRepository repository;

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
		Audit audit;

		id = super.getRequest().getData("id", int.class);
		audit = this.repository.findOneAuditById(id);
		status = audit != null && audit.getCourse() != null;

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

		SelectChoices marks;
		final SelectChoices courses2;
		Tuple tuple;
		Collection<Course> courses;

		marks = SelectChoices.from(Mark.class, object.getMark());

		courses = this.repository.findAllCourses();

		final List<Course> auditsCourse = this.repository.findAllCoursesFromAudit();

		courses.removeAll(auditsCourse);

		courses.add(object.getCourse());

		courses2 = SelectChoices.from(courses, "title", object.getCourse());

		final Auditor auditor = this.repository.findAuditorByAuditId(object.getId());

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "mark", "published", "course");
		tuple.put("marks", marks);
		tuple.put("courses", courses2);

		tuple.put("userName", auditor.getUserAccount().getUsername());
		tuple.put("firm", auditor.getFirm());
		tuple.put("link", auditor.getLink());

		super.getResponse().setData(tuple);
	}

}
