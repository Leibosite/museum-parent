drop table if exists 1task;
drop table if exists 1user;

create table 1task (
	id bigint auto_increment,
	title varchar(128) not null,
	description varchar(255),
	user_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table 1user (
	id bigint auto_increment,
	login_name varchar(64) not null unique,
	name varchar(64) not null,
	password varchar(255) not null,
	salt varchar(64) not null,
	roles varchar(255) not null,
	register_date timestamp not null default 0,
	primary key (id)
) engine=InnoDB;

create table alarm (
	id bigint auto_increment,
	name varchar(64) not null unique,
	desp varchar(64) not null,
    stamp varchar(255) not null,
	status varchar(64) not null,
	priority varchar(255),
	alarm_type varchar(255),
	alarm_handling_tips varchar(255),
	alarm_details varchar(255),
	primary key (ID)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
create table TBL_POLICY_GROUP (
	ID bigint auto_increment,
	NAME varchar(64) not null unique,
	DESP varchar(64) not null,
    STAMP varchar(255) not null,
	STATUS varchar(64) not null,
	PRIORITY varchar(255),
	primary key (ID)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
create table TBL_POLICY (
    ID bigint auto_increment,
	POLICY_GROUP_ID bigint,
	NAME varchar(64) not null unique,
	DESP varchar(64) not null,
    STAMP varchar(255) not null,
	STATUS varchar(64) not null,
	PRIORITY varchar(255),
	CONDITION_PROVINCE varchar(255),
	CONDITION_STATE varchar(255),
	CONDITION_SEASON varchar(255),
	CONDITION_TIMEZONE varchar(255),
	CONDITION_TIME varchar(255),
	CONDITION_HALL_ID varchar(255),
	CONDITION_CASE_ID varchar(255),
	CONDITION_POSITION varchar(255),
	CONDITION_ARTEFACTS_CATEGORY varchar(255),
	CONDITION_ARTEFACTS_PRIORITY varchar(255),
	CONDITION_TEMPERATURE varchar(255),
	CONDITION_HUMIDITY varchar(255),
	CONDITION_POWER varchar(255),
	CONDITION_RLATIVE_TEMPERATURE varchar(255),
	CONDITION_RELATIVE_HUMIDITY varchar(255),
	CONDITION_LIGHTING varchar(255),
	CONDITION_UV_INTENSITY varchar(255),
	CONDITION_SULFUR_DIOXIDE varchar(255),
	CONDITION_VOC varchar(255),
	CONDITION_PM varchar(255),
	CONDITION_FIREALARM varchar(255),
	CONDITION_WATERALARM varchar(255),
	CONDITION_SMOKE varchar(255),
	ACTION_PRIORITY varchar(255),
	ACTION_VALUE varchar(255),
	primary key (ID),
	key(POLICY_GROUP_ID)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;