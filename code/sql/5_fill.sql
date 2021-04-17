-- Passwords are hashed using SHA-256
INSERT INTO USERS(username, pass) VALUES('user1', 'e6c3da5b206634d7f3f3586d747ffdb36b5c675757b380c6a5fe5c570c714349'), -- pass1
                                        ('user2', '1ba3d16e9881959f8c9a9762854f72c6e6321cdd44358a10a4e939033117eab9'), -- pass2
                                        ('user3', '3acb59306ef6e660cf832d1d34c4fba3d88d616f0bb5c2a9e0f82d18ef6fc167'); -- pass3

INSERT INTO PROJECT(name, description, author) VALUES('project 1', 'description of project 1', 1),
                                                     ('project 2', 'description of project 2', 2),
                                                     ('project 3', 'description of project 3', 3);
                                                      
INSERT INTO LABEL(name, project, author) VALUES('label 1', 1, 1),
                                               ('label 2', 1, 1),
                                               ('label 3', 2, 2),
                                               ('label 4', 2, 2),
                                               ('label 5', 3, 3),
                                               ('label 6', 3, 3);
                         
INSERT INTO STATE(project, name, is_start, author) VALUES(1, 'start state', TRUE, 1),
                                                         (2, 'start state', TRUE, 2),
                                                         (3, 'start state', TRUE, 3);
                                                 
-- Allow transitions from 'start state' to state 'closed' in all projects
INSERT INTO STATETRANSITION VALUES(7, 1),
                                  (8, 3),
                                  (9, 5);
                                  
INSERT INTO ISSUE(project, name, description, author) VALUES(1, 'issue 1', 'description of issue 1', 1),
                                                            (1, 'issue 2', 'description of issue 2', 2),
                                                            (2, 'issue 3', 'description of issue 3', 3);
                                                    
INSERT INTO ISSUE_LABEL(iid, lid) VALUES(1, 1),
                                        (1, 2),
                                        (3, 3);
                              
INSERT INTO COMMENT(iid, text, author) VALUES(1, 'First comment', 1);