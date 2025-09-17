### A simple kafka streams app

1. Bootstrap Kafka cluster by running below command:
    docker compose up -d

2. Start springboot application. It will create couple of topics(input-topic & output-topic) behind the scene.

3. Visit UI at http://localhost:8082 to monitor broker & topic

4. Publish  avro input message schema in schema registry through CLI:

First publish the Schema:

kafka-avro-console-producer \
  --broker-list broker:29092 \
  --topic input-topic \
  --property schema.registry.url=http://schema-registry:8081 \
  --property value.schema='{
     "type":"record",
     "name":"UserEvent",
     "namespace":"com.kafka.kafka_stream.model",
     "fields":[
       {"name":"userId","type":"string"},
       {"name":"action","type":"string"}
     ]
  }'


Then put EVENTS:


{"userId":"u123","action":"login"}
{"userId":"u456","action":"purchase"}
.
.
.

  



###

# Sample call to register the any schema
POST /subjects/enriched-topic-value/versions
{ "schema": "<enriched schema>" }

# Actual message to KAFKA in AVRO wire-format
[ magic byte 0x00 ]
[ 4-byte schema id (big-endian) ]
[ Avro binary-encoded payload ]



###

5. publish the below sample message on input-topic. LEverage EventController at :

curl --location 'http://localhost:8080/send' \
--header 'Content-Type: application/json' \
--data '{
    "userId":"0909",
    "action":"login"
}'




5. Recieve the enriched message at output topic and in application logs.