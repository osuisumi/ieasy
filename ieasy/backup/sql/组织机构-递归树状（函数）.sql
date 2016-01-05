DROP FUNCTION IF EXISTS queryOrgChildren;

CREATE FUNCTION queryOrgChildren (areaId VARCHAR(1000))
	RETURNS VARCHAR(4000)
	BEGIN
	DECLARE sTemp VARCHAR(4000);
	DECLARE sTempChd VARCHAR(4000);
	
	SET sTemp = '$';
	SET sTempChd = cast(areaId as char);
	
	WHILE sTempChd is not NULL DO
	SET sTemp = CONCAT(sTemp,',',sTempChd);
	SELECT group_concat(id) INTO sTempChd FROM ieasy_sys_org where FIND_IN_SET(pid,sTempChd)>0;
	END WHILE;
	return sTemp;
END;

-- select queryOrgChildren(1);
-- select * from ieasy_sys_org where FIND_IN_SET(id, queryOrgChildren('402881e5491eceda01491ecf08f600a1')); 