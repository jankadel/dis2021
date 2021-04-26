INSERT INTO estate_agent (name, address, login, password) VALUES
	('Michael', 'Alster', 'michael', 'kranich_2'),
	('Corinna', 'Elbvororte', 'corinna', 'c321');
	
INSERT INTO apartment (city, postal_code, street, street_no, square_area, login, floor, rent, rooms, balcony, kitchen) VALUES
	('Hamburg', 20149, 'Harvestehuder Stieg', 2, 110, 'michael', 3, 6800.50, 4, FALSE, TRUE);
	
INSERT INTO house (city, postal_code, street, street_no, square_area, login, floors, price, garden) VALUES
	('Hamburg', 22587, 'Caprivistraﬂe', 16, 205.34, 'corinna', 2, 1200000, true);

INSERT INTO person (first_name, name, address) VALUES
	('Grau', 'Euler', 'Nestraﬂe 1, 34278 Nestling'),
	('Guru', 'Schnabel', 'Dorfstraﬂe 345, 85942 Aufm Land');
	
INSERT INTO tenancy_contract (date, place, start_date, duration, additional_costs, estate_id, person_id) VALUES
	('today', 'Hamburg', 'tomorrow', 2, 200, 1, 1);
	
INSERT INTO purchase_contract (date, place, no_installments, interest_rate, estate_id, person_id) VALUES
	('yesterday', 'Hamburg', 1, 0, 2, 2);