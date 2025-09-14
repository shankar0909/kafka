mkdir -p plugins

# Debezium MySQL
curl -L -o debezium-mysql.tar.gz \
  https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/2.7.2.Final/debezium-connector-mysql-2.7.2.Final-plugin.tar.gz
tar -xzf debezium-mysql.tar.gz -C plugins/

# JDBC
curl -L -o kafka-connect-jdbc.zip \
  https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-jdbc/versions/10.7.4/confluentinc-kafka-connect-jdbc-10.7.4.zip
unzip kafka-connect-jdbc.zip -d plugins/


# kafka-connect-jdbc might fail, so download them manually and place in plugins directory via:https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc
