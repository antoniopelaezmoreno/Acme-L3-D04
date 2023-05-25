/*
 * EmployerJobUpdateService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialUpdateService extends AbstractService<Assistant, Tutorial> {

	public static final String[]			ATTRIBUTES	= {
		"title", "code", "tutorialAbstract", "goals", "estimatedTime", "published"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository	repository;

	// AbstractService<Employer, Job> -------------------------------------


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
		Tutorial tutorial;
		Collection<Tutorial> myTutorials;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(id);
		principal = super.getRequest().getPrincipal();
		myTutorials = this.repository.findManyTutorialsByAssistantId(principal.getActiveRoleId());
		status = tutorial != null && !tutorial.isPublished() && principal.hasRole(Assistant.class) && myTutorials.contains(tutorial);

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
	public void bind(final Tutorial object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);

		super.bind(object, AssistantTutorialUpdateService.ATTRIBUTES);
		object.setCourse(course);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean uniqueCode;
			boolean sameCode;

			uniqueCode = this.repository.existsTutorialByCode(object.getCode());
			sameCode = this.repository.checksameTutorialByCode(object.getCode(), object.getId());
			if (!sameCode)
				super.state(uniqueCode, "code", "assistant.tutorial.form.error.uniqueCode");
		}
		if (!super.getBuffer().getErrors().hasErrors("course")) {
			boolean availableCourse;
			boolean sameCourse;

			availableCourse = this.repository.checkAvailableCourseById(object.getCourse().getId());
			sameCourse = this.repository.checkSameCourseById(object.getCourse().getId(), object.getId());
			if (!sameCourse)
				super.state(availableCourse, "course", "assistant.tutorial.form.error.availableCourse");
		}
	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tuple;
		Collection<Course> courses;
		SelectChoices choices;
		Principal principal;
		Collection<Tutorial> myTutorials;

		courses = this.repository.findManyAccessibleCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		principal = super.getRequest().getPrincipal();
		myTutorials = this.repository.findManyTutorialsByAssistantId(principal.getActiveRoleId());
		tuple = super.unbind(object, AssistantTutorialUpdateService.ATTRIBUTES);
		tuple.put("courses", choices);
		tuple.put("assistant", object.getAssistant().getSupervisor());
		tuple.put("showSessions", object.isPublished() && principal.hasRole(Assistant.class) && myTutorials.contains(object));
		tuple.put("showAssistant", object.isPublished());

		super.getResponse().setData(tuple);
	}
}
