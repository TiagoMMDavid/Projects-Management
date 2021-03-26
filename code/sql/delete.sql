-- Delete table entries
DELETE FROM ISSUE_LABEL;
DELETE FROM LABEL;
DELETE FROM COMMENT;
DELETE FROM ISSUE;
DELETE FROM STATETRANSITION;
DELETE FROM STATE;
DELETE FROM PROJECT;

-- Restart identity columns
ALTER SEQUENCE STATE_sid_seq restart;
ALTER SEQUENCE ISSUE_iid_seq restart;
ALTER SEQUENCE COMMENT_cid_seq restart;
ALTER SEQUENCE LABEL_lid_seq restart;