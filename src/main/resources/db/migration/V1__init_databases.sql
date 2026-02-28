CREATE SCHEMA IF NOT EXISTS account;

CREATE TABLE IF NOT EXISTS account.user(
    id UUID PRIMARY KEY ,
    email varchar(254) NOT NULL UNIQUE ,
    role varchar(20) NOT NULL ,
    password varchar(260) NOT NULL ,
    firstName varchar(100) NOT NULL ,
    lastName varchar(100) NOT NULL ,
    is_enabled BOOLEAN DEFAULT FALSE,
    is_email_verified BOOLEAN DEFAULT FALSE
);

-- 1. domain_event_entry (события)
CREATE TABLE IF NOT EXISTS domain_event_entry (
    global_index          BIGSERIAL PRIMARY KEY,
    event_identifier      VARCHAR(255) NOT NULL UNIQUE,
    meta_data             BYTEA        NOT NULL,
    payload               BYTEA        NOT NULL,
    payload_revision      VARCHAR(255),
    payload_type          VARCHAR(255) NOT NULL,
    time_stamp            VARCHAR(255) NOT NULL,
    aggregate_identifier  VARCHAR(255) NOT NULL,
    sequence_number       BIGINT       NOT NULL,
    type                  VARCHAR(255) NOT NULL,
    UNIQUE (aggregate_identifier, sequence_number, type)
    );

-- 2. snapshot_event_entry (снапшоты)
CREATE TABLE IF NOT EXISTS snapshot_event_entry (
                                                    event_identifier      VARCHAR(255) NOT NULL UNIQUE,
    meta_data             BYTEA        NOT NULL,
    payload               BYTEA        NOT NULL,
    payload_revision      VARCHAR(255),
    payload_type          VARCHAR(255) NOT NULL,
    time_stamp            VARCHAR(255) NOT NULL,
    aggregate_identifier  VARCHAR(255) NOT NULL,
    sequence_number       BIGINT       NOT NULL,
    type                  VARCHAR(255) NOT NULL,
    PRIMARY KEY (aggregate_identifier, sequence_number, type)
    );

-- 3. token_entry (токены процессоров событий)
CREATE TABLE IF NOT EXISTS token_entry (
                                           processor_name  VARCHAR(255) NOT NULL,
    segment         INTEGER      NOT NULL,
    owner           VARCHAR(255),
    timestamp       VARCHAR(255) NOT NULL,
    token           BYTEA,
    token_type      VARCHAR(255),
    PRIMARY KEY (processor_name, segment)
    );

-- 4. saga_entry (саги)
CREATE TABLE IF NOT EXISTS saga_entry (
                                          saga_id         VARCHAR(255) PRIMARY KEY,
    revision        VARCHAR(255),
    saga_type       VARCHAR(255),
    serialized_saga BYTEA
    );

-- 5. association_value_entry (ассоциации саг)
CREATE TABLE IF NOT EXISTS association_value_entry (
                                                       id               BIGINT       PRIMARY KEY,
                                                       association_key  VARCHAR(255) NOT NULL,
    association_value VARCHAR(255),
    saga_id          VARCHAR(255) NOT NULL,
    saga_type        VARCHAR(255)
    );

CREATE SEQUENCE IF NOT EXISTS association_value_entry_seq START WITH 1 INCREMENT BY 50;

-- 6. dead_letter_entry (Dead Letter Queue — твоя версия + полная из Axon)
CREATE TABLE IF NOT EXISTS dead_letter_entry (
                                                 dead_letter_id        VARCHAR(255)          PRIMARY KEY,
    cause_message         VARCHAR(1023),
    cause_type            VARCHAR(255),
    diagnostics           BYTEA,
    enqueued_at           TIMESTAMPTZ           NOT NULL,
    last_touched          TIMESTAMPTZ,
    aggregate_identifier  VARCHAR(255),
    event_identifier      VARCHAR(255)          NOT NULL,
    message_type          VARCHAR(255)          NOT NULL,
    meta_data             BYTEA,
    payload               BYTEA                 NOT NULL,
    payload_revision      VARCHAR(255),
    payload_type          VARCHAR(255)          NOT NULL,
    sequence_number       BIGINT,
    time_stamp            VARCHAR(255)          NOT NULL,
    token                 BYTEA,
    token_type            VARCHAR(255),
    type                  VARCHAR(255),
    processing_group      VARCHAR(255)          NOT NULL,
    processing_started    TIMESTAMPTZ,
    sequence_identifier   VARCHAR(255)          NOT NULL,
    sequence_index        BIGINT                NOT NULL,

    CONSTRAINT uk_dead_letter_unique UNIQUE (processing_group, sequence_identifier, sequence_index)
    );

-- Индексы для dead_letter (рекомендуются)
CREATE INDEX IF NOT EXISTS idx_dead_letter_processing_group    ON dead_letter_entry (processing_group);
CREATE INDEX IF NOT EXISTS idx_dead_letter_sequence_identifier ON dead_letter_entry (sequence_identifier);