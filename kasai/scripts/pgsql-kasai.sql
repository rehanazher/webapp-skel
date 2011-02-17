-- KASAI database creation script
-- for PostgreSQL 8.x and above

CREATE TABLE kasai_users
(
id                                  VARCHAR(50) NOT NULL,
first_name                          VARCHAR(50),
last_name                           VARCHAR(50),
email                               VARCHAR(254),
password                            VARCHAR(254),
blocked                             INT,
description                         VARCHAR(254),
data                                TEXT,
super_user                          SMALLINT default 0,
PRIMARY KEY (id)
);

CREATE TABLE kasai_groups
(
id                                  VARCHAR(50) NOT NULL,
description                         VARCHAR(254),
blocked                             INT,
system                              SMALLINT default 0,
data                                TEXT,

PRIMARY KEY (id)
) ;

CREATE TABLE kasai_users_groups
(
id_user                             VARCHAR(50) NOT NULL,
id_group                            VARCHAR(50) NOT NULL,

PRIMARY KEY (id_user,id_group ),

CONSTRAINT FK_users_groups_users FOREIGN KEY( id_user )
    REFERENCES kasai_users ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE,

CONSTRAINT FK_users_groups_groups FOREIGN KEY( id_group )
    REFERENCES kasai_groups ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE
) ;

CREATE INDEX IDX_kasai_users_groups_user ON kasai_users_groups (id_user);
CREATE INDEX IDX_kasai_users_groups_group ON kasai_users_groups (id_group);

CREATE TABLE kasai_operatives
(
id                                  VARCHAR(254) NOT NULL,
sequence                            INT,
description                         VARCHAR(254),

PRIMARY KEY (id )
) ;

CREATE TABLE kasai_objects
(
id                             VARCHAR(254) NOT NULL,

PRIMARY KEY (id )
) ;

CREATE TABLE kasai_roles
(
id                             SERIAL,
name                           VARCHAR(50) NOT NULL,
description                    VARCHAR(254),

PRIMARY KEY (id )
) ;

CREATE TABLE kasai_roles_operatives
(
id_role                         int NOT NULL,
id_operative                   VARCHAR(254) NOT NULL,

PRIMARY KEY (id_role,id_operative ),

CONSTRAINT FK_roles_operatives_operatives FOREIGN KEY( id_operative )
    REFERENCES kasai_operatives ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE,

CONSTRAINT FK_roles_operatives_roles FOREIGN KEY( id_role )
    REFERENCES kasai_roles ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE

) ;

CREATE INDEX IDX_kasai_roles_operatives_role ON kasai_roles_operatives (id_role);
CREATE INDEX IDX_kasai_roles_operatives_operative ON kasai_roles_operatives (id_operative);

CREATE TABLE kasai_objects_users_roles(
id                      SERIAL,
id_object  		VARCHAR(254) NOT NULL,
id_user                 VARCHAR(50) NOT NULL,
id_role		         int NOT NULL,

PRIMARY KEY (id ),

UNIQUE (id_object, id_user, id_role),

CONSTRAINT FK_objects_users_roles_roles FOREIGN KEY( id_role )
    REFERENCES kasai_roles ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE,

CONSTRAINT FK_objects_users_roles_objects FOREIGN KEY( id_object )
    REFERENCES kasai_objects ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE,

CONSTRAINT FK_objects_users_roles_users FOREIGN KEY( id_user )
    REFERENCES kasai_users ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE

) ;

CREATE INDEX IDX_kasai_objects_users_roles_object ON kasai_objects_users_roles (id_object);
CREATE INDEX IDX_kasai_objects_users_roles_user ON kasai_objects_users_roles (id_user);
CREATE INDEX IDX_kasai_objects_users_roles_role ON kasai_objects_users_roles (id_role);

CREATE TABLE kasai_objects_groups_roles(
id                       SERIAL,
id_object 		VARCHAR(254) NOT NULL,
id_group                 VARCHAR(50) NOT NULL,
id_role	 		int NOT NULL,

PRIMARY KEY (id ),

UNIQUE (id_object, id_group, id_role),

CONSTRAINT FK_objects_groups_roles_roles FOREIGN KEY( id_role )
    REFERENCES kasai_roles ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE,

CONSTRAINT FK_objects_groups_roles_objects FOREIGN KEY( id_object )
    REFERENCES kasai_objects ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE,

CONSTRAINT FK_objects_groups_roles_groups FOREIGN KEY( id_group )
    REFERENCES kasai_groups ( id )
    ON UPDATE NO ACTION
    ON DELETE CASCADE

) ;

