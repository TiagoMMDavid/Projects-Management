# Projects

A project is a long-running development activity, such as "DAW Project" or "LS Project".

## Properties
* `id` - Unique and stable global identifier of a project
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `name` - Unique and short name that defines the project
    * mandatory
    * editable
    * type: **text**
    * example: `"My Project"`
* `description` - Short description that characterizes the project
    * mandatory
    * editable
    * type: **text**
    * example: `"This is my project"`
* `author` - Name of the project's creator
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"John Doe"`
* `authorId` - Unique and stable global identifier of the project's creator
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`

## Link Relations
* [self](#get-project)
* [labels](labels.md#list-labels)
* [issues](issues.md#list-issues)
* [states](states.md#list-states)
* [author](users.md#get-user)
* [projects](#list-projects)

## Actions
* [List Projects](#list-projects)
* [Get Project](#get-project)
* [Create Project](#create-project)
* [Edit Project](#edit-project)
* [Delete Project](#delete-project)

------
### List Projects
List all created projects, in the order that they were created.

```http
GET /api/projects
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| page        | integer     | query      | Specifies the current page of the list                                                |
| limit       | integer     | query      | Specifies the number of results per page (max. 100)                                   |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["project", "collection"],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["project"],
      "rel": ["item"],
      "properties": {
        "id": 1,
        "name": "project 1",
        "description": "description of project 1",
        "author": "user1",
        "authorId": 1
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/1/labels"
        },
        {
          "rel": ["issues"],
          "href": "http://localhost:8080/api/projects/1/issues"
        },
        {
          "rel": ["states"],
          "href": "http://localhost:8080/api/projects/1/states"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/1"
        },
        {
          "rel": ["projects"],
          "href": "http://localhost:8080/api/projects"
        }
      ]
    },
    {
      "class": ["project"],
      "rel": ["item"],
      "properties": {
        "id": 2,
        "name": "project 2",
        "description": "description of project 2",
        "author": "user2",
        "authorId": 2
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/2"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/2/labels"
        },
        {
          "rel": ["issues"],
          "href": "http://localhost:8080/api/projects/2/issues"
        },
        {
          "rel": ["states"],
          "href": "http://localhost:8080/api/projects/2/states"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/2"
        },
        {
          "rel": ["projects"],
          "href": "http://localhost:8080/api/projects"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-project",
      "title": "Create Project",
      "method": "POST",
      "href": "http://localhost:8080/api/projects",
      "type": "application/json",
      "fields": [
        {
          "name": "name",
          "type": "text"
        },
        {
          "name": "description",
          "type": "text"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "http://localhost:8080/api/projects{?page,limit}"
    }
  ]
}
```
#### Bad Request
```
Status: 400 Bad Request
```

#### Requires Authentication
```
Status: 401 Unauthorized
```

------
### Get Project
Get a single project.

```http
GET /api/projects/{projectId}
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId   | integer     | path       | The project's unique identifier                                                       |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["project"],
  "properties": {
    "id": 1,
    "name": "project 1",
    "description": "description of project 1",
    "author": "user1",
    "authorId": 1
  },
  "actions": [
    {
      "name": "edit-project",
      "title": "Edit Project",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1",
      "type": "application/json",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "name",
          "type": "text"
        },
        {
          "name": "description",
          "type": "text"
        }
      ]
    },
    {
      "name": "delete-project",
      "title": "Delete Project",
      "method": "DELETE",
      "href": "http://localhost:8080/api/projects/1",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1"
    },
    {
      "rel": ["labels"],
      "href": "http://localhost:8080/api/projects/1/labels"
    },
    {
      "rel": ["issues"],
      "href": "http://localhost:8080/api/projects/1/issues"
    },
    {
      "rel": ["states"],
      "href": "http://localhost:8080/api/projects/1/states"
    },
    {
      "rel": ["author"],
      "href": "http://localhost:8080/api/users/1"
    },
    {
      "rel": ["projects"],
      "href": "http://localhost:8080/api/projects"
    }
  ]
}

```
#### Bad Request
```
Status: 400 Bad Request
```

#### Requires Authentication
```
Status: 401 Unauthorized
```

#### Resource Not Found
```
Status: 404 Not Found
```

------
### Create Project
Create a project.

```http
POST /api/projects
```

#### Parameters
| Name         | Type        | In         | Description                                                                           |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| content-type | string      | header     | Should be set to `application/json`                                                   |
| name         | string      | body       | **Required**. Unique and short name that defines the project                          |
| description  | string      | body       | **Required**. Short description that characterizes the project                        |

#### Default Response
```
Status: 201 Created
Location: /api/projects/{projectId}
```

#### Bad Request
```
Status: 400 Bad Request
```

#### Requires Authentication
```
Status: 401 Unauthorized
```

#### Conflict
```
Status: 409 Conflict
```

------
### Edit Project
Edit an already existing project.

```http
PUT /api/projects/{projectId}
```

#### Parameters
| Name         | Type        | In         | Description                                                                                      |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------------------ |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                       |
| content-type | string      | header     | Should be set to `application/json`                                                              |
| projectId    | integer     | path       | The project's unique identifier                                                                  |
| name         | string      | body       | **Required unless you provide `description`**. Unique and short name that defines the project    |
| description  | string      | body       | **Required unless you provide `name`**. Short description that characterizes the project         |

#### Default Response
```
Status: 200 OK
Location: /api/projects/{projectId}
```

#### Bad Request
```
Status: 400 Bad Request
```

#### Requires Authentication
```
Status: 401 Unauthorized
```

#### Resource Not Found
```
Status: 404 Not Found
```

#### Conflict
```
Status: 409 Conflict
```

------
### Delete Project
Delete an existing project.

```http
DELETE /api/projects/{projectId}
```

#### Parameters
| Name         | Type        | In         | Description                                                                    |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------ |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`     |
| projectId    | integer     | path       | The project's unique identifier                                                |

#### Default Response
```
Status: 200 OK
```

#### Bad Request
```
Status: 400 Bad Request
```

#### Requires Authentication
```
Status: 401 Unauthorized
```

#### Resource Not Found
```
Status: 404 Not Found
```
