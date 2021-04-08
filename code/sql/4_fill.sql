INSERT INTO PROJECT VALUES('project 1', 'description of project 1'),
                          ('project 2', 'description of project 2'),
                          ('project 3', 'description of project 3');
                          
INSERT INTO LABEL VALUES('label 1', 'project 1'),
                        ('label 2', 'project 1'),
                        ('label 3', 'project 2'),
                        ('label 4', 'project 2'),
                        ('label 5', 'project 3'),
                        ('label 6', 'project 3');
                         
INSERT INTO STATE(project, name, is_start) VALUES('project 1', 'start state', TRUE),
                                                 ('project 2', 'start state', TRUE),
                                                 ('project 3', 'start state', TRUE);
                                                 
-- Allow transitions from 'start state' to state 'closed' in all projects
INSERT INTO STATETRANSITION VALUES(7, 1),
                                  (8, 3),
                                  (9, 5);
                                  
INSERT INTO ISSUE(project, name, description) VALUES('project 1', 'issue 1', 'description of issue 1'),
                                                    ('project 1', 'issue 2', 'description of issue 2'),
                                                    ('project 2', 'issue 3', 'description of issue 3');
                                                    
INSERT INTO ISSUE_LABEL VALUES(1, 'label 1', 'project 1'),
                              (1, 'label 2', 'project 1'),
                              (3, 'label 3', 'project 2');
                              
INSERT INTO COMMENT(iid, text) VALUES(1, 'First comment');