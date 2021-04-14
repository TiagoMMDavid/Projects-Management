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
    ISSUE.name AS name,
    ISSUE.description AS description,
    create_date,
    close_date,
    
    state AS state_id,
    STATE.name AS state_name,
    
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
    text,
    COMMENT.create_date AS create_date,
    
    COMMENT.iid AS issue_id,
    ISSUE.name AS issue_name,
    
    uid AS author_id, 
    username AS author_name    
FROM COMMENT JOIN USERS ON author = uid 
JOIN ISSUE ON ISSUE.iid = COMMENT.iid;

CREATE VIEW V_LABEL AS 
SELECT 
    lid, 
    LABEL.name AS name, 
    
    project AS project_id, 
    PROJECT.name AS project_name, 
    
    uid AS author_id, 
    username AS author_name    
FROM LABEL JOIN USERS ON author = uid 
JOIN PROJECT ON LABEL.project = PROJECT.pid;