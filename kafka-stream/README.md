### A simple kafka streams app

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
5. Publish  avro input message schema in schema registry through CLI:

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
6. Recieve the enriched message at output topic and in application logs.
  



###

# Sample call to register any schema
POST /subjects/enriched-topic-value/versions
{ "schema": "<enriched schema>" }

# Actual message to KAFKA in AVRO wire-format
[ magic byte 0x00 ]
[ 4-byte schema id (big-endian) ]
[ Avro binary-encoded payload ]


