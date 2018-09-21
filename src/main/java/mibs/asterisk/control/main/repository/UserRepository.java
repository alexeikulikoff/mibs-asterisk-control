package mibs.asterisk.control.main.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.main.entity.User;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

	User findByName(String name);

}
