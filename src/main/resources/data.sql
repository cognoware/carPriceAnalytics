-- Datos de ejemplo para demo de carPriceAnalytics
-- Vehiculos pre-cargados en cache H2

INSERT INTO vehicle (id, license_plate, owner_identification, owner_name, brand, model, vehicle_year, color, engine_displacement, vehicle_class, service_type, vin, canton, country, commercial_model, average_price, min_price, max_price, price_source, last_update_date, created_date)
VALUES
(1, 'ABC1234', '0102030405', 'JUAN CARLOS PEREZ LOPEZ', 'CHEVROLET', 'AVEO FAMILY', 2018, 'BLANCO', '1500', 'AUTOMOVIL', 'PARTICULAR', '9GASP48X09B123456', 'CUENCA', 'ECUADOR', 'AVEO FAMILY 1.5L', 10500.00, 9200.00, 11800.00, 'PRICE_PROVIDER_A,PRICE_PROVIDER_B', '2025-02-20 10:30:00', '2025-01-15 08:00:00'),
(2, 'XYZ5678', '0102030405', 'JUAN CARLOS PEREZ LOPEZ', 'KIA', 'SPORTAGE', 2020, 'GRIS', '2000', 'CAMIONETA', 'PARTICULAR', '5XYZH4AG8LG123789', 'CUENCA', 'ECUADOR', 'SPORTAGE 2.0L LX', 24500.00, 22000.00, 27000.00, 'PRICE_PROVIDER_A,PRICE_PROVIDER_B', '2025-02-20 10:35:00', '2025-01-15 08:05:00'),
(3, 'DEF9012', '0301020304', 'MARIA FERNANDA GARCIA RUIZ', 'TOYOTA', 'HILUX', 2019, 'NEGRO', '2400', 'CAMIONETA', 'PARTICULAR', 'JTFSS22P6J0654321', 'QUITO', 'ECUADOR', 'HILUX 4X4 2.4L', 32000.00, 28500.00, 35500.00, 'PRICE_PROVIDER_A,PRICE_PROVIDER_B', '2025-02-18 14:20:00', '2025-01-10 09:00:00'),
(4, 'GHI3456', '0704050607', 'CARLOS ANDRES MARTINEZ VERA', 'HYUNDAI', 'TUCSON', 2021, 'ROJO', '2000', 'CAMIONETA', 'PARTICULAR', 'KM8J3CA46MU987654', 'GUAYAQUIL', 'ECUADOR', 'TUCSON 2.0L GLS', 27800.00, 25000.00, 30600.00, 'PRICE_PROVIDER_A,PRICE_PROVIDER_B', '2025-02-19 11:00:00', '2025-01-12 07:30:00');

-- Modelos de vehiculos
INSERT INTO vehicle_model (id, brand, model, commercial_model, vehicle_year) VALUES
(1, 'CHEVROLET', 'AVEO FAMILY', 'AVEO FAMILY 1.5L', 2018),
(2, 'KIA', 'SPORTAGE', 'SPORTAGE 2.0L LX', 2020),
(3, 'TOYOTA', 'HILUX', 'HILUX 4X4 2.4L', 2019),
(4, 'HYUNDAI', 'TUCSON', 'TUCSON 2.0L GLS', 2021),
(5, 'NISSAN', 'SENTRA', 'SENTRA 1.8L ADVANCE', 2019),
(6, 'CHEVROLET', 'DMAX', 'DMAX 3.0L 4X4', 2020);

-- Homologacion de modelos (marca-modelo -> modelo comercial)
INSERT INTO vehicle_homologation (id, brand, model, commercial_brand, commercial_model) VALUES
(1, 'CHEVROLET', 'AVEO FAMILY', 'CHEVROLET', 'AVEO FAMILY 1.5L'),
(2, 'CHEVROLET', 'AVEO', 'CHEVROLET', 'AVEO FAMILY 1.5L'),
(3, 'KIA', 'SPORTAGE', 'KIA', 'SPORTAGE 2.0L LX'),
(4, 'TOYOTA', 'HILUX', 'TOYOTA', 'HILUX 4X4 2.4L'),
(5, 'HYUNDAI', 'TUCSON', 'HYUNDAI', 'TUCSON 2.0L GLS'),
(6, 'NISSAN', 'SENTRA', 'NISSAN', 'SENTRA 1.8L ADVANCE'),
(7, 'CHEVROLET', 'DMAX', 'CHEVROLET', 'DMAX 3.0L 4X4');

-- Configuracion de servicios
INSERT INTO service_config (id, service_name, timeout_seconds, cache_limit_days, active) VALUES
(1, 'VEHICLE_QUERY', 60, 7, true),
(2, 'PRICE_CALCULATION', 45, 15, true),
(3, 'PRIMARY_PROVIDER_EXTRACTOR', 30, 7, true),
(4, 'TERTIARY_PROVIDER_EXTRACTOR', 30, 7, true),
(5, 'SECONDARY_PROVIDER_EXTRACTOR', 25, 5, true),
(6, 'PRICE_PROVIDER_A_EXTRACTOR', 20, 30, true),
(7, 'PRICE_PROVIDER_B_EXTRACTOR', 20, 30, true);
