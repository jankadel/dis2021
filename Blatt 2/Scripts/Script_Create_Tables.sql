DROP TABLE IF EXISTS estate_agent, estate, apartment, house, person, contract, tenancy_contract, purchase_contract;

CREATE TABLE estate_agent(
	name varchar(30),
	address varchar(30),
	login varchar(30) PRIMARY KEY,
	password varchar(30)
);

CREATE TABLE estate(
	estate_id SERIAL PRIMARY KEY,
	city varchar(30),
	postal_code varchar(30),
	street varchar(30),
	street_no integer,
	square_area NUMERIC,
	login varchar(30) REFERENCES estate_agent (login) ON DELETE CASCADE
);

CREATE TABLE apartment(
	floor integer,
	rent NUMERIC,
	rooms integer,
	balcony bool,
	kitchen bool,
	PRIMARY KEY (estate_id),
	FOREIGN KEY (login) REFERENCES estate_agent (login) ON DELETE CASCADE 
) INHERITS (estate);

CREATE TABLE house(
	floors integer,
	price NUMERIC,
	garden bool,
	PRIMARY KEY (estate_id),
	FOREIGN KEY (login) REFERENCES estate_agent (login) ON DELETE CASCADE
) INHERITS (estate);

CREATE TABLE person(
	first_name varchar(30),
	name varchar(30),
	address varchar(50),
	person_id SERIAL PRIMARY KEY
);

CREATE TABLE contract(
	contract_no SERIAL PRIMARY KEY,
	date date,
	place varchar(30),
	estate_id integer REFERENCES estate (estate_id) ON DELETE CASCADE,
	person_id integer REFERENCES person (person_id)
);

CREATE TABLE tenancy_contract(
	start_date date,
	duration integer,
	additional_costs NUMERIC,
	PRIMARY KEY (contract_no),
	FOREIGN KEY (estate_id) REFERENCES apartment (estate_id) ON DELETE CASCADE,
	FOREIGN KEY (person_id) REFERENCES person (person_id)
) INHERITS (contract);

CREATE TABLE purchase_contract(
	no_installments integer,
	interest_rate NUMERIC,
	PRIMARY KEY (contract_no),
	FOREIGN KEY (estate_id) REFERENCES house (estate_id) ON DELETE CASCADE,
	FOREIGN KEY (person_id) REFERENCES person (person_id)
) INHERITS (contract);