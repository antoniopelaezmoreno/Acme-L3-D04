
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

public interface AssistantTutorialTestRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.assistant.userAccount.username = :userName")
	Collection<Tutorial> findManyTutorialsByUserName(String userName);

	@Query("select t from Tutorial t where t.code = :code")
	Tutorial findTutorialByCode(String code);

}
