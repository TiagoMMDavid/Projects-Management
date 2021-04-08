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

-- Trigger to check if it's a duplicate state and if there is another start state
CREATE TRIGGER trig_check_state
BEFORE INSERT OR UPDATE ON STATE
FOR EACH ROW
WHEN (pg_trigger_depth() = 0)
EXECUTE PROCEDURE func_check_state();

-- Trigger to update close_date when state changes to 'closed' and check if transition is valid
CREATE TRIGGER trig_check_issue_update
BEFORE UPDATE ON ISSUE
FOR EACH ROW
EXECUTE PROCEDURE func_check_issue_update();