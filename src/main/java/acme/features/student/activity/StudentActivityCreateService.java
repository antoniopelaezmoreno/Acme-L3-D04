
package acme.features.student.activity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activity.Activity;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityCreateService extends AbstractService<Student, Activity> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Activity object;

		object = new Activity();

		object.setTitle("");
		object.setActivityAbstract("");
		object.setLink("");

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		Integer enrolmentId;
		Enrolment enrolment;

		enrolmentId = super.getRequest().getData("enrolment", Integer.class);

		enrolment = this.repository.findOneEnrolmentById(enrolmentId);
		//indicator = super.getRequest().getData("indicator", String.class);
		//System.out.println("El id es: " + enrolmentId);
		//System.out.println("El indicador es: " + indicator);

		super.bind(object, "title", "activityAbstract", "indicator", "periodStart", "periodEnd", "link");
		object.setEnrolment(enrolment);
		//System.out.println(enrolment);

	}

	@Override
	public void validate(final Activity object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("enrolment"))
			super.state(object.getEnrolment() != null, "enrolment", "student.activity.form.error.enrolment");

		if (!super.getBuffer().getErrors().hasErrors("periodEnd"))
			super.state(MomentHelper.isAfter(object.getPeriodEnd(), object.getPeriodStart()), "periodEnd", "student.activity.form.error.periodEnd");

	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		Double duration;
		Double workTime;
		Enrolment enrolment;

		duration = (double) MomentHelper.computeDuration(object.getPeriodStart(), object.getPeriodEnd()).toHours();
		enrolment = object.getEnrolment();
		workTime = enrolment.getWorkTime();

		if (workTime != null)
			enrolment.setWorkTime(workTime + duration);
		else
			enrolment.setWorkTime(duration);

		//object.setIndicator(null);

		this.repository.save(object);
		this.repository.save(enrolment);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		int studentId;
		Collection<Enrolment> enrolments;
		SelectChoices choices;
		//SelectChoices indicators;
		Tuple tuple;

		studentId = super.getRequest().getPrincipal().getActiveRoleId();

		enrolments = this.repository.findFinalisedEnrolmentsByStudentId(studentId);
		choices = SelectChoices.from(enrolments, "code", object.getEnrolment());
		//indicators = SelectChoices.from(Indication.class, object.getIndicator());

		tuple = super.unbind(object, "title", "activityAbstract", "indicator", "periodStart", "periodEnd", "link");
		tuple.put("enrolment", choices.getSelected().getKey());
		tuple.put("enrolments", choices);
		//tuple.put("indicators", indicators);

		super.getResponse().setData(tuple);
	}

}
