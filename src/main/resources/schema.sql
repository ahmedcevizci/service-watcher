drop table if exists tb_http_link_status;
drop table if exists tb_http_link;
drop table if exists tb_user;

create table tb_user
(
    name               varchar(255) not null
        primary key,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    password           varchar(255) not null,
    version            bigint       not null
);


create table tb_http_link
(
    id                 varchar(255)  not null
        primary key,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    method             varchar(255) not null,
    name               varchar(255) not null,
    success_status     int          not null,
    url                varchar(255) not null,
    version            bigint       not null,
    user               varchar(255) not null,
    constraint unique_url
        unique (url),
    constraint fk_tb_user_name__user
        foreign key (user) references tb_user (name)
);

create table tb_http_link_status
(
    http_link_id   varchar(255)  not null
        primary key,
    last_fail_date datetime(6)  null,
    last_ok_date   datetime(6)  null,
    last_status    varchar(255) null,
    version        bigint       not null
);
