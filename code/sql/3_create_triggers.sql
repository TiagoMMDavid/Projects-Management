CREATE TRIGGER trig_insert_default_states
AFTER INSERT ON PROJECT
FOR EACH ROW
EXECUTE PROCEDURE func_insert_default_states();

CREATE TRIGGER trig_insert_start_state
BEFORE INSERT ON ISSUE
FOR EACH ROW
EXECUTE PROCEDURE func_insert_start_state();

CREATE TRIGGER trig_check_state
BEFORE INSERT OR UPDATE ON STATE
FOR EACH ROW
WHEN (pg_trigger_depth() = 0)
EXECUTE PROCEDURE func_check_state();