
package acme.features.student.activity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activity.Activity;
import acme.entities.enrolment.Enrolment;
import acme.enums.Indication;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityUpdateService extends AbstractService<Student, Activity> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

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
		boolean finalised;
		int masterId;
		Enrolment enrolment;
		Student student;

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentByActivityId(masterId);

		if (enrolment != null) {
			student = enrolment.getStudent();
			finalised = enrolment.isFinalised();
			status = super.getRequest().getPrincipal().hasRole(student);
			super.getResponse().setAuthorised(status && finalised);
		} else
			super.getResponse().setAuthorised(false);

	}

	@Override
	public void load() {
		Activity object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneActivityById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		Integer enrolmentId;
		Enrolment enrolment;

		enrolmentId = super.getRequest().getData("enrolment", Integer.class);

		if (enrolmentId != null)
			enrolment = this.repository.findOneEnrolmentById(enrolmentId);
		else
			enrolment = null;

		super.bind(object, "title", "activityAbstract", "indicator", "periodStart", "periodEnd", "link");
		object.setEnrolment(enrolment);
	}

	@Override
	public void validate(final Activity object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("periodEnd"))
			super.state(MomentHelper.isAfter(object.getPeriodEnd(), object.getPeriodStart()), "periodEnd", "student.activity.form.error.periodEnd");
	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		Activity activity;
		Double oldDuration;
		Double newDuration;
		Double difference;
		Double workTime;
		Enrolment enrolment;
		Enrolment oldEnrolment;

		activity = this.repository.findOneActivityById(object.getId());
		enrolment = this.repository.findOneEnrolmentById(object.getEnrolment().getId());

		oldEnrolment = activity.getEnrolment();

		oldDuration = (double) MomentHelper.computeDuration(activity.getPeriodStart(), activity.getPeriodEnd()).toHours();
		newDuration = (double) MomentHelper.computeDuration(object.getPeriodStart(), object.getPeriodEnd()).toHours();

		//enrolment = object.getEnrolment();
		workTime = enrolment.getWorkTime();

		if (enrolment == oldEnrolment) {

			difference = newDuration - oldDuration;
			enrolment.setWorkTime(workTime + difference);

		} else {
			Double oldTime;
			oldTime = oldEnrolment.getWorkTime();
			oldEnrolment.setWorkTime(oldTime - oldDuration);

			if (workTime != null)
				enrolment.setWorkTime(workTime + newDuration);
			else
				enrolment.setWorkTime(newDuration);

			this.repository.save(oldEnrolment);

		}
		this.repository.save(enrolment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		int studentId;
		Collection<Enrolment> enrolments;
		SelectChoices choices;
		SelectChoices indicators;
		Tuple tuple;

		studentId = super.getRequest().getPrincipal().getActiveRoleId();

		enrolments = this.repository.findFinalisedEnrolmentsByStudentId(studentId);
		choices = SelectChoices.from(enrolments, "code", object.getEnrolment());
		indicators = SelectChoices.from(Indication.class, object.getIndicator());

		tuple = super.unbind(object, "title", "activityAbstract", "indicator", "periodStart", "periodEnd", "link");
		tuple.put("enrolment", choices.getSelected().getKey());
		tuple.put("enrolments", choices);
		tuple.put("indicators", indicators);

		super.getResponse().setData(tuple);

	}
}
