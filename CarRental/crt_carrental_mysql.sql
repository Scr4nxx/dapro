create table Automodell
(ID integer unsigned auto_increment primary key,
Bezeichnung varchar(20) not null,
Hersteller varchar(20),
Autoart integer /* constraint fk_autoart references Autoarten(ID) */,
Sitzplaetze integer,
kw integer,
Treibstoff char(10),
PreisTag float(5,2),
PreisKM float(5,2),
Achsen integer default 2,
Ladevolumen integer,
Zuladung integer,
Fuehrerschein char(2));

create table Autoarten
(ID integer unsigned auto_increment primary key,
Art varchar(20) unique not null);

create table Ausstattungen
(ID integer unsigned auto_increment primary key,
Bezeichnung varchar(20));

create table ModHatAusst
(MID integer /* constraint fk_mha_modell references Automodell(ID) */,
AID integer /* constraint fk_mha_ausst references Ausstattungen(ID) */);

create table Auto
(Kennzeichen char(10) primary key,
KMStand integer,
TUVTermin date,
Kaufdatum date,
Modell integer /* constraint fk_auto_modell references Automodell(ID) */);

create table Kunde
(ID integer unsigned auto_increment primary key,
Vorname varchar(50),
Nachname varchar(50),
PLZ char(5),
Ort varchar(50),
Strasse varchar(50),
EMail varchar(30),
TelNr char(18));

create table Reservierung
(ID integer unsigned auto_increment primary key,
KundeID integer /* constraint fk_res_kunde references KundeID) */,
ModellID integer /* constraint fk_res_modell references Automodell(ID) */,
Beginn datetime,
Ende datetime);

create table Leihvertrag
(ID int unsigned auto_increment primary key,
KundeID integer /* constraint fk_leih_kunde references KundeID) */,
AutoID integer /* constraint fk_leih_auto references AutoID) */,
Beginn datetime,
Ende datetime,
StartKM integer,
EndeKM integer,
Rechnungsbetrag float(6,2),
bezahlt char(1));

insert into Autoarten values (1,'Limousine');
insert into Autoarten values (2,'Kombi');
insert into Autoarten values (3,'Cabrio');
insert into Autoarten values (4,'Van');
insert into Autoarten values (5,'Kleinbus');
insert into Autoarten values (6,'LKW');
insert into Autoarten values (7,'Pickup');

insert into Automodell values (1,'Golf FSI','VW',1,5,80,'Super',54.70,0.04,2,350,400,'A');
insert into Automodell values (2,'Golf Variant TDI','VW',2,5,90,'Diesel',62.30,0.05,2,450,500,'A');
insert into Automodell values (3,'Golf','VW',1,5,60,'Super',45.00,0.03,2,350,400,'A');
insert into Automodell values (4,'Astra','Opel',1,5,70,'Super',40.70,0.04,2,330,380,'A');
insert into Automodell values (5,'528i','BMW',1,5,120,'Super',83.55,0.07,2,320,440,'A');
insert into Automodell values (6,'Taurus','Daimler-Chrysler',6,3,340,'Diesel',120.30,0.09,3,20000,4000,'B');
insert into Automodell values (7,'Sharan','VW',4,7,100,'Super',85.60,0.05,2,550,500,'A');

insert into Ausstattungen values (1,'Klimaanlage');
insert into Ausstattungen values (2,'Anhängekupplung');
insert into Ausstattungen values (3,'Navigationssystem');
insert into Ausstattungen values (4,'Tempomat');

insert into ModHatAusst values (1,1);
insert into ModHatAusst values (2,2);
insert into ModHatAusst values (5,1);
insert into ModHatAusst values (5,3);
insert into ModHatAusst values (5,4);
insert into ModHatAusst values (7,1);
insert into ModHatAusst values (7,2);

insert into Auto values ('RV AB 335', 45000, '2004-05-01', '2002-05-01', 3);
insert into Auto values ('RV AB 336', 39000, '2004-05-01', '2002-05-01', 3);
insert into Auto values ('RV AB 337', 41000, '2004-05-01', '2002-05-01', 3);
insert into Auto values ('RV XY 245', 18000, '2005-04-01', '2002-05-01', 1);
insert into Auto values ('RV XY 246', 19000, '2005-04-01', '2002-05-01', 1);
insert into Auto values ('RV XY 247', 21000, '2005-04-01', '2002-05-01', 1);
insert into Auto values ('RV XY 248', 35000, '2005-04-01', '2002-05-01', 2);
insert into Auto values ('RV XY 249', 29050, '2005-04-01', '2002-05-01', 2);
insert into Auto values ('RV BQ 591', 65000, '2005-06-01', '2002-05-01', 4);
insert into Auto values ('RV BQ 592', 66000, '2005-06-01', '2002-05-01', 4);
insert into Auto values ('RV BQ 593', 64500, '2005-06-01', '2002-05-01', 4);
insert into Auto values ('RV C 45', 150000, '2005-04-01', '2002-05-01', 6);
insert into Auto values ('RV MM 999', 16000, '2005-04-01', '2002-05-01', 5);
insert into Auto values ('RV PF 23', 25000, '2005-04-01', '2002-05-01', 7);