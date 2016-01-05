@echo off

@echo ====================================================
@echo ※  文件：mysql数据库备份工具                     ※
@echo ※  版本：1.0                                     ※
@echo ※  作者：加州旅馆                                ※
@echo ※  时间：2008年12月                              ※
@echo ※  Q Q：416306292                                ※
@echo ====================================================

set remote_host=localhost
set remote_db=ieasy
set romote_user=root
set remote_passwd=yhqmcq

::根据当前的日期时间，生成备份的文件名。
@set time_=%time:~0,2%&call set time_=%%time_: =0%%
::设置时间点格式，当小时是1-9点时候，前面补零
@set FileName=%date:~0,10%【%time_%点%time:~3,-6%分】MysqlBak-ieasy.sql
::实例：2008-12-26【01点10分】MysqlBak-dbcrm.sql
::====================================================
::生成当月的文件夹名
@set DirName=%date:~0,7%月数据备份


::====================================================
::每月1号创建新的文件夹
@if %date:~8,2%==01 (md %DirName%)
::如果没有本月文件夹（初次运行时如果不是1号），则创建本月文件夹
@if not exist %DirName% (md %DirName%)


::====================================================
@echo         正在备份数据，请不要关闭！ 
@echo off 
mysqldump  -h%remote_host% -u%romote_user% -p%remote_passwd% %remote_db% > %cd%\%DirName%\%FileName%
::用mysqldump命令  root【用户名】 123【密码】 dbcrm【要备份的数据库名】 %cd%【备份文件所要放置的当::前路径】  %DirName%【以年月创建的存放数据文件的文件夹】  %FileName%【备份的数据文件】
@echo ====================================================
@echo ★      恭喜您！数据备份成功      ★
@echo ====================================================
pause
