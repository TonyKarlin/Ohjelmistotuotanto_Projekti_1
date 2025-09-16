-- Create the database (run this separately if needed)
-- CREATE DATABASE otp_project;
-- DROP DATABASE IF EXISTS otp_project;

-- Connect to the database (in psql, run this after creating the database)
-- \c otp-project

-- Create users table
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create contacts table
-- Stores user contacts (friends)
-- Each contact is a reference to another user in the users table
CREATE TABLE IF NOT EXISTS contacts
(
    contact_id      BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    contact_user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_contact_user FOREIGN KEY (contact_user_id) REFERENCES users (id)
);


-- Create conversation table
CREATE TABLE IF NOT EXISTS conversations
(
    conversation_id BIGSERIAL PRIMARY KEY,
    type            VARCHAR(20) NOT NULL DEFAULT 'PRIVATE',
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE conversation_type AS ENUM ('PRIVATE', 'GROUP');

-- Create conversation_participants table
CREATE TABLE IF NOT EXISTS conversation_participants
(
    conversation_id BIGINT      NOT NULL,
    user_id         BIGINT      NOT NULL,
    role            VARCHAR(50) NOT NULL DEFAULT 'MEMBER',
    joined_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (conversation_id, user_id),
    CONSTRAINT fk_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (conversation_id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TYPE conversation_role AS ENUM ('ADMIN', 'OWNER', 'MODERATOR', 'MEMBER');



-- Create messages table
-- Stores messages sent between users
-- Each message has a sender and a receiver, both referencing the users table
CREATE TABLE IF NOT EXISTS messages
(
    id              BIGSERIAL PRIMARY KEY,
    sender_id       BIGINT    NOT NULL,
    content         TEXT      NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    conversation_id BIGINT    NOT NULL REFERENCES conversations (conversation_id) ON DELETE CASCADE,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create message_content table
-- Stores binary content for messages (e.g., images, files)
-- Each entry is linked to a message via message_id
CREATE TABLE message_content
(
    id         BIGSERIAL PRIMARY KEY,
    message_id BIGINT NOT NULL REFERENCES messages (id) ON DELETE CASCADE,
    data       BYTEA,
    file_type  VARCHAR(50)
);

-- Indexes for messages table
-- Works much faster when searching for messages by sender or receiver
-- Example: SELECT * FROM messages WHERE sender_id = 1;
-- Example: SELECT * FROM messages WHERE receiver_id = 2;
CREATE INDEX idx_messages_sender ON messages (sender_id);


-- Create Spring Session tables
CREATE TABLE SPRING_SESSION
(
    PRIMARY_ID            CHAR(36) NOT NULL,
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    EXPIRY_TIME           BIGINT   NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BYTEA        NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);