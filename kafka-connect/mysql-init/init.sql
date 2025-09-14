-- Criação do banco e tabelas
CREATE DATABASE IF NOT EXISTS kafka_db;

USE kafka_db;

CREATE TABLE customers (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insere dados iniciais
INSERT INTO customers (first_name, last_name, email) VALUES
('John', 'Doe', 'john.doe@example.com'),
('Jane', 'Smith', 'jane.smith@example.com'),
('Robert', 'Johnson', 'robert.johnson@example.com');

-- Remove o usuário se já existir (para recriar com as configurações corretas)
DROP USER IF EXISTS 'kafka'@'%';

-- Cria o usuário com autenticação mysql_native_password (mais compatível)
CREATE USER 'kafka'@'%' IDENTIFIED WITH mysql_native_password BY 'kafka';

-- Concede as permissões necessárias (sintaxe corrigida)
GRANT RELOAD, REPLICATION CLIENT, REPLICATION SLAVE ON *.* TO 'kafka'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON kafka_db.* TO 'kafka'@'%';
FLUSH PRIVILEGES;