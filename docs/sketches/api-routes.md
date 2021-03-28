# API Routes Sketch

## Projects
```
GET    /api/projects                                        => Get all projects

GET    /api/projects/{project-name}                         => Get project details
PUT    /api/projects/{project-name}                         => Creates/Edits a project
```


## Labels
```
GET    /api/projects/{project-name}/labels                  => Get all project labels

PUT    /api/projects/{project-name}/labels/{label-name}     => Create/Edit label for project
DELETE /api/projects/{project-name}/labels/{label-name}     => Delete label for project
```


## States
```
GET    /api/projects/{project-name}/states                  => Get all project states

GET    /api/projects/{project-name}/states/{state-name}     => Get state details (possible transitions)
PUT    /api/projects/{project-name}/states/{state-name}     => Create/Edit state for project
DELETE /api/projects/{project-name}/states/{state-name}     => Delete state for project
```


## Issues
```
GET    /api/projects/{project-name}/issues                  => Get issues of project
POST   /api/projects/{project-name}/issues                  => Creates a new issue for the project

GET    /api/projects/{project-name}/issues/{iid}            => Get issue details (maybe without comments)
PUT    /api/projects/{project-name}/issues/{iid}            => Edit issue (edit close date, labels or change state)

GET    /api/projects/{project-name}/issues/{iid}/comments   => Get comments of issue
```