### A simple kafka streams app

1. Bootstrap Kafka cluster by running below command:
    docker compose up -d

2. Start springboot application. It will create couple of topics behind the scene.

3. Visit UI at http://localhost:8080

4. Publish any message at source topic

5. Recieve the same message in UPPSERCASE at output topic and in application logs.