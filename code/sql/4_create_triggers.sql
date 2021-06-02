-- Trigger to insert the 'closed' and 'archived' state in a new project
CREATE TRIGGER trig_insert_default_states
AFTER INSERT ON PROJECT
FOR EACH ROW
EXECUTE PROCEDURE func_insert_default_states();

-- Trigger to insert the defined start state on an issue
CREATE TRIGGER trig_insert_start_state
BEFORE INSERT ON ISSUE
FOR EACH ROW
EXECUTE PROCEDURE func_insert_start_state();

-- Trigger to check if there is another start state and to disable modifications to 'closed' and 'archived' states
CREATE TRIGGER trig_validate_state
BEFORE INSERT OR UPDATE ON STATE
FOR EACH ROW
WHEN (pg_trigger_depth() = 0)
EXECUTE PROCEDURE func_validate_state();

-- Trigger to control state transitions
CREATE TRIGGER trig_validate_state_transitions
BEFORE INSERT ON STATETRANSITION
FOR EACH ROW
EXECUTE PROCEDURE func_validate_state_transitions();

-- Trigger to update close_date when state changes to 'closed' and check if transition is valid
CREATE TRIGGER trig_check_issue_update
BEFORE UPDATE ON ISSUE
FOR EACH ROW
EXECUTE PROCEDURE func_check_issue_update();

-- Trigger to check if comment can be inserted (issue state is not 'archived')
CREATE TRIGGER trig_check_comment_validity
BEFORE INSERT OR UPDATE ON COMMENT
FOR EACH ROW
EXECUTE PROCEDURE func_check_comment_validity();

CREATE TRIGGER trig_create_project_seq
BEFORE INSERT ON PROJECT
FOR EACH ROW 
EXECUTE PROCEDURE func_create_project_seq();

CREATE TRIGGER trig_cleanup_project_seq
BEFORE DELETE ON PROJECT
FOR EACH ROW 
EXECUTE PROCEDURE func_cleanup_project_seq();

CREATE TRIGGER trig_get_issue_number
BEFORE INSERT ON ISSUE
FOR EACH ROW 
EXECUTE PROCEDURE func_get_issue_number();

CREATE TRIGGER trig_cleanup_issue_seq
BEFORE DELETE ON ISSUE
FOR EACH ROW 
EXECUTE PROCEDURE func_cleanup_issue_seq();

CREATE TRIGGER trig_get_state_number
BEFORE INSERT ON STATE
FOR EACH ROW 
EXECUTE PROCEDURE func_get_state_number();

CREATE TRIGGER trig_get_label_number
BEFORE INSERT ON LABEL
FOR EACH ROW 
EXECUTE PROCEDURE func_get_label_number();

CREATE TRIGGER trig_get_comment_number
BEFORE INSERT ON COMMENT
FOR EACH ROW 
EXECUTE PROCEDURE func_get_comment_number();
