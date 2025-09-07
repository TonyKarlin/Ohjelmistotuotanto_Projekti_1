-- Create the database (run this separately if needed)
CREATE DATABASE otp_project;

-- Connect to the database (in psql, run this after creating the database)
-- \c otp-project

-- Create users table
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL,
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


-- DROP TABLE IF EXISTS message_attachments;
-- DROP TABLE IF EXISTS messages CASCADE;
-- DROP INDEX idx_messages_sender;
-- DROP INDEX idx_messages_receiver;
-- DROP TABLE IF EXISTS conversations;
-- DROP TABLE IF EXISTS contacts;

-- Create messages table
-- Stores messages sent between users
-- Each message has a sender and a receiver, both referencing the users table
CREATE TABLE IF NOT EXISTS messages
(
    id              BIGSERIAL PRIMARY KEY,
    sender_id       BIGINT    NOT NULL,
    receiver_id     BIGINT    NOT NULL,
    content         TEXT      NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    conversation_id BIGINT    NOT NULL,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES users (id),
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES users (id),
    CONSTRAINT fk_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (conversation_id)
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
CREATE INDEX idx_messages_receiver ON messages (receiver_id);

-- Create conversation table
CREATE TABLE IF NOT EXISTS conversations
(
    conversation_id BIGSERIAL PRIMARY KEY
);

-- Create message_attachments table
-- Stores attachments for messages
-- Each attachment is linked to a message via message_id
-- Supports multiple attachments per message
CREATE TABLE IF NOT EXISTS message_attachments
(
    id         BIGSERIAL PRIMARY KEY,
    message_id BIGINT      NOT NULL REFERENCES messages (id) ON DELETE CASCADE,
    file_type  VARCHAR(50) NOT NULL,
    file_data  BYTEA       NOT NULL
);

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