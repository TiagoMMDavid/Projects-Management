DROP TRIGGER IF EXISTS trig_insert_default_states ON PROJECT;
DROP TRIGGER IF EXISTS trig_insert_start_state ON ISSUE;
DROP TRIGGER IF EXISTS trig_check_state ON STATE;
DROP TRIGGER IF EXISTS trig_check_issue_update ON ISSUE;
DROP TRIGGER IF EXISTS trig_check_comment_validity ON COMMENT;

DROP FUNCTION IF EXISTS func_insert_default_states;
DROP FUNCTION IF EXISTS func_insert_start_state;
DROP FUNCTION IF EXISTS func_check_state;
DROP FUNCTION IF EXISTS func_check_issue_update;
DROP FUNCTION IF EXISTS func_check_comment_validity;

DROP TABLE IF EXISTS ISSUE_LABEL;
DROP TABLE IF EXISTS LABEL;
DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS ISSUE;
DROP TABLE IF EXISTS STATETRANSITION;
DROP TABLE IF EXISTS STATE;
DROP TABLE IF EXISTS PROJECT;