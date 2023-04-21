
package acme.features.student.activity;

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

		super.bind(object, "title", "activityAbstract", "indicator", "periodStart", "periodEnd", "link");
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

		this.repository.save(object);
		this.repository.save(enrolment);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		final boolean finalised;
		SelectChoices indicators;
		Tuple tuple;

		indicators = SelectChoices.from(Indication.class, object.getIndicator());
		//finalised = this.repository.findOneEnrolmentById(object.getEnrolment().getId()).isFinalised();

		tuple = super.unbind(object, "title", "activityAbstract", "indicator", "periodStart", "periodEnd", "link");
		//tuple.put("readonly", !finalised);
		tuple.put("enrolment", object.getEnrolment().getCode());
		tuple.put("indicators", indicators);

		super.getResponse().setData(tuple);

	}
}
