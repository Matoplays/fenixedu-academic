create table `STUDENT_DATA_SHARE_AUTHORIZATION` (`OID` bigint unsigned, `SINCE` timestamp NULL default NULL, `OID_STUDENT` bigint unsigned, `AUTHORIZATION_CHOICE` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_STUDENT)) type=InnoDB, character set latin1;

insert into FF$DOMAIN_CLASS_INFO(DOMAIN_CLASS_NAME, DOMAIN_CLASS_ID)
select 'net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization', max(DOMAIN_CLASS_ID) + 1
from FF$DOMAIN_CLASS_INFO;

insert into STUDENT_DATA_SHARE_AUTHORIZATION(SINCE, OID_STUDENT, AUTHORIZATION_CHOICE)
select EXECUTION_INTERVAL.BEGIN_DATE_YEAR_MONTH_DAY, STUDENT_DATA_BY_EXECUTION_YEAR.OID_STUDENT, STUDENT_DATA_BY_EXECUTION_YEAR.PERSONAL_DATA_AUTHORIZATION
from STUDENT_DATA_BY_EXECUTION_YEAR, EXECUTION_INTERVAL
where STUDENT_DATA_BY_EXECUTION_YEAR.OID_EXECUTION_YEAR = EXECUTION_INTERVAL.OID;

select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization';
update STUDENT_DATA_SHARE_AUTHORIZATION set OID = (@xpto << 32) + ID_INTERNAL;