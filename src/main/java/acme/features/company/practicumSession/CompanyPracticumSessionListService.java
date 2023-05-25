
package acme.features.company.practicumSession;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionListService extends AbstractService<Company, PracticumSession> {

	@Autowired
	protected CompanyPracticumSessionRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Practicum object;
		int masterId;
		boolean status;
		Company company;

		company = this.repository.findCompanyByUserId(super.getRequest().getPrincipal().getAccountId());
		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findPracticumById(masterId);
		status = object != null && super.getRequest().getPrincipal().hasRole(Company.class) && object.getCompany().getId() == company.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<PracticumSession> objects;
		int masterId;
		boolean masterPublished;
		Practicum practicum;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findAllSessionByPracticumId(masterId);

		practicum = this.repository.findPracticumById(masterId);
		masterPublished = practicum.isPublished();

		super.getResponse().setGlobal("masterId", masterId);
		super.getResponse().setGlobal("masterPublished", masterPublished);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;
		Collection<PracticumSession> objects;
		final Practicum practicum;
		final boolean addendumAvailable;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findAllSessionByPracticumId(masterId);
		practicum = this.repository.findPracticumById(masterId);
		addendumAvailable = objects.stream().filter(PracticumSession::isAddendum).collect(Collectors.toList()).isEmpty();

		Tuple tuple;

		tuple = super.unbind(object, "title", "periodStart", "periodEnd", "published");
		super.getResponse().setGlobal("masterId", masterId);
		super.getResponse().setGlobal("addendumAvailable", addendumAvailable);
		super.getResponse().setGlobal("publishedPracticum", practicum.isPublished());
		super.getResponse().setData(tuple);
	}
}
