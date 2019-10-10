CREATE DEFINER=`asterisk-admin`@`172.16.30.%` TRIGGER consolidate_update AFTER INSERT ON queue_log
  
   FOR EACH ROW
   BEGIN
      DECLARE cid int(11);
	  DECLARE i int(11);
	  DECLARE a int(11);
	  DECLARE c int(11);
	  DECLARE e int(11);
      DECLARE now DATETIME;
      
      SET now =  DATE_FORMAT(NOW(),'%Y-%m-%d %H:00:00');
	  
      select count(id) into cid from consolidate where calldate = now and queue = NEW.queuename ;
	  
      IF (cid = 0) THEN 
           insert into consolidate(calldate,queue,eneter,connect,abandon) values ( now, NEW.queuename, 0,0,0);
	  END IF;  
      
	  IF(NEW.event='ABANDON') AND (NEW.agent='NONE') THEN
           select id, abandon into i, a from consolidate where queue = NEW.queuename and calldate =now;         
           set a = a + 1;
           update consolidate set abandon = a where id = i;
	 
      ELSEIF(NEW.event='CONNECT') THEN
          select id, connect into i, c from consolidate where queue = NEW.queuename and calldate = now;         
          set c = c + 1;
		  update consolidate set connect = c where id = i;
     
      ELSEIF(NEW.event='ENTERQUEUE') AND (NEW.agent='NONE') THEN
          select id, eneter into i, e from consolidate where queue = NEW.queuename and calldate = now;         
          set e = e + 1;
		  update consolidate set eneter = e where id = i;
      END IF;

  END
