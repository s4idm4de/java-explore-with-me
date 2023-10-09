CREATE TABLE  IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR,
    email VARCHAR,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT unique_users UNIQUE (name)
);

CREATE TABLE  IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT unique_categories UNIQUE (name)
);

CREATE TABLE  IF NOT EXISTS events
(
    annotation VARCHAR(2000),

    category_id BIGINT,

    confirmed_requests BIGINT,

    created_on TIMESTAMP WITHOUT TIME ZONE,

    description VARCHAR(7000),

    event_date TIMESTAMP WITHOUT TIME ZONE,

    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,

    initiator_id BIGINT,

    lat REAL,

    lon REAL,

    paid BOOL,

    participant_limit BIGINT,

    published_on TIMESTAMP WITHOUT TIME ZONE,

    request_moderation BOOL,

    state VARCHAR,

    title VARCHAR,

    views BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (id),

    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,

    FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
        created TIMESTAMP WITHOUT TIME ZONE,

        event_id BIGINT,

        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,

        requester_id BIGINT,

        status VARCHAR,
        CONSTRAINT pk_requests PRIMARY KEY (id),

        FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,

        FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOL,
    title VARCHAR,
    CONSTRAINT pk_compilations PRIMARY KEY (id)

);

CREATE TABLE IF NOT EXISTS events_compilations (
    compilations_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE,
    event_id BIGINT REFERENCES events (id) ON DELETE CASCADE,
    PRIMARY KEY (event_id, compilations_id),
    CONSTRAINT event_compilation UNIQUE (event_id, compilations_id)

);