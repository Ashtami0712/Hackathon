create database "framework-db";

create user framework_admin with password 'frameworkDbLocal';

GRANT ALL privileges on database "framework-db" to framework_admin;

GRANT ALL ON SCHEMA public TO framework_admin;