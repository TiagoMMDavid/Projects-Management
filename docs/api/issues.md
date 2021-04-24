# Issues

An issue is a task that needs to be done in the context of a project, such as adding a new functionality, resolve an error, add a test, create a final release. An issue always exists in the context of a project.

## Properties
* `id` - Unique and stable global identifier of an issue
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `number` - Stable identifier of an issue relative to a project
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `name` - Short name that defines an issue
    * mandatory
    * editable
    * type: **text**
    * example: `"My Project"`
* `description` - Short description that characterizes the issue
    * mandatory
    * editable
    * type: **text**
    * example: `"This is my project"`
* `createDate` - Date of the issue's creation (in ISO format)
    * mandatory
    * non editable, auto-assigned
    * type: **datetime**
    * example: `"2021-04-20T20:00:00.123456+01:00"`
* `closeDate` - Date of the issue's closure (in ISO format)
    * mandatory when state is 'closed'
    * non editable, auto-assigned
    * type: **datetime**
    * example: `"2021-04-20T20:00:00.123456+01:00"`
* `state` - Name of the issue's current state
    * mandatory
    * editable
    * type: **text**
    * example: `"closed"`
* `project` - Name of the project where the issue is contained
    * mandatory
    * non editable
    * type: **text**
    * example: `"project 1"`
* `author` - Name of the issue's creator
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"John Doe"`

## Link Relations
* [self](#get-issue)
* [state](states.md#list-states)
* [comments](comments.md#list-comments)
* [labels](labels.md#list-issue-labels)
* [author](users.md#get-user)
* [project](projects.md#list-projects)
* [issues](#list-issues)

## Actions
* [List Issues](#list-issues)
* [Get Issue](#get-project)
* [Create Issue](#create-project)
* [Edit Issue](#edit-project)
* [Delete Issue](#delete-project)

------
### List Issues
List all project issues, in the order that they were created.

```http
GET /api/projects/{projectId}/issues
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId   | integer     | path       | The project's unique identifier                                                       |
| page        | integer     | query      | Specifies the current page of the list                                                |
| limit       | integer     | query      | Specifies the number of results per page (max. 100)                                   |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["issue", "collection"],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["issue"],
      "rel": ["item"],
      "properties": {
        "id": 1,
        "number": 1,
        "name": "issue 1",
        "description": "description of issue 1",
        "createDate": "2021-04-24T14:41:01.798825+01:00",
        "closeDate": null,
        "state": "start state",
        "project": "project 1",
        "author": "user1"
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/issues/1"
        },
        {
          "rel": ["state"],
          "href": "http://localhost:8080/api/projects/1/states/3"
        },
        {
          "rel": ["comments"],
          "href": "http://localhost:8080/api/projects/1/issues/1/comments"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/1/issues/1/labels"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/1"
        },
        {
          "rel": ["project"],
          "href": "http://localhost:8080/api/projects/1"
        },
        {
          "rel": ["issues"],
          "href": "http://localhost:8080/api/projects/1/issues"
        }
      ]
    },
    {
      "class": ["issue"],
      "rel": ["item"],
      "properties": {
        "id": 2,
        "number": 2,
        "name": "issue 2",
        "description": "description of issue 2",
        "createDate": "2021-04-24T14:41:01.798825+01:00",
        "closeDate": null,
        "state": "start state",
        "project": "project 1",
        "author": "user2"
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/issues/2"
        },
        {
          "rel": ["state"],
          "href": "http://localhost:8080/api/projects/1/states/3"
        },
        {
          "rel": ["comments"],
          "href": "http://localhost:8080/api/projects/1/issues/2/comments"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/1/issues/2/labels"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/2"
        },
        {
          "rel": ["project"],
          "href": "http://localhost:8080/api/projects/1"
        },
        {
          "rel": ["issues"],
          "href": "http://localhost:8080/api/projects/1/issues"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-issue",
      "title": "Create Issue",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1/issues",
      "type": "application/x-www-form-urlencoded",
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
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/issues?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "http://localhost:8080/api/projects/1/issues{?page,limit}"
    },
    {
      "rel": ["project"],
      "href": "http://localhost:8080/api/projects/1"
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
### Get Issue
Get a single issue from a project.

```http
GET /api/projects/{projectId}/issue/{issueNumber}
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId   | integer     | path       | The project's unique identifier                                                       |
| issueNumber | integer     | path       | The issue's identifier relative to the project                                        |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["issue"],
  "properties": {
    "id": 1,
    "number": 1,
    "name": "issue 1",
    "description": "description of issue 1",
    "createDate": "2021-04-24T14:41:01.798825+01:00",
    "closeDate": null,
    "state": "start state",
    "project": "project 1",
    "author": "user1"
  },
  "actions": [
    {
      "name": "edit-issue",
      "title": "Edit Issue",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1/issues/1",
      "type": "application/x-www-form-urlencoded",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "issueNumber",
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
        },
        {
          "name": "state",
          "type": "text"
        }
      ]
    },
    {
      "name": "delete-issue",
      "title": "Delete Issue",
      "method": "DELETE",
      "href": "http://localhost:8080/api/projects/1/issues/1",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "issueNumber",
          "type": "hidden",
          "value": 1
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/issues/1"
    },
    {
      "rel": ["state"],
      "href": "http://localhost:8080/api/projects/1/states/3"
    },
    {
      "rel": ["comments"],
      "href": "http://localhost:8080/api/projects/1/issues/1/comments"
    },
    {
      "rel": ["labels"],
      "href": "http://localhost:8080/api/projects/1/issues/1/labels"
    },
    {
      "rel": ["author"],
      "href": "http://localhost:8080/api/users/1"
    },
    {
      "rel": ["project"],
      "href": "http://localhost:8080/api/projects/1"
    },
    {
      "rel": ["issues"],
      "href": "http://localhost:8080/api/projects/1/issues"
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
### Create Issue
Create an issue inside the project. In order to successfully create an issue, the project must have a start state already defined.

```http
PUT /api/projects/{projectId}/issues
```

#### Parameters
| Name         | Type        | In         | Description                                                                           |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                  |
| projectId    | integer     | path       | The project's unique identifier                                                       |
| name         | string      | body       | **Required**. Short name that defines the issue                                       |
| description  | string      | body       | **Required**. Short description that characterizes the issue                          |

#### Default Response
```
Status: 201 Created
Location: /api/projects/{projectId}/issues/{issueNumber}
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
### Edit Issue
Edit an already existing issue.

```http
PUT /api/projects/{projectId}/issues/{issueNumber}
```

#### Parameters
| Name         | Type        | In         | Description                                                                                                               |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------------------------------------------- |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                                                |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                                                      |
| projectId    | integer     | path       | The project's unique identifier                                                                                           |
| issueNumber  | integer     | path       | The issue's identifier relative to the project                                                                            |
| name         | string      | body       | **Required unless you provide `description` or `state`**. Short name that defines the issue                               |
| description  | string      | body       | **Required unless you provide `name` or `state`**. Short description that characterizes the issue                         |
| state        | string      | body       | **Required unless you provide `name` or `description`**. Name of the state to transition to. Must be a valid transition   |

#### Default Response
```
Status: 200 OK
Location: /api/projects/{projectId}/issues/{issueNumber}
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
### Delete Issue
Delete an existing issue from a project.

```http
DELETE /api/projects/{projectId}/issues/{issueNumber}
```

#### Parameters
| Name         | Type        | In         | Description                                                                    |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------ |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`     |
| projectId    | integer     | path       | The project's unique identifier                                                |
| issueNumber  | integer     | path       | The issue's identifier relative to the project                                 |

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