CREATE INDEX IDX_kasai_objects_groups_roles_object ON kasai_objects_groups_roles (id_object);
CREATE INDEX IDX_kasai_objects_groups_roles_group ON kasai_objects_groups_roles (id_group);
CREATE INDEX IDX_kasai_objects_groups_roles_role ON kasai_objects_groups_roles (id_role);

CREATE TABLE kasai_audit(
    audit_id            SERIAL,
    user_id             VARCHAR(50) NOT NULL,
    date_time           TIMESTAMP NOT NULL,
    return_code         INT NOT NULL,
    error_description   VARCHAR(254),
    duration            INT,
    client_ip           VARCHAR(15),
    operation           VARCHAR(254) NOT NULL,
    object_id           VARCHAR(254),
    transaction_data    TEXT,

    PRIMARY KEY (audit_id)
) ;

-- Operatives

INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai','1','All permissions over Kasai');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.object.modifyaccess','2','Modify an object permissions');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.user','5','All permissions involving users');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.user.read','6','Read user information');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.user.delete','7','Delete a user');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.user.commit','8','Create and update a users information');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.user.resetpassword','9','Reset a users password');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.user.block','10','Block a user');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.user.unblock','11','Unblock a user');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group','12','All permissions involving groups');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group.read','13','Read group information');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group.delete','14','Delete a group');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group.commit','15','Create and update a groups information');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group.block','16','Block a group');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group.unblock','17','Unblock a group');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group.user.delete','18','Remove users from a group');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.group.user.add','19','Add users to a group');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.role.commit','20','Create and update a role information');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.role.read','21','Read role information');
INSERT INTO kasai_operatives (id,sequence,description) VALUES('kasai.role.delete','22','Delete a role');

-- Groups

insert into kasai_groups (id,description,blocked,data) values ('Administrators','Administrators group',0,null);
insert into kasai_groups (id,description,blocked,system,data) values ('AllUsers','All system users',0,1,null);

-- Users
-- This two sample users have the password set to "password" using a SHA hashing algorithm
insert into kasai_users (id, password,blocked, super_user) values ('admin','5b-56-61-1c-37-47-3f-3f-6-7e-25-b-6c-8-33-1b-7e-1a-71-28',0,1);
insert into kasai_users (id, password,blocked, super_user) values ('guest','5b-56-61-1c-37-47-3f-3f-6-7e-25-b-6c-8-33-1b-7e-1a-71-28',0,0);

-- user/group

insert into kasai_users_groups (id_user,id_group) values ('admin','Administrators');
insert into kasai_users_groups (id_user,id_group) values ('admin','AllUsers');
insert into kasai_users_groups (id_user,id_group) values ('guest','AllUsers');
-- operatives/roles

-- Let PGSQL generate IDs
insert into kasai_roles (name,description) values ('Administrator','Can perform any action');
insert into kasai_roles (name,description) values ('Guest','Can not perform any action by default');
insert into kasai_roles (name,description) values ('User','Can read users, roles and groups information');

----Administrator Role
insert into kasai_roles_operatives values (1,'kasai');

---User Role
insert into kasai_roles_operatives values (3,'kasai.group.read');
insert into kasai_roles_operatives values (3,'kasai.role.read');
insert into kasai_roles_operatives values (3,'kasai.user.read');


-- Entities

insert into kasai_objects values ('/kasai/');
insert into kasai_objects values ('/kasai/user/');
insert into kasai_objects values ('/kasai/group/');
insert into kasai_objects values ('/kasai/role/');
insert into kasai_objects values ('/kasai/user/admin');
insert into kasai_objects values ('/kasai/user/guest');
insert into kasai_objects values ('/kasai/group/Administrators');
insert into kasai_objects values ('/kasai/group/AllUsers');
insert into kasai_objects values ('/kasai/role/1');
insert into kasai_objects values ('/kasai/role/2');
insert into kasai_objects values ('/kasai/role/3');

-- Assign permissions on objects

insert into kasai_objects_groups_roles (id_object, id_group, id_role) values ('/kasai/','Administrators',1);