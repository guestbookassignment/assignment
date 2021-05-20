insert into user_role values (1, curdate(), 'Nithish', null, null, 'Admin');
insert into user_role values (2, curdate(), 'Nithish', null, null, 'User');

insert into users (id,created_date,created_user,updated_date,updated_user,enabled,last_login_date,password,username,role_id, phone_number)
values
(1,curdate(), 'Nithish', null, null, true, curdate(), '$2y$12$p0/Ck3if9MJ.PhQCdUzoQerXfXAUC0MPpnBeKETreGIepuO1l/J1.', 'user', 2, '123123213');

insert into users (id,created_date,created_user,updated_date,updated_user,enabled,last_login_date,password,username,role_id, phone_number)
values
(2,curdate(), 'Nithish', null, null, true, curdate(), '$2y$12$p0/Ck3if9MJ.PhQCdUzoQerXfXAUC0MPpnBeKETreGIepuO1l/J1.', 'admin', 1, '123123213');
