package mibs.asterisk.control.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import mibs.asterisk.control.entity.CustomersEntity;

@Transactional
public interface CustomersRepository  extends CrudRepository<CustomersEntity, Long>{
	
	List<CustomersEntity> findAll();
	CustomersEntity findByid(Long id);
	@Modifying 
	@Query("UPDATE CustomersEntity c SET c.phone = :phone, c.name = :name  WHERE c.id = :id")
	void updateCustomers(@Param("phone") String phone, @Param("name") String name,  @Param("id") Long id);
 	
}
