# API Routes Sketch

## Projects
```
GET    /api/projects                                                        => Get all projects
GET    /api/projects/{project-id}                                           => Get project details

PUT    /api/projects                                                        => Create project
PUT    /api/projects/{project-id}                                           => Edit a project
DELETE /api/projects/{project-id}                                           => Delete a project
```


## Labels
```
GET    /api/projects/{project-id}/labels                                        => Get all project labels
GET    /api/projects/{project-id}/labels/{label-id}                             => Get label details

PUT    /api/projects/{project-id}/labels                                        => Create label for project
DELETE /api/projects/{project-id}/labels/{label-id}                             => Delete label for project

PUT    /api/projects/{project-id}/issues/{issue-id}/labels                      => Adds a label to the issue
DELETE /api/projects/{project-id}/issues/{issue-id}/labels/{label-id}           => Removes a label
```


## States
```
GET    /api/projects/{project-id}/states                                        => Get all project states
GET    /api/projects/{project-id}/states/{state-id}                             => Get state details
GET    /api/projects/{project-id}/states/{state-id}/nextStates                  => Get the states {state-id} can transition to

PUT    /api/projects/{project-id}/states                                        => Create state for project
PUT    /api/projects/{project-id}/states/{state-id}                             => Edit state for project (without nextStates)
DELETE /api/projects/{project-id}/states/{state-id}                             => Delete state for project

PUT    /api/projects/{project-id}/states/{state-id}/nextStates                  => Add next state to {state-id}
DELETE /api/projects/{project-id}/states/{state-id}/nextStates/${nextState-id}  => Delete next state {nextState-id} of {state-id} 
```


## Issues
```
GET    /api/projects/{project-id}/issues                                        => Get issues of project
GET    /api/projects/{project-id}/issues/{issue-id}                             => Get issue details

POST   /api/projects/{project-id}/issues                                        => Create issue for the project
PUT    /api/projects/{project-id}/issues/{issue-id}                             => Edit issue (edit close date, labels or change state)
DELETE /api/projects/{project-id}/issues/{issue-id}                             => Delete issue
```

## Comments
```
GET    /api/projects/{project-id}/issues/{issue-id}/comments                    => Get comments of issue
GET    /api/projects/{project-id}/issues/{issue-id}/comments/{comment-id}       => Get comment details


POST   /api/projects/{project-id}/issues/{issue-id}/comments                    => Create a comment for the issue
PUT    /api/projects/{project-id}/issues/{issue-id}/comments/{comment-id}       => Edit a comment
DELETE /api/projects/{project-id}/issues/{issue-id}/comments/{comment-id}       => Delete a comment
```