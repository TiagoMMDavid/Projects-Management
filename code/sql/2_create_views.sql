CREATE VIEW V_PROJECT AS 
SELECT 
    pid, 
    name, 
    description,
    
    uid AS author_id, 
    username AS author_name
FROM PROJECT JOIN USERS ON author = uid;

CREATE VIEW V_STATE AS 
SELECT 
    sid, 
    STATE.number AS number,
    STATE.name AS name, 
    is_start,
    
    project AS project_id, 
    PROJECT.name AS project_name, 
    
    uid AS author_id, 
    username AS author_name    
FROM STATE JOIN USERS ON author = uid 
JOIN PROJECT ON STATE.project = PROJECT.pid;

CREATE VIEW V_ISSUE AS 
SELECT 
    iid,
    ISSUE.number AS number,
    ISSUE.name AS name,
    ISSUE.description AS description,
    create_date,
    close_date,
    
    state AS state_id,
    STATE.name AS state_name,
    STATE.number AS state_number,
    
    PROJECT.pid AS project_id,
    PROJECT.name AS project_name,
    
    uid AS author_id, 
    username AS author_name
FROM ISSUE JOIN USERS ON author = uid 
JOIN PROJECT ON ISSUE.project = PROJECT.pid
JOIN STATE ON STATE.sid = state;

CREATE VIEW V_COMMENT AS 
SELECT 
    cid,
    COMMENT.number AS number,
    text,
    COMMENT.create_date AS create_date,
    
    COMMENT.iid AS issue_id,
    ISSUE.name AS issue_name,
    ISSUE.number AS issue_number,
    
    V_ISSUE.project_id AS project_id,
    V_ISSUE.project_name AS project_name,
    
    uid AS author_id, 
    username AS author_name    
FROM COMMENT JOIN USERS ON author = uid 
JOIN V_ISSUE ON V_ISSUE.iid = COMMENT.iid;

CREATE VIEW V_LABEL AS 
SELECT 
    lid, 
    LABEL.number AS number,
    LABEL.name AS name, 
    
    project AS project_id, 
    PROJECT.name AS project_name, 
    
    uid AS author_id, 
    username AS author_name    
FROM LABEL JOIN USERS ON author = uid 
JOIN PROJECT ON LABEL.project = PROJECT.pid;