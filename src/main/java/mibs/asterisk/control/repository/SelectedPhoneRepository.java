package mibs.asterisk.control.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mibs.asterisk.control.entity.SelectedPhoneEntity;

public interface SelectedPhoneRepository extends CrudRepository<SelectedPhoneEntity, Long>{
	List<SelectedPhoneEntity> findAll();
}
