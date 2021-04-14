CREATE TABLE USERS
(
    uid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username    VARCHAR(16) UNIQUE NOT NULL,
    pass        VARCHAR(128) NOT NULL -- hashed
);

CREATE TABLE PROJECT
(
    pid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    description VARCHAR(256) NOT NULL,
    
    author      INT REFERENCES USERS(uid) NOT NULL
);

CREATE TABLE STATE
(
    sid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project     INT REFERENCES PROJECT(pid) NOT NULL,
    name        VARCHAR(64) NOT NULL,
    is_start    BOOLEAN NOT NULL DEFAULT false,
    
    author      INT REFERENCES USERS(uid) NOT NULL,
    UNIQUE (name, project)
);

CREATE TABLE STATETRANSITION
(
    from_sid    INT REFERENCES STATE(sid),
    to_sid      INT REFERENCES STATE(sid),
    PRIMARY KEY (from_sid, to_sid)
);

CREATE TABLE ISSUE
(
    iid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    state       INT REFERENCES STATE(sid) NOT NULL,
    project     INT REFERENCES PROJECT(pid) NOT NULL,
    
    name        VARCHAR(64) NOT NULL,
    description VARCHAR(256) NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    close_date  TIMESTAMP,
    
    author      INT REFERENCES USERS(uid) NOT NULL
);

CREATE TABLE COMMENT
(
    cid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    iid         INT REFERENCES ISSUE(iid) NOT NULL,
    
    text        VARCHAR(256) NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    author      INT REFERENCES USERS(uid) NOT NULL
);

CREATE TABLE LABEL
(
    lid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(64) NOT NULL,
    project     INT REFERENCES PROJECT(pid),
    
    author      INT REFERENCES USERS(uid) NOT NULL,
    UNIQUE (name, project)
);

CREATE TABLE ISSUE_LABEL
(
    iid         INT REFERENCES ISSUE(iid),
    lid         INT REFERENCES LABEL(lid),
    PRIMARY KEY (iid, lid)
);