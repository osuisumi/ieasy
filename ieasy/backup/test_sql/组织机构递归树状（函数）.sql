DROP FUNCTION IF EXISTS getOrgTreeNode;
CREATE FUNCTION getOrgTreeNode(rootId VARCHAR(1000), notIn VARCHAR(1000))
	RETURNS varchar(1000)
	BEGIN
		DECLARE sTemp VARCHAR(1000);
	  	DECLARE sTempChd VARCHAR(1000);

	  	SET sTemp = '$';
	  	SET sTempChd =cast(rootId as CHAR);

	  	WHILE sTempChd is not null DO
	    	SET sTemp = concat(sTemp,',',sTempChd);
			
			IF(notIn='O') THEN
				SELECT group_concat(id) INTO sTempChd FROM ieasy_sys_org where FIND_IN_SET(pid,sTempChd)>0 AND type<>'O';
			ELSEIF(notIn='') THEN
				SELECT group_concat(id) INTO sTempChd FROM ieasy_sys_org where FIND_IN_SET(pid,sTempChd)>0;
			ELSE
				SELECT group_concat(id) INTO sTempChd FROM ieasy_sys_org where FIND_IN_SET(pid,sTempChd)>0;
			END IF;				

	  	END WHILE;
	  	RETURN sTemp;
END



-- 调用方式

select id,pid,name from ieasy_sys_org 
where FIND_IN_SET(id, getOrgTreeNode('4028848347614008014761419a050000'));

select id,pid,name from ieasy_sys_org 
where FIND_IN_SET(id, getOrgTreeNode('402884834761400801476142ee140001'));

-- 嵌套查询
select id,pid,name from ieasy_sys_org where id in( 
     select id from ieasy_sys_org where FIND_IN_SET(id, getOrgTreeNode('4028848347614008014761419a050000')) 
); 

select getOrgTreeNode('4028848347614008014761419a050000'); 


SELECT * FROM ieasy_sys_org o WHERE o.id IN(
	select id from ieasy_sys_org where FIND_IN_SET(id, getOrgTreeNode('4028848347614008014761419a050000','o')) AND pid IS NOT NULL
);









