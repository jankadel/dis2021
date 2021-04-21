create table estate_agent (
	lastName varchar(30),
	address varchar(30),
	login varchar(30),
	passphrase varchar(30),
	constraint pkEstate_agent primary key (lastName)
);

create table estate (
	ID int,
	city varchar(30),
	postalCode int,
	street varchar(30),
	streetNumber int,
	squareArea int,
	lastName varchar(30),
	constraint pkEstate primary key (ID),
	constraint fkEstateAgent foreign key (lastName) references estate_agent,
	constraint lastNameUnique unique (lastName)
);

create table apartment (
	apartment_id int not null references estate(ID),
	floors int,
	rent decimal(10,2),
	rooms int,
	balcony bool,
	kitchen bool,
	constraint pkApartment primary key (apartment_id)
);

create table house (
	house_id int not null references estate(ID),
	floors int,
	price decimal(10,2),
	garden bool,
	constraint pkHouse primary key (house_id),
	constraint houseUnique unique (house_id)
);

create table contract (
	contract_id int,
	contract_date date,
	place varchar(30),
	constraint pkContract primary key (contract_id),
	constraint contractUnique unique (contract_id)
);

create table tenancy_contract (
	tenancy_contract_id int not null references contract(contract_id),
	start_date date,
	duration int,
	additional_cost decimal(10,2),
	constraint pkTenancy primary key (tenancy_contract_id)
);

create table purchase_contract (
	purchase_contract_id int not null references contract(contract_id),
	installments int,
	interest_rate decimal(3,2),
	constraint pkPurchase primary key (purchase_contract_id)
);

create table person (
	first_name varchar(30),
	last_name varchar(30),
	address varchar(30),
	constraint pkPerson primary key (first_name, last_name, address)
);

create table sells (
	contract_id int not null references purchase_contract(purchase_contract_id),
	house_id int,
	first_name varchar(30),
	last_name varchar(30),
	address varchar(30),
	constraint pkSale primary key (contract_id),
	constraint fkHouseID foreign key (house_id) references house,
	constraint houseUnique unique (house_id),
	constraint fkPerson foreign key (first_name, last_name, address) references person
);

create table rents (
	contract_id int not null references tenancy_contract(tenancy_contract_id),
	apartment_id int,
	first_name varchar(30),
	last_name varchar(30),
	address varchar(30),
	constraint pkRent primary key (contract_id),
	constraint fkApartmentID foreign key (apartment_id) references apartment,
	constraint apartmentUnique unique (apartment_id),
	constraint fkPerson foreign key (first_name, last_name, address) references person
);
