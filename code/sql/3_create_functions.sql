-- Function to insert the 'closed' and 'archived' state in a new project
CREATE FUNCTION func_insert_default_states()
RETURNS TRIGGER AS
$$
    DECLARE
        id1 INT;
        id2 INT;
    BEGIN
        INSERT INTO STATE(project, name, is_start, author) VALUES(NEW.pid, 'closed', FALSE, NEW.author) RETURNING sid INTO id1;
        INSERT INTO STATE(project, name, is_start, author) VALUES(NEW.pid, 'archived', FALSE, NEW.author) RETURNING sid INTO id2;
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
            RAISE SQLSTATE 'P0005' USING message = 'No starting state defined in the project!';
        END IF;
        
        RETURN NEW;
    END;
$$
LANGUAGE 'plpgsql';

-- Function to check if there is another start state
CREATE FUNCTION func_validate_start_state()
RETURNS TRIGGER AS
$$
    DECLARE
        oldStartState INT;
    BEGIN
        IF NEW.is_start THEN
            SELECT sid INTO oldStartState
            FROM STATE
            WHERE project = NEW.project AND is_start = TRUE AND sid != NEW.sid;
        
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
                RAISE SQLSTATE 'P0006' USING message = 'Invalid transition!';
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

-- Function to check if comment can be inserted (issue state is not 'archived')
CREATE FUNCTION func_check_comment_validity()
RETURNS TRIGGER AS
$$
    DECLARE
        stateName VARCHAR(64);
    BEGIN
        -- Check issue state
        SELECT STATE.name into stateName
        FROM STATE JOIN ISSUE ON (STATE.sid = ISSUE.state)
        WHERE ISSUE.iid = NEW.iid;

        IF stateName = 'archived' THEN
            RAISE SQLSTATE 'P0007' USING message = 'Cannot add a comment to an archived issue!';
        END IF;
        
        RETURN NEW;
    END;
$$
LANGUAGE 'plpgsql';

CREATE FUNCTION func_create_project_seq()
RETURNS TRIGGER AS
$$
    BEGIN
        EXECUTE format('CREATE sequence issue_number_seq_%s', NEW.pid);
        EXECUTE format('CREATE sequence label_number_seq_%s', NEW.pid);
        EXECUTE format('CREATE sequence state_number_seq_%s', NEW.pid);
        RETURN NEW;
    END
$$
LANGUAGE 'plpgsql';

CREATE FUNCTION func_cleanup_project_seq()
RETURNS TRIGGER AS
$$
    BEGIN
        EXECUTE format('DROP sequence issue_number_seq_%s', OLD.pid);
        EXECUTE format('DROP sequence label_number_seq_%s', OLD.pid);
        EXECUTE format('DROP sequence state_number_seq_%s', OLD.pid);
        RETURN OLD;
    END
$$
LANGUAGE 'plpgsql';

CREATE FUNCTION func_get_issue_number()
RETURNS TRIGGER AS
$$
    BEGIN
        NEW.number = nextval('issue_number_seq_' || NEW.project);
        EXECUTE format('CREATE sequence comment_number_seq_%s', NEW.iid);
        RETURN NEW;
    END
$$
LANGUAGE 'plpgsql';

CREATE FUNCTION func_cleanup_issue_seq()
RETURNS TRIGGER AS
$$
    BEGIN
        EXECUTE format('DROP sequence comment_number_seq_%s', OLD.iid);
        RETURN OLD;
    END
$$
LANGUAGE 'plpgsql';

CREATE FUNCTION func_get_state_number()
RETURNS TRIGGER AS
$$
    BEGIN
        NEW.number = nextval('state_number_seq_' || NEW.project);
        RETURN NEW;
    END
$$
LANGUAGE 'plpgsql';

CREATE FUNCTION func_get_label_number()
RETURNS TRIGGER AS
$$
    BEGIN
        NEW.number = nextval('label_number_seq_' || NEW.project);
        RETURN NEW;
    END
$$
LANGUAGE 'plpgsql';

CREATE FUNCTION func_get_comment_number()
RETURNS TRIGGER AS
$$
    BEGIN
        NEW.number = nextval('comment_number_seq_' || NEW.iid);
        RETURN NEW;
    END
$$
LANGUAGE 'plpgsql';
