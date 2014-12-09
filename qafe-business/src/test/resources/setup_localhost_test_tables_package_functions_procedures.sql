-- clear existing tables/functions/procedures and packages --

drop table emp;
drop table dept;
drop table QAFE_TEST_SELECTDB;
drop table QAFE_TEST_DELETE;
drop table QAFE_TEST_INSERT;
drop table QAFE_TEST_UPDATE;
drop PACKAGE QPD_TEST_PCK;
drop procedure qpd_prc_update_emp_sal;
drop procedure qpd_prc_get_ename;
drop function qpd_fnc_get_hello;
drop function qpd_fnc_get_ename;

-- Create tables for function/procedure calls and insert some test data --

CREATE TABLE "HDEMO65"."DEPT"
  (
    "DEPTNO" NUMBER(2,0),
    "DNAME"  VARCHAR2(14 BYTE),
    "LOC"    VARCHAR2(13 BYTE),
    PRIMARY KEY ("DEPTNO") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "SYSTEM" ENABLE
  )
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT
  )
  TABLESPACE "SYSTEM" ;
  
commit;
  
CREATE TABLE "HDEMO65"."EMP"
  (
    "EMPNO" NUMBER(4,0) NOT NULL ENABLE,
    "ENAME" VARCHAR2(10 BYTE),
    "JOB"   VARCHAR2(9 BYTE),
    "MGR"   NUMBER(4,0),
    "HIREDATE" DATE,
    "SAL"    NUMBER(7,2),
    "COMM"   NUMBER(7,2),
    "DEPTNO" NUMBER(2,0),
    PRIMARY KEY ("EMPNO") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "SYSTEM" ENABLE,
    FOREIGN KEY ("MGR") REFERENCES "HDEMO65"."EMP" ("EMPNO") ENABLE,
    FOREIGN KEY ("DEPTNO") REFERENCES "HDEMO65"."DEPT" ("DEPTNO") ENABLE
  )
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT
  )
  TABLESPACE "SYSTEM" ;

commit;

Insert into DEPT (DEPTNO,DNAME,LOC) values (10,'ACCOUNTING','NEW YORK');
Insert into DEPT (DEPTNO,DNAME,LOC) values (20,'RESEARCH','DALLAS');
Insert into DEPT (DEPTNO,DNAME,LOC) values (30,'SALES','CHICAGO');
Insert into DEPT (DEPTNO,DNAME,LOC) values (40,'OPERATIONS','BOSTON');

commit;

Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7839,'KING','PRESIDENT',null,to_timestamp('17-11-81','DD-MM-RR HH24:MI:SSXFF'),-99000,null,10);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7698,'BLAKE','MANAGER',7839,to_timestamp('01-05-81','DD-MM-RR HH24:MI:SSXFF'),-56430,null,30);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7782,'CLARK','MANAGER',7839,to_timestamp('09-06-81','DD-MM-RR HH24:MI:SSXFF'),-48510,null,10);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7566,'JONES','MANAGER',7839,to_timestamp('02-04-81','DD-MM-RR HH24:MI:SSXFF'),-58905,null,20);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7788,'SCOTT','ANALYST',7566,to_timestamp('09-12-82','DD-MM-RR HH24:MI:SSXFF'),-59400,null,20);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7902,'FORD','ANALYST',7566,to_timestamp('03-12-81','DD-MM-RR HH24:MI:SSXFF'),-59400,null,20);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7369,'SMITH','CLERK',7902,to_timestamp('17-12-80','DD-MM-RR HH24:MI:SSXFF'),-15840,null,20);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7499,'ALLEN','SALESMAN',7698,to_timestamp('20-02-81','DD-MM-RR HH24:MI:SSXFF'),-31680,300,30);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7521,'WARD','SALESMAN',7698,to_timestamp('22-02-81','DD-MM-RR HH24:MI:SSXFF'),-24750,500,30);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7654,'MARTIN','SALESMAN',7698,to_timestamp('28-09-81','DD-MM-RR HH24:MI:SSXFF'),-24750,1400,30);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7844,'TURNER','SALESMAN',7698,to_timestamp('08-09-81','DD-MM-RR HH24:MI:SSXFF'),-29700,0,30);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7876,'ADAMS','CLERK',7788,to_timestamp('12-01-83','DD-MM-RR HH24:MI:SSXFF'),-21780,null,20);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7900,'JAMES','CLERK',7698,to_timestamp('03-12-81','DD-MM-RR HH24:MI:SSXFF'),-18810,null,30);
Insert into EMP (EMPNO,ENAME,JOB,MGR,HIREDATE,SAL,COMM,DEPTNO) values (7934,'MILLER','CLERK',7782,to_timestamp('23-01-82','DD-MM-RR HH24:MI:SSXFF'),-25740,null,10);

