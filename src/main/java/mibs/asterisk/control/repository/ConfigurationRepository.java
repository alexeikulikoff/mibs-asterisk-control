package mibs.asterisk.control.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.ConfigurationEntity;

@Transactional
public interface ConfigurationRepository extends CrudRepository<ConfigurationEntity, Long>{

	List<ConfigurationEntity> findAll();
	Optional<ConfigurationEntity> findById(Long id);
	@Modifying 
	@Query("UPDATE ConfigurationEntity u SET u.astname = :astname, u.dbhost = :dbhost, u.dbname = :dbname, u.dbuser = :dbuser, u.dbpassword = :dbpassword, u.sshlogin = :sshlogin, u.sshpassword =:sshpassword, u.asthost=:asthost, u.astuser=:astuser, u.astpassword =:astpassword  WHERE u.id = :id")
	void updateConfiguration(@Param("astname") String astname, @Param("dbhost") String dbhost, @Param("dbname") String dbname, @Param("dbuser") String dbuser, @Param("dbpassword") String dbpassword, @Param("sshlogin") String sshlogin, @Param("sshpassword") String sshpassword, @Param("asthost") String asthost, @Param("astuser") String astuser, @Param("astpassword") String astpassword, @Param("id") Long id);
}
