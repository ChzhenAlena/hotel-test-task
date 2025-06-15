INSERT INTO hotel (id, name, description, brand) VALUES
(1, 'DoubleTree by Hilton Minsk', 'The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...', 'Hilton');

INSERT INTO address (hotel_id, house_number, street, city, country, post_code) VALUES
(1, '9', 'Pobediteley Avenue', 'Minsk', 'Belarus', '220004');

INSERT INTO contacts (hotel_id, phone, email) VALUES
(1, '+375 17 309-80-00', 'doubletreeminsk.info@hilton.com');

INSERT INTO arrival_time (hotel_id, check_in, check_out) VALUES
(1, '14:00', '12:00');

INSERT INTO amenity (id, name) VALUES
(1, 'Free parking'),
(2, 'Free WiFi'),
(3, 'Non-smoking rooms'),
(4, 'Concierge'),
(5, 'On-site restaurant'),
(6, 'Fitness center'),
(7, 'Pet-friendly rooms'),
(8, 'Room service'),
(9, 'Business center'),
(10, 'Meeting rooms');

INSERT INTO hotel_amenities (hotel_id, amenity_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10);

ALTER TABLE hotel ALTER COLUMN id RESTART WITH 2;
ALTER TABLE address ALTER COLUMN id RESTART WITH 2;
ALTER TABLE contacts ALTER COLUMN id RESTART WITH 2;
ALTER TABLE arrival_time ALTER COLUMN id RESTART WITH 2;
ALTER TABLE amenity ALTER COLUMN id RESTART WITH 11;