commit;

-- create table for tests <delete> --

CREATE TABLE "HDEMO65"."QAFE_TEST_DELETE"
  (
    "EMP_ID"   VARCHAR2(20 BYTE) NOT NULL ENABLE,
    "EMP_NAME" VARCHAR2(100 BYTE),
    "DEP_ID"   VARCHAR2(20 BYTE)
  )
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT
  )
  TABLESPACE "USERS" ;
  
-- create table for tests <insert> --

CREATE TABLE "HDEMO65"."QAFE_TEST_INSERT"
  (
    "EMP_ID"   VARCHAR2(20 BYTE) NOT NULL ENABLE,
    "EMP_NAME" VARCHAR2(100 BYTE),
    "DEP_ID"   VARCHAR2(20 BYTE),
    "SALARY"  NUMBER,
    "ENTRY_DATE" DATE,
    CONSTRAINT "QAFE_TEST_INSERT_PK" PRIMARY KEY ("EMP_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "SYSTEM" ENABLE
  )
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT
  )
  TABLESPACE "SYSTEM" ;  
  
-- create table for tests <select> --

CREATE TABLE "HDEMO65"."QAFE_TEST_SELECTDB"
  (
    "EMP_ID"   VARCHAR2(20 BYTE) NOT NULL ENABLE,
    "EMP_NAME" VARCHAR2(100 BYTE),
    "DEP_ID"   VARCHAR2(20 BYTE),
    "BIRTHDATE" DATE,
    "CHILDREN" NUMBER(10,0)
  )
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT
  )
  TABLESPACE "USERS" ;
  
-- create table for tests <update> --

CREATE TABLE "HDEMO65"."QAFE_TEST_UPDATE"
  (
    "ID"      NUMBER NOT NULL ENABLE,
    "NAME"    VARCHAR2(50 BYTE),
    "ADDRESS" VARCHAR2(50 BYTE),
    "EMAIL"   VARCHAR2(50 BYTE),
    "SALARY"  NUMBER,
    "ENTRY_DATE" DATE,
    CONSTRAINT "QAFE_TEST_UPDATE_PK" PRIMARY KEY ("ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "SYSTEM" ENABLE
  )
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT
  )
  TABLESPACE "SYSTEM" ;

commit;

-- create package --

create or replace
PACKAGE QPD_TEST_PCK AS 
  procedure update_emp_sal(p_percentage in number);
  
  procedure get_ename_prc(p_empno in emp.empno%type
                         ,p_ename out emp.ename%type
                         );
  function get_ename_fnc(p_empno in emp.empno%type)
  return emp.ename%type;
  
END QPD_TEST_PCK;
/

create or replace
PACKAGE body QPD_TEST_PCK AS 
  cursor c_emp(b_empno in emp.empno%type) is
  select emp.empno
  ,      emp.comm 
  ,      emp.deptno 
  ,      emp.ename 
  ,      emp.hiredate 
  ,      emp.job 
  ,      emp.mgr 
  ,      emp.sal
  from emp
  where emp.empno = b_empno;
  --
  r_emp    c_emp%rowtype;
  --
  procedure update_emp_sal(p_percentage in number)
  is
  begin
    update emp
    set sal = sal + p_percentage*sal;
  end update_emp_sal;
  
  procedure get_ename_prc(p_empno in emp.empno%type
                         ,p_ename out emp.ename%type
                         )
  is
  begin
    open c_emp(b_empno => p_empno);
    fetch c_emp into r_emp;
    close c_emp;
    p_ename := r_emp.ename;
  end get_ename_prc;
                         
  function get_ename_fnc(p_empno in emp.empno%type)
  return emp.ename%type
  is
  begin
    open c_emp(b_empno => p_empno);
    fetch c_emp into r_emp;
    close c_emp;
    return r_emp.ename;
  end get_ename_fnc;
  
END QPD_TEST_PCK;
/

commit;

-- create functions --

create or replace
function qpd_fnc_get_hello
return varchar2
is
begin
    return 'hello';
end qpd_fnc_get_hello;
/

create or replace
function qpd_fnc_get_ename(p_empno in emp.empno%type)
return emp.ename%type
is
begin
    return qpd_test_pck.get_ename_fnc(p_empno);
end qpd_fnc_get_ename;
/

-- create procedures --

