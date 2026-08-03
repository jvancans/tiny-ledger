INSERT INTO accounts(id, currency_iso_number, balance)
VALUES ('ce58d887-2a59-4dc1-a83a-0d74ea642a71', 978, 100.5);

INSERT INTO transactions(id, account_id, timestamp_millis, amount)
VALUES ('3b381d40-3272-4e10-89f9-7fa7afb9dcc8', 'ce58d887-2a59-4dc1-a83a-0d74ea642a71', 1785743355448, 1000);

INSERT INTO transactions(id, account_id, timestamp_millis, amount)
VALUES ('fdfa6dd6-bca3-481f-b7cc-fe1f786178ac', 'ce58d887-2a59-4dc1-a83a-0d74ea642a71', 1785743355449, -899.5);