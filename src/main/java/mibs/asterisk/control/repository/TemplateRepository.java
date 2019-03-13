package mibs.asterisk.control.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.TemplateEntity;
@Transactional
public interface TemplateRepository  extends CrudRepository<TemplateEntity, Long>{

	TemplateEntity findByName(String name);
	List<TemplateEntity> findAll();
	@Modifying 
	@Query("UPDATE TemplateEntity u SET u.name = :name WHERE u.id = :id")
	void updateTemplate(@Param("name") String name, @Param("id") Long id);
}
