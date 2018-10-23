package mibs.asterisk.control.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.UnitsEntity;

@Transactional
public interface UnitsRepository extends CrudRepository<UnitsEntity, Long>{

	List<UnitsEntity> findAll();
	List<UnitsEntity> findByQ(Long q);
	List<UnitsEntity> findByPbx(Long pbx);
	@Modifying 
	@Query("UPDATE UnitsEntity u SET u.unit = :unit  WHERE u.id = :p")
	void updateUnit(@Param("unit") String unit,  @Param("p") Long p);

}
