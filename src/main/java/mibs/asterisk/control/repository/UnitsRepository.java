package mibs.asterisk.control.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.UnitsEntity;

@Transactional
public interface UnitsRepository extends CrudRepository<UnitsEntity, Long>{

	List<UnitsEntity> findAll();
<<<<<<< HEAD
	
	List<UnitsEntity> findByQ(Long q);
	
=======
	List<UnitsEntity> findByQ(Long q);
>>>>>>> origin
}
