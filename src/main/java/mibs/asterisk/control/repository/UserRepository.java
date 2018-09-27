package mibs.asterisk.control.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.UserEntity;

@Transactional
public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByName(String name);
	
	List<UserEntity> findAll();

}
