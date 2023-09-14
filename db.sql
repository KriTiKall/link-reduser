-- сначала создаём базу и пользователя.
CREATE USER "admin_short" LOGIN SUPERUSER PASSWORD 'admin';

CREATE DATABASE 'link-reducer-2' OWNER "admin_short" ENCODING 'UTF8' CONNECTION LIMIT 20;

-- После создаём все таблицы

create schema if not exists model;

create table if not exists model.users
(
    id              serial primary key,
    login            varchar(25)  not null,
    name            varchar(25)  not null,
    email            varchar(25)  not null,
    password_hash   varchar(80)  not null,
    unique (login)
);

create table if not exists model.user_roles
(
    id              serial primary key,
    name            varchar(80)  not null
    unique (name)
);

create table if not exists model.roles
(
    id              serial primary key,
    name            varchar(80)  not null
    unique (name)
);

create table if not exists model.shorts
(
    id      serial primary key,
    short   varchar(10) not null,
    unique (short)
);

create table if not exists model.links
(
    id          serial primary key,
    user_id     int references model.users(id),
    site        varchar(25) not null,
    name        varchar(25) not null,
    link        text not null,
    short_id    int references model.shorts(id),
    unique (user_id, name),
    unique (user_id, short_id)
);

create table if not exists model.statistics
(
	id          serial primary key,
    link_id  int references model.links(id),
    date_time timestamp not null
);