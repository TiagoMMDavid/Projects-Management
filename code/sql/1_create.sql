CREATE TABLE PROJECT
(
    name        VARCHAR(64) PRIMARY KEY,
    description VARCHAR(256) NOT NULL
);

-- Unique names per project is assured via a trigger
CREATE TABLE STATE
(
    sid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project     VARCHAR(64) REFERENCES PROJECT(name) NOT NULL,
    name        VARCHAR(64) NOT NULL,
    is_start    BOOLEAN NOT NULL DEFAULT FALSE
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
    project     VARCHAR(64) REFERENCES PROJECT(name) NOT NULL,
    
    name        VARCHAR(64) NOT NULL,
    description VARCHAR(256) NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    close_date  TIMESTAMP
);

CREATE TABLE COMMENT
(
    cid         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    iid         INT REFERENCES ISSUE(iid) NOT NULL,
    
    text        VARCHAR(256) NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE LABEL
(
    name        VARCHAR(64),
    project     VARCHAR(64) REFERENCES PROJECT(name),
    PRIMARY KEY (project, name)
);

CREATE TABLE ISSUE_LABEL
(
    iid         INT REFERENCES ISSUE(iid),
    label_name  VARCHAR(64),
    label_proj  VARCHAR(64),
    PRIMARY KEY (iid, label_name, label_proj),
    FOREIGN KEY (label_name, label_proj) REFERENCES LABEL(name, project)
);