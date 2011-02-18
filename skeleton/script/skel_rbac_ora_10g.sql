/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2011/2/18 15:18:23                           */
/*==============================================================*/


alter table "rbac_catalog_operatives_action"
   drop constraint FK_RBAC_CAT_REFERENCE_RBAC_CAT;

alter table "rbac_catalog_operatives_action"
   drop constraint FK_RBAC_CAT_REFERENCE_RBAC_ACT;

alter table "rbac_catalog_operatives_action"
   drop constraint FK_RBAC_CAT_REFERENCE_RBAC_OPE;

alter table "rbac_groups_roles"
   drop constraint FK_RBAC_GRO_REFERENCE_RBAC_ROL;

alter table "rbac_groups_roles"
   drop constraint FK_RBAC_GRO_REFERENCE_RBAC_GRO;

alter table "rbac_roles_catalogs_operatives"
   drop constraint FK_RBAC_ROL_REFERENCE_RBAC_OPE;

alter table "rbac_roles_catalogs_operatives"
   drop constraint FK_RBAC_ROL_REFERENCE_RBAC_ROL;

alter table "rbac_roles_catalogs_operatives"
   drop constraint FK_RBAC_ROL_REFERENCE_RBAC_CAT;

alter table "rbac_users_groups"
   drop constraint FK_RBAC_GROUP;

alter table "rbac_users_groups"
   drop constraint FK_RBAC_USER;

drop table "rbac_actions" cascade constraints;

drop table "rbac_auditing" cascade constraints;

drop table "rbac_catalog_operatives_action" cascade constraints;

drop table "rbac_catalogs" cascade constraints;

drop table "rbac_groups" cascade constraints;

drop table "rbac_groups_roles" cascade constraints;

drop table "rbac_operatives" cascade constraints;

drop table "rbac_roles" cascade constraints;

drop table "rbac_roles_catalogs_operatives" cascade constraints;

drop table "rbac_users" cascade constraints;

drop table "rbac_users_groups" cascade constraints;

/*==============================================================*/
/* Table: "rbac_actions"                                        */
/*==============================================================*/
create table "rbac_actions"  (
   "id"                 char(32)                        not null,
   "name"               varchar(128),
   "action"             varchar(256),
   "namespace"          varchar(256),
   constraint PK_RBAC_ACTIONS primary key ("id")
);

/*==============================================================*/
/* Table: "rbac_auditing"                                       */
/*==============================================================*/
create table "rbac_auditing"  (
   "id"                 char(32)                        not null,
   "user_id"            char(32),
   "user_name"          varchar(64),
   "return_code"        int,
   "error_description"  varchar(255),
   "duration"           int,
   "client_ip"          varchar(15),
   "operation"          varchar(255),
   "catalog_id"         char(32),
   "transaction_data"   varchar(4000),
   "creation_time"      date,
   constraint PK_RBAC_AUDITING primary key ("id")
);

/*==============================================================*/
/* Table: "rbac_catalog_operatives_action"                      */
/*==============================================================*/
create table "rbac_catalog_operatives_action"  (
   "catalog_id"         char(32),
   "action_id"          char(32),
   "operatives_id"      char(32)
);

/*==============================================================*/
/* Table: "rbac_catalogs"                                       */
/*==============================================================*/
create table "rbac_catalogs"  (
   "id"                 char(32)                        not null,
   "name"               varchar(64),
   "weight"             int,
   "parent_id"          char(32),
   "disabled"           int,
   constraint PK_RBAC_CATALOGS primary key ("id")
);

comment on column "rbac_catalogs"."parent_id" is
'root id: 00000000000000000000000000000000';

comment on column "rbac_catalogs"."disabled" is
'0: normal
1: disabled';

/*==============================================================*/
/* Table: "rbac_groups"                                         */
/*==============================================================*/
create table "rbac_groups"  (
   "id"                 char(32)                        not null,
   "name"               varchar(64),
   "description"        varchar(256),
   "blocked"            int,
   "system"             int,
   constraint PK_RBAC_GROUPS primary key ("id")
);

comment on column "rbac_groups"."blocked" is
'0: normal
1: blocked';

comment on column "rbac_groups"."system" is
'0: customize
1: system groups';

