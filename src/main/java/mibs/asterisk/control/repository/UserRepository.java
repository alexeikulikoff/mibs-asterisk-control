package mibs.asterisk.control.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.User;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

	User findByName(String name);

}
