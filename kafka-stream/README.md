### A simple kafka streams app

This app will use AVRO library for serialization & deserialization of events by publishing the messages on the kafka topics.

## Application Flow

-> Event Recieved in JSON format

-> Transformer will transform it into the AVRO format

-> Serialized using AVRO serde and published on the input-topic

-> Consumer will pull the event, and deserialize using AVRO serde

-> Enrich the event 

-> Serialize and publish the event on the output-topic



## Bootstrap

1. Bootstrap Kafka cluster by running below command:
    docker compose up -d

###
2. Start springboot application. It will create couple of topics(input-topic & output-topic) behind the scene.

###
3. Visit UI at http://localhost:8082 to monitor broker & topic

###
4. Register optional schema using:


            curl --location 'http://localhost:8081/subjects/input-topic-value/versions' \
        --header 'Content-Type: application/json' \
        --data '{
            "schemaType": "AVRO",
            "schema": "{\"type\":\"record\",\"name\":\"UserEvent\",\"namespace\":\"com.kafka.kafka_stream.model\",\"fields\":[{\"name\":\"userId\",\"type\":\"string\"},{\"name\":\"action\",\"type\":\"string\"}]}"
          }'
        


###
5. Use REST endpoint to send the JSON message: This message will be transformed to the AVRO message and delivered to the input-topic.

   curl --location 'http://localhost:8080/api/events' \
--header 'Content-Type: application/json' \
--data '{
    "userId":"2722",
    "action":"inq"
}'



###
6. Read the message from input-topic, ENRICH it and deliver it to the output-topic for consumer to consume.
  



###

# Sample call to register any schema
POST /subjects/enriched-topic-value/versions
{ "schema": "<enriched schema>" }

# Actual message to KAFKA in AVRO wire-format
[ magic byte 0x00 ]
[ 4-byte schema id (big-endian) ]
[ Avro binary-encoded payload ]


