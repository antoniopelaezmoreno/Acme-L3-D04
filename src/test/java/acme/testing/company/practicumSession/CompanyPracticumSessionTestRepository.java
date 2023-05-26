
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.repositories.AbstractRepository;

public interface CompanyPracticumSessionTestRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.company.userAccount.username = :u")
	Collection<Practicum> findPracticumsByUsername(String u);

	@Query("select ps from PracticumSession ps where ps.practicum.company.userAccount.username = :u")
	Collection<PracticumSession> findPracticumSessionsByUsername(String u);
}
