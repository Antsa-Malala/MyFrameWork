drop user servlet cascade;
create user servlet identified by servlet;
grant dba to servlet;
alter user servlet account unlock;
connect servlet/servlet;

CREATE sequence seqPlat start with 1;
CREATE FUNCTION getSeqPlat RETURN number IS sequence number;
    BEGIN
    sequence := seqPlat.NEXTVAL;
    RETURN sequence;
    END;
    /

create table plat(
    id varchar(7) primary key,
    libelle varchar(20)
);

CREATE sequence seqEmploye start with 1;
CREATE FUNCTION getSeqEmploye RETURN number IS sequence number;
    BEGIN
    sequence := seqEmploye.NEXTVAL;
    RETURN sequence;
    END;
    /

create table Employe(
    id varchar(7) primary key,
    nom varchar(20),
    prenom varchar(20)
);


CREATE sequence seqPlatC start with 1;
CREATE FUNCTION getSeqPlatC RETURN number IS sequence number;
    BEGIN
    sequence := seqPlatC.NEXTVAL;
    RETURN sequence;
    END;
    /

create table platC(
    id varchar(7) primary key,
    daty date,
    idemp varchar(7),
    idplat varchar(7),
    foreign key (idemp) references employe(id),
    foreign key (idplat) references plat(id)
);

create table fichier_plat(
    idplatC varchar(7),
    sary BLOB,
    foreign key (idplatC) references platC(id)
);

insert into plat values('PLT0001','Henakisoa');
insert into plat values('PLT0002','Hena Omby');
insert into plat values('PLT0003','Saosisy');
insert into plat values('PLT0004','Hena baolina');

insert into employe values('EMP0001','Andria','Jean');
insert into employe values('EMP0002','Malala','Marie');
insert into employe values('EMP0003','Rakoto','Rakoto');
insert into employe values('EMP0004','Jean','Marc');