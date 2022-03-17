insert into tb_user (name, password, version, created_date, last_modified_date)
values ('root', 'password', 0, NOW(), NOW());


insert into tb_http_link (id, version, user, name, url, method, success_status, created_date, last_modified_date)
values ('1ab1534c-3f03-4294-872c-c0e6e69d1b7e',  0, 'root', 'KRY', 'https://www.kry.se', 'GET', 200, NOW(), NOW());

insert into tb_http_link (id, version, user, name, url, method, success_status, created_date, last_modified_date)
values ('4b90e431-ec7b-466a-a38e-c770e3563955',  0, 'root', 'Google', 'https://www.google.com', 'GET', 200, NOW(), NOW());

insert into tb_http_link (id, version, user, name, url, method, success_status, created_date, last_modified_date)
values ('cb703821-9ddd-486a-aaac-2cba62654ed',  0, 'root', 'Livi TR', 'https://livi.com.tr', 'GET', 200, NOW(), NOW());
