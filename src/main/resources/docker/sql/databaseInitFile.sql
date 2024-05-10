CREATE DATABASE IF NOT EXISTS BankDB;

use BankDB;

DROP TABLE IF EXISTS bank_account;
DROP TABLE IF EXISTS operation;

CREATE TABLE bank_account (
    accountId BIGINT AUTO_INCREMENT PRIMARY KEY,
    accountNumber BINARY(16) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    overdraft DECIMAL(19, 2) NOT NULL DEFAULT 0
);

CREATE TABLE operation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    type_operation VARCHAR(20) NOT NULL,
    montant DECIMAL(19, 2) NOT NULL,
    date_operation TIMESTAMP NOT NULL,
    FOREIGN KEY (account_id) REFERENCES compte_bancaire(id)
);
