create database card_db;
create user 'card-server'@'%' identified by 'card-server';
grant all on card_db.* to 'card-server'@'%' with grant option;
exit