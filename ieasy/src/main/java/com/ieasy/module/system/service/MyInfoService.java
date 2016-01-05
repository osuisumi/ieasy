package com.ieasy.module.system.service;


import java.io.File;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.ConfigUtil;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.OrgEntity;
import com.ieasy.module.system.entity.PersonEntity;
import com.ieasy.module.system.entity.PositionEntity;
import com.ieasy.module.system.entity.UserEntity;
import com.ieasy.module.system.web.form.PersonForm;
import com.ieasy.module.system.web.form.UserForm;

@Service @Transactional
public class MyInfoService implements IMyInfoService {
	
	private Log log = LogFactory.getLog(MyInfoService.class) ;
	
	@Inject
	private IBaseDao<OrgEntity> basedaoOrg ;

	@Inject
	private IBaseDao<PositionEntity> basedaoPos ;
	
	@Inject
	private IBaseDao<PersonEntity> basedaoEmp ;
	
	@Inject
	private IBaseDao<UserEntity> basedaoUser ;

	@Override
	public PersonForm getMyInfo(PersonForm form) {
		try {
			String sql = "select " +
						 "u.id user_id, u.account user_account, " +
						 "e.id, e.name, e.num, e.sex, e.birth, e.zjlx, e.zjhm, "+
						 "e.salNum, e.ssNum, e.place, e.stirps, e.country, e.marriage, " +
						 "e.political, e.health, e.highest, e.profession, e.posTitle, e.school, " +
						 "e.cjgzrq, e.kxgl, e.height, e.weight, e.hkxz, e.hkdz, " +
						 "e.empState, e.empType, e.empLevel, e.enterDate, e.changeDate, " +
						 "e.mobile, e.phone, e.email, e.address, e.zipCode, " +
						 "e.archivesStatus, e.txPicPath, e.remark, " +
						 "e.created, e.modifyDate, " +
						 "o.id as org_id, o.name as org_name, " +
						 "position.id as position_id " +
						 "from ieasy_sys_user u " +
					 	 "left join ieasy_sys_person e ON(e.id=u.emp_id) " +
					 	 "left join ieasy_sys_org o ON(o.id=e.org_id) " + 
					 	 "left join ieasy_sys_position position ON(position.id=e.position_id) " + 
					 	 "where u.id='"+form.getUser_id()+"'";
			
			form = (PersonForm)this.basedaoEmp.queryObjectSQL(sql, PersonForm.class, false) ;
			
			return form ;
		} catch (Exception e) {
			e.printStackTrace() ;
			log.error("个人信息-加载个人信息失败===>异常信息：", e) ;
			throw new ServiceException("加载信息异常：", e) ;
		}
	}

	@Override
	public Msg updateMyInfo(PersonForm form) {
		try {
			PersonEntity entity = this.basedaoEmp.load(PersonEntity.class, form.getId()) ;
			
			if((null == form.getTxPicPath() || "".equals(form.getTxPicPath())) && (null != entity.getTxPicPath() && !"".equals(entity.getTxPicPath()))) {
				com.ieasy.basic.util.file.FileUtils.deleteFile(WebContextUtil.getRealPath(entity.getTxPicPath())) ;
			}
			//如果两个头像的名称不相等,则说明已修改头像
			if(null != form.getTxPicPath() && !form.getTxPicPath().equals(entity.getTxPicPath())) {
				if(null != entity.getTxPicPath() && !"".equals(entity.getTxPicPath().trim()))
					com.ieasy.basic.util.file.FileUtils.deleteFile(WebContextUtil.getRealPath(entity.getTxPicPath())) ;
				
				String tempTxpath = WebContextUtil.getRealPath(form.getTxPicPath()) ;
				String toPath = WebContextUtil.getRealPath(WebContextUtil.getUploadDir()+File.separator+ConfigUtil.get("emp_tx_path"))+File.separator+FilenameUtils.getName(form.getTxPicPath()) ;
				FileUtils.copyFile(new File(tempTxpath), new File(toPath)) ;
				entity.setTxPicPath(WebContextUtil.getUploadDir()+File.separator+ConfigUtil.get("emp_tx_path")+File.separator+FilenameUtils.getName(form.getTxPicPath())) ;
			}
			
			BeanUtils.copyNotNullProperties(form, entity, new String[]{
					"num", "salNum", "ssNum", "remark", "txPicPath", "dimissionDate", "archivesStatus",
					"dbmType", "lbmType", "dbmDate", "lbmDate", "byProjectWorkStatus", "createName", "created"}) ;
			
			if(null == form.getTxPicPath() || "".equals(form.getTxPicPath())) entity.setTxPicPath(null) ;
			entity.setModifyDate(new Date()) ;
			
			this.basedaoEmp.update(entity) ;
			
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace() ;
			log.error("修改失败===>异常信息：", e) ;
			return new Msg(false, "修改失败！") ;
		}
	}

	@Override
	public Msg modifyMyPwf(UserForm form) {
		UserEntity entity = this.basedaoUser.load(UserEntity.class, form.getId()) ;
		
		if(entity != null) {
			if(form.getOldPwd().equals(entity.getPassword())) {
				entity.setPassword((null == form.getPassword() || "".equals(form.getPassword().trim())?"":form.getPassword())) ;
				return new Msg(true, "密码修改成功，重新登录生效。") ;
			} else {
				return new Msg(false, "原密码不正确，请重新输入！") ;
			}
		} else {
			return new Msg(false, "程序错误！") ;
		}
	}

}
