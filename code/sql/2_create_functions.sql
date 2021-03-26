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

CREATE FUNCTION func_check_state()
RETURNS TRIGGER AS
$$
	DECLARE
		oldStartState INT;
	BEGIN
		SELECT sid INTO oldStartState -- Value discarded
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