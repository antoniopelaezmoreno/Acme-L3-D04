
package acme.features.company.practicum;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.enums.Indication;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumPublishService extends AbstractService<Company, Practicum> {

	@Autowired
	protected CompanyPracticumRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Practicum object;
		int id;
		boolean status;
		Company company;

		company = this.repository.findCompanyByUserId(super.getRequest().getPrincipal().getAccountId());
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(id);
		status = object != null && super.getRequest().getPrincipal().hasRole(Company.class) && object.getCompany().getId() == company.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;
		final double totalTime;
		final Collection<PracticumSession> sessions;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(id);

		sessions = this.repository.findAllSessionByPracticumId(id);
		totalTime = sessions.stream().mapToDouble(x -> x.getPeriodEnd().getTime() - x.getPeriodStart().getTime()).sum();

		object.setEstimatedTotalTime(totalTime / (1000 * 60 * 60));

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		super.bind(object, "code", "title", "practicumAbstract", "goals", "estimatedTotalTime", "published");
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		final Collection<PracticumSession> sessions = this.repository.findAllSessionByPracticumId(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Optional<Practicum> optPracticum;

			optPracticum = this.repository.findPracticumByCode(object.getCode());
			super.state(!optPracticum.isPresent() || optPracticum.get().getId() == object.getId(), "code", "company.practicum.form.error.duplicated");

		}

		super.state(!sessions.isEmpty(), "*", "company.practicum.form.error.emptyPracticum");
		super.state(sessions.stream().map(PracticumSession::isPublished).allMatch(x -> x), "*", "company.practicum.form.error.allPublished");
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		final SelectChoices choice;
		final Collection<Course> courses;
		final Tuple tuple;

		courses = this.repository.findAllCourses().stream().filter(x -> x.getIndicator().equals(Indication.HANDS_ON)).collect(Collectors.toList());
		choice = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "title", "practicumAbstract", "goals", "estimatedTotalTime", "published", "company");
		tuple.put("courses", choice);
		tuple.put("company", object.getCompany().getName());

		super.getResponse().setData(tuple);
	}
}
