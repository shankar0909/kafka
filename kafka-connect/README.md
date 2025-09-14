A sample pipeline project written using kafka connect. This is written using KRAFT (Non-Zookeeper) approach.

Helper Commands

Run the docker compose to create brokers and ui by executing the below command:

PRE-REQUISITE: Make sure connectors/plugins are downloaded under plugin directory by using the "pre-requiste-script" provided.

### Move to correct directory
cd kafka-connect
### Bring up cluster
docker compose up -d --build


### Connectors are being auto registered. However if someone wants to explore. Use below commands: This app is not IDEMPOTENT. Only supports registration of new connectors, not updates.

curl -X POST -H "Content-Type: application/json" --data @connectors/mysql-source.json http://localhost:8083/connectors
curl -X POST -H "Content-Type: application/json" --data @connectors/postgres-sink.json http://localhost:8083/connectors


### Confirm the status

curl -X GET http://localhost:8083/connectors/mysql-source-connector/status
curl -X GET http://localhost:8083/connectors/postgres-sink-connector/status


### INSERT data in MYSQL
docker exec -it mysql mysql -u root -prootpass -e "USE kafka_db; INSERT INTO customers (first_name, last_name, email) VALUES ('Virat', 'Kohli', 'vk18@example.com');"

### CONFIRM if data recieved in POSTGRES
docker exec -it postgres psql -U kafka -d kafka_db -c "SELECT * FROM customers;"

###
KAFKA UI: http://localhost:8080/

