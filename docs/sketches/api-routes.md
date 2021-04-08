# API Routes Sketch

## Projects
```
GET    /api/projects                                                    => Get all projects
GET    /api/projects/{project-name}                                     => Get project details

PUT    /api/projects                                                    => Create project
PUT    /api/projects/{project-name}                                     => Edit a project
DELETE /api/projects/{project-name}                                     => Delete a project
```


## Labels
```
GET    /api/projects/{project-name}/labels                              => Get all project labels

PUT    /api/projects/{project-name}/labels                              => Create label for project
DELETE /api/projects/{project-name}/labels/{label-name}                 => Delete label for project
```


## States
```
GET    /api/projects/{project-name}/states                              => Get all project states
GET    /api/projects/{project-name}/states/{state-name}                 => Get state details (possible transitions)

PUT    /api/projects/{project-name}/states                              => Create state for project
PUT    /api/projects/{project-name}/states/{state-name}                 => Edit state for project
DELETE /api/projects/{project-name}/states/{state-name}                 => Delete state for project
```


## Issues
```
GET    /api/projects/{project-name}/issues                              => Get issues of project
GET    /api/projects/{project-name}/issues/{iid}                        => Get issue details (maybe without comments)
GET    /api/projects/{project-name}/issues/{iid}/comments               => Get comments of issue

POST   /api/projects/{project-name}/issues                              => Create issue for the project
PUT    /api/projects/{project-name}/issues/{iid}                        => Edit issue (edit close date, labels or change state)
DELETE /api/projects/{project-name}/issues/{iid}                        => Delete issue

PUT    /api/projects/{project-name}/issues/{iid}/labels                 => Adds a label to the issue
DELETE /api/projects/{project-name}/issues/{iid}/labels/{label-name}    => Removes a label

POST   /api/projects/{project-name}/issues/{iid}/comments               => Create a comment for the issue
PUT    /api/projects/{project-name}/issues/{iid}/comments/{cid}         => Edit a comment
DELETE /api/projects/{project-name}/issues/{iid}/comments/{cid}         => Delete a comment
```