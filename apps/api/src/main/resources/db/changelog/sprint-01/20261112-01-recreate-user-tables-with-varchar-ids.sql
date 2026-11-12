-- liquibase formatted sql

-- changeset Codex:20261112-01-recreate-user-tables-with-varchar-ids
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS user_accounts;

CREATE TABLE user_accounts (
    id VARCHAR(36) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    deleted_at TIMESTAMP(6) NULL,
    CONSTRAINT pk_user_accounts PRIMARY KEY (id),
    CONSTRAINT uq_user_accounts_email UNIQUE (email)
);

CREATE TABLE user_profiles (
    account_id VARCHAR(36) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_user_profiles PRIMARY KEY (account_id),
    CONSTRAINT fk_user_profiles_user_accounts FOREIGN KEY (account_id)
        REFERENCES user_accounts (id) ON DELETE CASCADE
);

-- rollback DROP TABLE IF EXISTS user_profiles;
-- rollback DROP TABLE IF EXISTS user_accounts;
-- rollback CREATE TABLE user_accounts (id BINARY(16) NOT NULL, email VARCHAR(255) NOT NULL, password VARCHAR(255) NULL, status VARCHAR(20) NOT NULL, provider VARCHAR(50) NOT NULL, provider_id VARCHAR(255) NULL, created_at TIMESTAMP(6) NOT NULL, updated_at TIMESTAMP(6) NOT NULL, deleted_at TIMESTAMP(6) NULL, CONSTRAINT pk_user_accounts PRIMARY KEY (id), CONSTRAINT uq_user_accounts_email UNIQUE (email));
-- rollback CREATE TABLE user_profiles (account_id BINARY(16) NOT NULL, full_name VARCHAR(255) NOT NULL, created_at TIMESTAMP(6) NOT NULL, updated_at TIMESTAMP(6) NOT NULL, CONSTRAINT pk_user_profiles PRIMARY KEY (account_id), CONSTRAINT fk_user_profiles_user_accounts FOREIGN KEY (account_id) REFERENCES user_accounts (id) ON DELETE CASCADE);
