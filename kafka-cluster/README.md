Commands

Run the docker compose to create brokers and ui by executing the below command:

### Move to correct directory
cd kafka-cluster
### Bring up cluster
docker compose up -d


1. To create a topic

    docker exec -it broker1 kafka-topics \
    --create \
    --topic b1-topic \
    --bootstrap-server broker1:9092 \
    --replication-factor 3 \
    --partitions 3

2. List all topics

    docker exec -it broker1 kafka-topics \
    --list \
    --bootstrap-server broker1:9092   

3. Describe a topic

    docker exec -it broker1 kafka-topics \
    --describe \
    --topic b1-topic \
    --bootstrap-server broker1:9092



CLEAN UP
-> docker compose down -v
-> Stop all containers (docker container stop "id")
-> Remove all containers (docker container prune)