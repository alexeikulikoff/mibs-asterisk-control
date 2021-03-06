package mibs.asterisk.control.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import mibs.asterisk.control.entity.EquipmentsEntity;
import mibs.asterisk.control.entity.TemplateEntity;

@Transactional
public interface EquipmentsRepository extends CrudRepository<EquipmentsEntity, Long>{

	List<EquipmentsEntity> findAll();
	Optional<EquipmentsEntity> findById(Long id);
	List<EquipmentsEntity> findByP( Long p);
	EquipmentsEntity findByPhone(String phone);
	@Modifying 
	@Query("UPDATE EquipmentsEntity u SET u.phone= :phone, u.password=:password, u.office=:office, u.p=:p, u.ipaddress=:ipaddress, u.netmask=:netmask, u.gateway=:gateway, u.person=:person, u.recordIn=:recordIn, u.recordOut=:recordOut, u.external=:external, u.template=:template where u.id=:id")
	void updateEquipments(@Param("phone") String phone, @Param("password") String password, @Param("office") String office,
			@Param("p") Long p, @Param("ipaddress") String ipaddress, @Param("netmask") String netmask, @Param("gateway") String gateway,
			@Param("person") String person, @Param("recordIn") String recotdIn, @Param("recordOut") String recordOut, @Param("external") String external, @Param("template") TemplateEntity template,  @Param("id") Long id);

	
}
