package mibs.asterisk.control.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.UserEntity;

@Transactional
public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByName(String name);
	List<UserEntity> findAll();
	@Modifying 
	@Query("UPDATE UserEntity u SET u.name = :name, u.password = :password, u.role = :role  WHERE u.id = :id")
	void updateUser(@Param("name") String name, @Param("password") String password, @Param("role") String role, @Param("id") Long id);

}
