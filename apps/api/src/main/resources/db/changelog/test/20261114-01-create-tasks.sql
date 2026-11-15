-- liquibase formatted sql

-- changeset osamuharu:20261114-01-create-tasks
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE LOWER(table_name) = 'tasks';
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT NOT NULL,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(200) NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

-- rollback DROP TABLE IF EXISTS tasks;

