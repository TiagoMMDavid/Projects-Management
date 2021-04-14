INSERT INTO USERS(username, pass) VALUES('User1', '123'),
                                        ('User2', '456'),
                                        ('User3', '789');

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