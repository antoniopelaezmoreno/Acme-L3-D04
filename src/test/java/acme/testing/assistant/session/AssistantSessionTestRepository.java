
package acme.testing.assistant.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.session.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

public interface AssistantSessionTestRepository extends AbstractRepository {

	@Query("select s from Session s where s.tutorial.code = :code")
	Collection<Session> findManySessionsByTutorialCode(String code);

	@Query("select t from Tutorial t where t.assistant.userAccount.username = :userName")
	Collection<Tutorial> findManyTutorialsByUserName(String userName);
}
