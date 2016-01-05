
-- 复制infos库下的infox_sysmgr_emp表数据到ieasy_sys_person表中
insert into ieasy_sys_person
(
	num
)
select (
	id
)
from infos.infox_sysmgr_emp where id<>'9999';


--复制项目表数据
INSERT INTO ieasy_oa_project_center
(
	id, proj_num, proj_name, proj_start_time, proj_end_time, proj_status,
	
	proj_quot, proj_shouzhu, proj_zyfw, proj_gm, proj_buglv, proj_bjzry, proj_yjtrzry, 
	proj_bjscx, proj_clrl, proj_cclrl, proj_ydscx, proj_htpjzt, proj_cwjszt, proj_cwydwcjssj, proj_gjjb, proj_xmjsnf, proj_cwwcjssj, proj_jxzt
	
) 
SELECT 
	id, projectNum, name, startDate, endDate, 
	case status 
			WHEN 1 THEN 2
			WHEN 2 THEN 3
			WHEN 3 THEN 4
			ELSE 0 END,
	quot, shouzhu, taskScope, project_gm, project_buglv, project_bjzry, project_yjtrzry, project_bjscx,
	project_clrl, project_cclrl, project_ydscx, xmpjzt, jiesuan, cwydwcjssj, gjjb, xmjsnf, cwwcjssj, jxzt

	
FROM infos.infox_project_project i WHERE `status`<>5 ;

update ieasy_oa_project_center set 
	proj_budget_amount1=0,
	proj_budget_amount2=0,
	proj_budget_amount3=0,
	proj_budget_amount4=0,
	proj_budget_amount5=0,
	proj_budget_amount6=0,
	proj_budget_amount7=0,
	proj_budget_amount8=0,
	proj_budget_amount9=0 ;





SELECT 
t.id, t.projectNum, t.name,t.startDate, t.endDate, t.`status`, t.quot, t.gjjb, t.createrId, t.createrName,
e.id, e.truename,
o.fullname

FROM `infox_project_project` t 
LEFT JOIN infox_sysmgr_emp e ON(e.id=t.EMP_ID)
LEFT JOIN infox_sysmgr_org_dept o ON(o.id=t.DEPT_PID)
WHERE t.`status`<>5
;






SELECT
infox_sysmgr_emp.id,
infox_sysmgr_emp.bysj,
infox_sysmgr_emp.dbmDate,
infox_sysmgr_emp.dbmType,
infox_sysmgr_emp.email,
infox_sysmgr_emp.lbmDate,
infox_sysmgr_emp.lbmType,
infox_sysmgr_emp.rzsj,
infox_sysmgr_emp.sex,
infox_sysmgr_emp.truename,
infox_sysmgr_emp.ORG_PID
FROM `infox_sysmgr_emp`


SELECT
infox_project_project.id,
infox_project_project.endDate,
infox_project_project.`name`,
infox_project_project.projectNum,
infox_project_project.quot,
infox_project_project.startDate,
infox_project_project.`status`,
infox_project_project.DEPT_PID,
infox_project_project.EMP_ID,
infox_sysmgr_emp.truename,
infox_sysmgr_org_dept.fullname,
infox_project_project.createrId,
infox_project_project.createrName
FROM
infox_project_project
LEFT JOIN infox_sysmgr_emp ON infox_project_project.EMP_ID = infox_sysmgr_emp.id
LEFT JOIN infox_sysmgr_org_dept ON infox_project_project.DEPT_PID = infox_sysmgr_org_dept.id
WHERE
infox_project_project.`status` <> 5
ORDER BY
infox_sysmgr_emp.truename ASC




