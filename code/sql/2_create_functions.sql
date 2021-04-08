-- Function to insert the 'closed' and 'archived' state in a new project
CREATE FUNCTION func_insert_default_states()
RETURNS TRIGGER AS
$$
    DECLARE
        id1 INT;
        id2 INT;
    BEGIN
        INSERT INTO STATE(project, name, is_start) VALUES(NEW.name, 'closed', FALSE) RETURNING sid INTO id1;
        INSERT INTO STATE(project, name, is_start) VALUES(NEW.name, 'archived', FALSE) RETURNING sid INTO id2;
        INSERT INTO STATETRANSITION VALUES(id1, id2);
        RETURN NEW;
    END;
$$
LANGUAGE 'plpgsql';

-- Function to insert the defined start state on an issue
CREATE FUNCTION func_insert_start_state()
RETURNS TRIGGER AS
$$
    DECLARE
        state INT;
    BEGIN
        SELECT sid INTO state
        FROM STATE
        WHERE project = NEW.project AND is_start = TRUE;
        
        IF FOUND THEN
            NEW.state = state;
        ELSE
            RAISE 'No starting state defined in the project!';
        END IF;
        
        RETURN NEW;
    END;
$$
LANGUAGE 'plpgsql';

-- Function to check if it's a duplicate state and if there is another start state
CREATE FUNCTION func_check_state()
RETURNS TRIGGER AS
$$
    DECLARE
        oldStartState INT;
    BEGIN
        PERFORM sid
        FROM STATE
        WHERE name = NEW.name AND project = NEW.project;
        
        IF FOUND THEN
            RAISE 'Name already defined in this project states!';
        END IF;
        
        IF NEW.is_start THEN
            SELECT sid INTO oldStartState
            FROM STATE
            WHERE project = NEW.project AND is_start = TRUE;
        
            IF FOUND THEN
                UPDATE STATE SET is_start = FALSE WHERE STATE.sid = oldStartState;
            END IF;
        END IF;

        RETURN NEW;
    END;
$$
LANGUAGE 'plpgsql';

-- Function to update close_date when state changes to 'closed' and check if transition is valid
CREATE FUNCTION func_check_issue_update()
RETURNS TRIGGER AS
$$
    DECLARE
        stateName VARCHAR(64);
    BEGIN
        IF NEW.state != OLD.state THEN
            -- Check if transition is valid
            PERFORM from_sid
            FROM STATETRANSITION
            WHERE from_sid = OLD.state AND to_sid = NEW.state;
            
            IF NOT FOUND THEN
                RAISE 'Invalid transition!';
            END IF;
            
           -- Set close_date if state was changed to 'closed'
            SELECT name INTO stateName
            FROM STATE
            WHERE sid = NEW.state;
            
            IF stateName = 'closed' THEN
                NEW.close_date = CURRENT_TIMESTAMP;
            END IF;
        END IF;
        
        RETURN NEW;
    END;
$$
LANGUAGE 'plpgsql';