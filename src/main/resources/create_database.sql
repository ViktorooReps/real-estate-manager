DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS Offers CASCADE;
DROP TABLE IF EXISTS Clients CASCADE;

CREATE TABLE IF NOT EXISTS Clients(
    client_id SERIAL PRIMARY KEY,
    client_name TEXT,
    email TEXT,
    phone_number TEXT
);

CREATE TABLE IF NOT EXISTS Orders(
    order_id SERIAL PRIMARY KEY,
    ordered_by INTEGER REFERENCES Clients ON DELETE CASCADE NOT NULL,
    contract_type TEXT NOT NULL,
    requested_estate_types JSON,
    requested_estate_facades JSON,
    requested_space_min JSON,
    requested_commodities JSON,
    floor_min INTEGER CHECK ( floor_min > 0 ),
    floor_max INTEGER CHECK ( floor_max > floor_min ),
    building_state TEXT,
    requested_transport_max JSON,
    requested_locations JSON,
    price_max INTEGER CHECK ( price_max > 0 )
);

CREATE TABLE IF NOT EXISTS Offers(
    offer_id SERIAL PRIMARY KEY,
    offered_by INTEGER REFERENCES Clients ON DELETE CASCADE NOT NULL,
    contract_type TEXT NOT NULL,
    estate_type TEXT NOT NULL,
    estate_facade TEXT NOT NULL,
    space JSON NOT NULL,
    commodities JSON NOT NULL,
    floor INTEGER CHECK ( floor > 0 ) NOT NULL,
    building_state TEXT NOT NULL,
    transport JSON NOT NULL,
    location TEXT NOT NULL,
    starting_price INTEGER CHECK ( starting_price > 0 ) NOT NULL,
    address TEXT NOT NULL
);

INSERT INTO Clients (client_name, email, phone_number)
    VALUES ('Mark Markovich',   'mark@m.com',   '+77777777'),
           ('Bob Bobich',       'bob@m.com',    '+77778888'),
           ('John Johnovich',   'john@m.com',   '+77734737'),
           ('Steve Stevenson',  'steve@m.com',  '+73942432'),
           ('Paul Paulig',      'paul@m.com',   '+72375802');

INSERT INTO Orders (ordered_by, contract_type, requested_estate_types, requested_estate_facades,
                    requested_space_min, requested_commodities, floor_min, floor_max, building_state, requested_transport_max,
                    requested_locations, price_max)
    VALUES (1, 'long-term rental', NULL, NULL, '{"total": 100, "kitchen": 10}', '{"Internet": true}', NULL,
            NULL, NULL, NULL, '{"Moscow inside MKAD": true, "Moscow outside MKAD": true, "Moscow region": false}', 100000),
           (2, 'short-term rental', '{"cottage": true, "apartment": false, "room": false}', NULL, NULL,
            '{"sauna": true, "pool": true}', NULL, NULL, NULL, '{"train": 15}', NULL, 10000),
           (3, 'purchase', '{"apartment": true, "room": true, "cottage": false}',
            '{"brick": true, "concrete": false, "wood": false}', NULL, NULL, 5, 14, 'new', NULL, NULL, 5000000),
           (4, 'short-term rental', '{"cottage": false, "apartment": true, "room": false}', NULL, NULL, NULL, NULL,
            NULL, NULL, '{"bus": 5, "metro": 10}',
            '{"Moscow inside MKAD": true, "Moscow outside MKAD": false, "Moscow region": false}', 2000),
           (5, 'long-term rental', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10000);

INSERT INTO Offers (offered_by, contract_type, estate_type, estate_facade, space, commodities, floor,
                    building_state, transport, location, starting_price, address)
    VALUES (1, 'long-term rental', 'cottage', 'wood', '{"total": 200, "kitchen": 29, "bedroom": 40}',
            '{"Internet": true, "sauna": true, "furniture": true}', 1, 'new', '{"train": 30}', 'Moscow region', 70000,
            'Spinoza st. 187/2'),
           (1, 'short-term rental', 'cottage', 'wood', '{"total": 200, "kitchen": 29, "bedroom": 40}',
            '{"Internet": true, "sauna": true, "furniture": true}', 1, 'new', '{"train": 30}', 'Moscow region', 5000,
            'Spinoza st. 187/2'),
           (1, 'purchase', 'cottage', 'wood', '{"total": 200, "kitchen": 29, "bedroom": 40}',
            '{"Internet": true, "sauna": true, "furniture": true}', 1, 'new', '{"train": 30}', 'Moscow region', 9000000,
            'Spinoza st. 187/2'),
           (4, 'purchase', 'room', 'brick', '{"total": 30, "bedroom": 30}', '{"Internet": true, "furniture": false}',
            1, '90s', '{"metro": 10, "bus": 5}', 'Moscow outside MKAD', 500000, 'Leibniz st. 34/1'),
           (4, 'purchase', 'room', 'brick', '{"total": 50, "bedroom": 50}', '{"Internet": true, "furniture": false}',
            1, '90s', '{"metro": 10, "bus": 5}', 'Moscow outside MKAD', 1000000, 'Leibniz st. 34/1');