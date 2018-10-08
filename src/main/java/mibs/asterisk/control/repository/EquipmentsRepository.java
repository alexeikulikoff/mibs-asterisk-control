package mibs.asterisk.control.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.EquipmentsEntity;

@Transactional
public interface EquipmentsRepository extends CrudRepository<EquipmentsEntity, Long>{

	List<EquipmentsEntity> findAll();
	Optional<EquipmentsEntity> findById(Long id);
	List<EquipmentsEntity> findByP( Long p);
	EquipmentsEntity findByPhone(String phone);
	
	
}