/*==============================================================*/
/* Table: "rbac_groups_roles"                                   */
/*==============================================================*/
create table "rbac_groups_roles"  (
   "role_id"            char(32)                        not null,
   "group_id"           char(32)                        not null,
   constraint PK_RBAC_GROUPS_ROLES primary key ("role_id", "group_id")
);

/*==============================================================*/
/* Table: "rbac_operatives"                                     */
/*==============================================================*/
create table "rbac_operatives"  (
   "id"                 char(32)                        not null,
   "name"               varchar(255),
   "weight"             int,
   "description"        varchar(255),
   "parent_id"          char(32),
   constraint PK_RBAC_OPERATIVES primary key ("id")
);

comment on column "rbac_operatives"."parent_id" is
'root id: 00000000000000000000000000000000';

/*==============================================================*/
/* Table: "rbac_roles"                                          */
/*==============================================================*/
create table "rbac_roles"  (
   "id"                 char(32)                        not null,
   "name"               varchar(64),
   "description"        varchar(255),
   constraint PK_RBAC_ROLES primary key ("id")
);

/*==============================================================*/
/* Table: "rbac_roles_catalogs_operatives"                      */
/*==============================================================*/
create table "rbac_roles_catalogs_operatives"  (
   "role_id"            char(32)                        not null,
   "catalog_id"         char(32)                        not null,
   "operative_id"       char(32)                        not null,
   constraint PK_RBAC_ROLES_CATALOGS_OPERATI primary key ("role_id", "catalog_id", "operative_id")
);

/*==============================================================*/
/* Table: "rbac_users"                                          */
/*==============================================================*/
create table "rbac_users"  (
   "id"                 char(32)                        not null,
   "name"               varchar(64),
   "last_name"          varchar(64),
   "first_name"         varchar(64),
   "email"              varchar(255),
   "password"           varchar(255),
   "blocked"            int,
   "description"        varchar(255),
   "super_user"         INT,
   "creation_time"      date,
   constraint PK_RBAC_USERS primary key ("id")
);

comment on column "rbac_users"."blocked" is
'0: none blocked
1: blocked';

comment on column "rbac_users"."super_user" is
'0: default, normal user
1: super user';

/*==============================================================*/
/* Table: "rbac_users_groups"                                   */
/*==============================================================*/
create table "rbac_users_groups"  (
   "user_id"            char(32)                        not null,
   "group_id"           char(32)                        not null,
   constraint PK_RBAC_USERS_GROUPS primary key ("user_id", "group_id")
);

alter table "rbac_catalog_operatives_action"
   add constraint FK_RBAC_CAT_REFERENCE_RBAC_CAT foreign key ("catalog_id")
      references "rbac_catalogs" ("id");

alter table "rbac_catalog_operatives_action"
   add constraint FK_RBAC_CAT_REFERENCE_RBAC_ACT foreign key ("action_id")
      references "rbac_actions" ("id");

alter table "rbac_catalog_operatives_action"
   add constraint FK_RBAC_CAT_REFERENCE_RBAC_OPE foreign key ("operatives_id")
      references "rbac_operatives" ("id");

alter table "rbac_groups_roles"
   add constraint FK_RBAC_GRO_REFERENCE_RBAC_ROL foreign key ("role_id")
      references "rbac_roles" ("id");

alter table "rbac_groups_roles"
   add constraint FK_RBAC_GRO_REFERENCE_RBAC_GRO foreign key ("group_id")
      references "rbac_groups" ("id");

alter table "rbac_roles_catalogs_operatives"
   add constraint FK_RBAC_ROL_REFERENCE_RBAC_OPE foreign key ("operative_id")
      references "rbac_operatives" ("id");

alter table "rbac_roles_catalogs_operatives"
   add constraint FK_RBAC_ROL_REFERENCE_RBAC_ROL foreign key ("role_id")
      references "rbac_roles" ("id");

alter table "rbac_roles_catalogs_operatives"
   add constraint FK_RBAC_ROL_REFERENCE_RBAC_CAT foreign key ("catalog_id")
      references "rbac_catalogs" ("id");

alter table "rbac_users_groups"
   add constraint FK_RBAC_GROUP foreign key ("user_id")
      references "rbac_groups" ("id");

alter table "rbac_users_groups"
   add constraint FK_RBAC_USER foreign key ("group_id")
      references "rbac_users" ("id");