create or replace
procedure qpd_prc_get_ename(p_empno in emp.empno%type
                                             ,p_ename out emp.ename%type
                                            )
is
begin
  qpd_test_pck.get_ename_prc(p_empno  => p_empno
                            ,p_ename  => p_ename
                            );
end qpd_prc_get_ename;
/

create or replace
procedure qpd_prc_update_emp_sal(p_percentage in number)
is
begin
  qpd_test_pck.update_emp_sal  (p_percentage => p_percentage);
end qpd_prc_update_emp_sal;
/

create or replace TYPE lne_typ AS OBJECT 
( line_id		  NUMBER(38)
, header_id		  NUMBER(38)
, project_id		  NUMBER(38,0)
, project_name            VARCHAR(30)
, start_date         DATE
 );
 
 /
 
 create or replace TYPE hdr_typ AS OBJECT 
( header_id		  NUMBER(38)
, person_id		  NUMBER(38)
, datum		          DATE
, STATUS            	  CHAR(16)
);

/
 
 create or replace PACKAGE QAFE_COMPLEX_DATA AS 

PROCEDURE GetValuesObj (p_obj_lne out lne_typ);
PROCEDURE init_lne_obj (p_obj_lne out lne_typ);
PROCEDURE PutValuesObj (p_obj_lne1 in lne_typ, p_obj_lne2 out lne_typ);

PROCEDURE modifyObject (p_obj_lne1 in out lne_typ);
PROCEDURE modifyTwoObjects (p_obj_lne in out lne_typ,p_obj_hdr in out hdr_typ);

END QAFE_COMPLEX_DATA;

/

create or replace PACKAGE BODY QAFE_COMPLEX_DATA AS
 
  PROCEDURE GetValuesObj (p_obj_lne out lne_typ)
 AS
  BEGIN
    init_lne_obj (p_obj_lne);    
  END GetValuesObj;
  
  
  PROCEDURE init_lne_obj (p_obj_lne out lne_typ)
  AS
  BEGIN
    p_obj_lne := lne_typ (5001, 2456058, 10189666, 'KPN', sysdate); 
  END init_lne_obj;
 
 PROCEDURE PutValuesObj (p_obj_lne1 in lne_typ, p_obj_lne2 out lne_typ)
  AS
  BEGIN
    p_obj_lne2 := p_obj_lne1;   
    p_obj_lne2.project_id := p_obj_lne1.project_id + 1;
	  p_obj_lne2.start_date := p_obj_lne1.start_date + 1;	
		
  END PutValuesObj;
  
  PROCEDURE modifyObject (p_obj_lne1 in out lne_typ)
   AS
  BEGIN
    p_obj_lne1.project_id := p_obj_lne1.project_id + 1;
	  p_obj_lne1.start_date := p_obj_lne1.start_date + 10;	
		
  END modifyObject;
  
  PROCEDURE modifyTwoObjects (p_obj_lne in out lne_typ,p_obj_hdr in out hdr_typ)AS
  BEGIN
    p_obj_lne.project_id := p_obj_lne.project_id + 1;
	  p_obj_lne.start_date := p_obj_lne.start_date + 10;	
		p_obj_hdr.person_id := p_obj_hdr.person_id + 1;
    p_obj_hdr.STATUS := 'B';    
  END modifyTwoObjects;
 

END QAFE_COMPLEX_DATA; 

/

--------------------------------------------------------
--  DDL for Package QAFE_TESTS
--------------------------------------------------------
CREATE OR REPLACE PACKAGE QAFE_TESTS AS 
   procedure say_hello_prc(p_name in VARCHAR2, p_result out VARCHAR2);
   function say_hello_fnc(p_name in VARCHAR2) return VARCHAR2;
END QAFE_TESTS;
/
CREATE OR REPLACE PACKAGE BODY QAFE_TESTS AS

  procedure say_hello_prc(p_name in VARCHAR2, p_result out VARCHAR2) AS  
    curr_schema VARCHAR(2000);
  BEGIN
    select sys_context('userenv', 'current_schema')
    into curr_schema
    from dual;
    
    p_result := curr_schema || '-PROC: ' || 'Hello ' || p_name;
  END say_hello_prc;
  
  function say_hello_fnc(p_name in VARCHAR2) return VARCHAR2 AS  
    curr_schema VARCHAR(2000);
  BEGIN
    select sys_context('userenv', 'current_schema')
    into curr_schema
    from dual;
    
    RETURN curr_schema || '-FUNC: ' || 'Hello ' || p_name;
  END say_hello_fnc;

END QAFE_TESTS;

/

 
 
