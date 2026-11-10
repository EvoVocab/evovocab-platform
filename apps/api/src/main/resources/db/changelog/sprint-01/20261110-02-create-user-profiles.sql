-- liquibase formatted sql

-- changeset phanvanmanh:20261110-02-create-user-profiles
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE LOWER(table_name) = 'user_profiles';
CREATE TABLE IF NOT EXISTS user_profiles (
    account_id BINARY(16) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_user_profiles PRIMARY KEY (account_id),
    CONSTRAINT fk_user_profiles_user_accounts FOREIGN KEY (account_id) 
        REFERENCES user_accounts (id) 
        ON DELETE CASCADE
);
-- rollback DROP TABLE IF EXISTS user_profiles;