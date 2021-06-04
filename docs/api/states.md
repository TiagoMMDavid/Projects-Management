# States

A state characterizes the particular condition of an issue in a given time. A set of states must define a single start state. The set of available states and the set of possible transitions between states are defined per project.

## Properties
* `id` - Unique and stable global identifier of a state
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `number` - Stable identifier of a state relative to a project
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `name` - Unique name of the state within a project
    * mandatory
    * editable
    * type: **text**
    * example: `"This is my state"`
* `isStartState` - Indicates if the state is a start state or not (there can only be one start state per project)
    * mandatory
    * editable
    * type: **boolean**
    * example: `false`    
* `projectId` - Id of the project where the label is contained
    * mandatory
    * non editable, auto-assigned
    * type: **nuymber**
    * example: `1`
* `author` - Name of the label's creator
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"John Doe"`
* `authorId` - Id of the label's creator
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`

## Link Relations
* [self](#get-state)
* [author](users.md#get-user)
* [project](projects.md#get-project)
* [nextStates](#get-next-states)
* [states](#list-states)

## Actions
* [List States](#list-states)
* [Get State](#get-state)
* [Get Next States](#get-next-states)
* [Create State](#create-state)
* [Edit State](#edit-state)
* [Delete State](#delete-state)
* [Create Next State](#create-next-state)
* [Delete Next State](#delete-next-state)

------
### List States
List all created states in a project, in the order that they were created.

```http
GET /api/projects/{projectId}/states
```

#### Parameters
| Name          | Type        | In         | Description                                                                           |
| -----------   | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept        | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId     | integer     | path       | The project's unique identifier                                                       |
| page          | integer     | query      | Specifies the current page of the list                                                |
| limit         | integer     | query      | Specifies the number of results per page (max. 100)                                   |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": [
    "state",
    "collection"
  ],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["state"],
      "rel": ["item"],
      "properties": {
        "id": 1,
        "number": 1,
        "name": "closed",
        "isStartState": false,
        "project": "project 1",
        "projectId": 1,
        "author": "user1",
        "authorId": 1
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/states/1"
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
          "rel": ["nextStates"],
          "href": "http://localhost:8080/api/projects/1/states/1/nextStates"
        },
        {
          "rel": ["states"],
          "href": "http://localhost:8080/api/projects/1/states"
        }
      ]
    },
    {
      "class": ["state"],
      "rel": ["item"],
      "properties": {
        "id": 2,
        "number": 2,
        "name": "archived",
        "isStartState": false,
        "project": "project 1",
        "projectId": 1,
        "author": "user1",
        "authorId": 1
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/states/2"
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
          "rel": ["nextStates"],
          "href": "http://localhost:8080/api/projects/1/states/2/nextStates"
        },
        {
          "rel": ["states"],
          "href": "http://localhost:8080/api/projects/1/states"
        }
      ]
    },
  ],
  "actions": [
    {
      "name": "create-state",
      "title": "Create State",
      "method": "POST",
      "href": "http://localhost:8080/api/projects/1/states",
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
          "name": "isStart",
          "type": "checkbox"
        }
      ]
    },
      {
      "class": ["state"],
      "rel": ["item"],
      "properties": {
        "id": 7,
        "number": 3,
        "name": "start state",
        "isStartState": true,
        "project": "project 1",
        "projectId": 1,
        "author": "user1",
        "authorId": 1
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/states/3"
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
          "rel": ["nextStates"],
          "href": "http://localhost:8080/api/projects/1/states/3/nextStates"
        },
        {
          "rel": ["states"],
          "href": "http://localhost:8080/api/projects/1/states"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/states?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "http://localhost:8080/api/projects/1/states{?page,limit}"
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
### Get State
Get a single state.

```http
GET /api/projects/{projectId}/states/{stateNumber}
```

#### Parameters
| Name          | Type        | In         | Description                                                                           |
| -----------   | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept        | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId     | integer     | path       | The project's unique identifier                                                       |
| stateNumber   | integer     | path       | The state's unique identifier relative to the project                                 |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["state"],
  "properties": {
    "id": 3,
    "number": 3,
    "name": "example state",
    "isStartState": false,
    "project": "project 1",
    "projectId": 1,
    "author": "user1",
    "authorId": 1
  },
  "actions": [
    {
      "name": "edit-state",
      "title": "Edit State",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1/states/3",
      "type": "application/x-www-form-urlencoded",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "stateNumber",
          "type": "hidden",
          "value": 3
        },
        {
          "name": "name",
          "type": "text"
        },
        {
          "name": "isStart",
          "type": "checkbox"
        }
      ]
    },
    {
      "name": "delete-state",
      "title": "Delete State",
      "method": "DELETE",
      "href": "http://localhost:8080/api/projects/1/states/3",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "stateNumber",
          "type": "hidden",
          "value": 3
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/states/1"
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
      "rel": ["nextStates"],
      "href": "http://localhost:8080/api/projects/1/states/1/nextStates"
    },
    {
      "rel": ["states"],
      "href": "http://localhost:8080/api/projects/1/states"
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
### Get Next States
Get the possible state transitions for a single state.

```http
GET /api/projects/{projectId}/states/{stateNumber}/nextStates
```

#### Parameters
| Name          | Type        | In         | Description                                                                           |
| -----------   | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept        | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId     | integer     | path       | The project's unique identifier                                                       |
| stateNumber   | integer     | path       | The state's unique identifier relative to the project                                 |
| page          | integer     | query      | Specifies the current page of the list                                                |
| limit         | integer     | query      | Specifies the number of results per page (max. 100)                                   |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": [
    "state",
    "collection"
  ],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["state"],
      "rel": ["item"],
      "properties": {
        "id": 3,
        "number": 3,
        "name": "example state",
        "isStartState": false,
        "project": "project 1",
        "projectId": 1,
        "author": "user1",
        "authorId": 1
      },
      "actions": [
        {
          "name": "delete-next-state",
          "title": "Delete Next State",
          "method": "DELETE",
          "href": "/api/projects/1/states/1/nextStates/3",
          "type": "application/x-www-form-urlencoded",
          "fields": [
            {
              "name": "projectId",
              "type": "hidden",
              "value": 1
            },
            {
              "name": "stateNumber",
              "type": "hidden",
              "value": 1
            },
            {
              "name": "nextStateNumber",
              "type": "hidden",
              "value": 3
            }
          ]
        },
        {
      "class": ["state"],
      "rel": ["item"],
      "properties": {
        "id": 4,
        "number": 4,
        "name": "another state",
        "isStartState": false,
        "project": "project 1",
        "projectId": 1,
        "author": "user1",
        "authorId": 1
      },
      "actions": [
        {
          "name": "delete-next-state",
          "title": "Delete Next State",
          "method": "DELETE",
          "href": "/api/projects/1/states/1/nextStates/4",
          "type": "application/x-www-form-urlencoded",
          "fields": [
            {
              "name": "projectId",
              "type": "hidden",
              "value": 1
            },
            {
              "name": "stateNumber",
              "type": "hidden",
              "value": 1
            },
            {
              "name": "nextStateNumber",
              "type": "hidden",
              "value": 4
            }
          ]
        }

      ],
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/states/3"
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
          "rel": ["nextStates"],
          "href": "http://localhost:8080/api/projects/1/states/3/nextStates"
        },
        {
          "rel": ["states"],
          "href": "http://localhost:8080/api/projects/1/states"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-next-state",
      "title": "Create Next State",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1/states/1/nextStates",
      "type": "application/x-www-form-urlencoded",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "stateNumber",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "state",
          "type": "text"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/states/1/nextStates?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "http://localhost:8080/api/projects/1/states/1/nextStates{?page,limit}"
    },
    {
      "rel": ["project"],
      "href": "http://localhost:8080/api/projects/1"
    },
    {
      "rel": ["state"],
      "href": "http://localhost:8080/api/projects/1/states/1"
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
### Create State
Create a state in a project.

```http
POST /api/projects/{projectId}/states
```

#### Parameters
| Name         | Type        | In         | Description                                                                           |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                  |
| projectId    | integer     | path       | The project's unique identifier                                                       |
| name         | string      | body       | **Required**. Unique name (within the project) that defines the state                 |
| isStart      | boolean     | body       | **Required**. Indicates if the state is a start state or not                          |

#### Default Response
```
Status: 201 Created
Location: /api/projects/{projectId}/states/{stateNumber}
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
### Edit State
Edit a state in a project.

```http
PUT /api/projects/{projectId}/states/{stateNumber}
```

#### Parameters
| Name         | Type        | In         | Description                                                                                         |
| ------------ | ----------- | ---------- | ----------------------------------------------------------------------------------------------------|
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                          |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                                |
| projectId    | integer     | path       | The project's unique identifier                                                                     |
| stateNumber  | integer     | path       | The state's unique identifier relative to the project                                               |
| name         | string      | body       | **Required unless you provide `isStart`** Unique name (within the project) that defines the state   |
| isStart      | boolean     | body       | **Required unless you provide `name`** Indicates if the state is a start state or not               |

#### Default Response
```
Status: 200 OK
Location: /api/projects/{projectId}/states/{stateNumber}
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
### Delete State
Delete a state in a project.

```http
DELETE /api/projects/{projectId}/states/{stateNumber}
```

#### Parameters
| Name         | Type        | In         | Description                                                                                         |
| ------------ | ----------- | ---------- | ----------------------------------------------------------------------------------------------------|
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                          |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                                |
| projectId    | integer     | path       | The project's unique identifier                                                                     |
| stateNumber  | integer     | path       | The state's unique identifier relative to the project                                               |

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

------
### Create Next State
Add a state transition to a given state.

```http
PUT /api/projects/{projectId}/states/{stateNumber}/nextStates
```

#### Parameters
| Name         | Type        | In         | Description                                                                                         |
| ------------ | ----------- | ---------- | ----------------------------------------------------------------------------------------------------|
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                          |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                                |
| projectId    | integer     | path       | The project's unique identifier                                                                     |
| stateNumber  | integer     | path       | The state's unique identifier relative to the project                                               |
| state        | string      | body       | **Required** Name of the state to add                                                               |        

#### Default Response
```
Status: 201 OK
Location: /api/projects/{projectId}/states/{stateNumber}/nextStates/
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
### Delete Next State
Delete a state transition from a given state.

```http
DELETE /api/projects/{projectId}/states/{stateNumber}/nextStates/{nextStateNumber}
```

#### Parameters
| Name              | Type        | In         | Description                                                                                         |
| ----------------- | ----------- | ---------- | ----------------------------------------------------------------------------------------------------|
| accept            | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                          |
| content-type      | string      | header     | Should be set to `application/x-www-form-urlencoded`                                                |
| projectId         | integer     | path       | The project's unique identifier                                                                     |
| stateNumber       | integer     | path       | The state's unique identifier relative to the project                                               |
| nextStateNumber   | integer     | path       | The state transition's unique identifier relative to the project                                    |

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
