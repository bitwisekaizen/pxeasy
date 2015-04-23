create table PxeInstall (
    id int not null primary key auto_increment,
    uuid varchar(50) not null,
    macAddress varchar(20) not null
);