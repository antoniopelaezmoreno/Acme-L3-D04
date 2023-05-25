
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedTutorialShowService extends AbstractService<Authenticated, Tutorial> {

	public static final String[]				ATTRIBUTES	= {
		"title", "code", "tutorialAbstract", "goals", "estimatedTime", "published"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedTutorialRepository	repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		int id;
		Tutorial tutorial;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(id);
		principal = super.getRequest().getPrincipal();
		status = tutorial != null && tutorial.isPublished() && principal.hasRole(Authenticated.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tuple;
		Collection<Course> courses;
		SelectChoices choices;

		courses = this.repository.findManyAccessibleCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, AuthenticatedTutorialShowService.ATTRIBUTES);
		tuple.put("courses", choices);
		tuple.put("assistant", object.getAssistant().getSupervisor());
		tuple.put("showAssistant", object.isPublished());

		super.getResponse().setData(tuple);
	}
}
