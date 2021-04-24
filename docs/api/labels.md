# Labels

A label is a name that classifies an issue. Issues can have zero or more labels. The set of allowed labels is [defined per project](#create-label).

## Properties
* `id` - Unique and stable global identifier of a label
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `number` - Stable identifier of a label relative to a project
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `name` - Short name that defines the label. The name is unique per project
    * mandatory
    * editable
    * type: **text**
    * example: `"My Label"`
* `project` - Name of the project where the label is contained
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"My Project"`
* `author` - Name of the label's creator
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"John Doe"`

## Link Relations
* [self](#get-label)
* [project](projects.md#get-project)
* [author](users.md#get-user)
* [labels](#list-labels)

## Actions
* [List Labels](#list-labels)
* [Get Label](#get-label)
* [Create Label](#create-label)
* [Edit Label](#edit-label)
* [Delete Label](#delete-label)
* [List Issue Labels](#list-issue-labels)
* [Add Label To Issue](#add-label-to-issue)
* [Remove Label From Issue](#remove-label-from-issue)

------
### List Labels
List all project labels, in the order that they were created.

```http
GET /api/projects/{projectId}/labels
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
  "class": ["label", "collection"],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["label"],
      "rel": ["item"],
      "properties": {
        "id": 1,
        "number": 1,
        "name": "label 1",
        "project": "project 1",
        "author": "user1"
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/labels/1"
        },
        {
          "rel": ["project"],
          "href": "http://localhost:8080/api/projects/1"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/1"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/1/labels"
        }
      ]
    },
    {
      "class": ["label"],
      "rel": ["item"],
      "properties": {
        "id": 2,
        "number": 2,
        "name": "label 2",
        "project": "project 1",
        "author": "user1"
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/labels/2"
        },
        {
          "rel": ["project"],
          "href": "http://localhost:8080/api/projects/1"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/1"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/1/labels"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-label",
      "title": "Create Label",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1/labels",
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
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/labels?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "http://localhost:8080/api/projects/1/labels{?page,limit}"
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
### Get Label
Get a single project label.

```http
GET /api/projects/{projectId}/labels/{labelNumber}
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId   | integer     | path       | The project's unique identifier                                                       |
| labelNumber | integer     | path       | The label's identifier relative to the project                                        |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["label"],
  "properties": {
    "id": 1,
    "number": 1,
    "name": "label 1",
    "project": "project 1",
    "author": "user1"
  },
  "actions": [
    {
      "name": "edit-label",
      "title": "Edit Label",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1/labels/1",
      "type": "application/x-www-form-urlencoded",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "labelNumber",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "name",
          "type": "text"
        }
      ]
    },
    {
      "name": "delete-label",
      "title": "Delete Label",
      "method": "DELETE",
      "href": "http://localhost:8080/api/projects/1/labels/1",
      "fields": [
        {
          "name": "projectId",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "labelNumber",
          "type": "hidden",
          "value": 1
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/labels/1"
    },
    {
      "rel": ["project"],
      "href": "http://localhost:8080/api/projects/1"
    },
    {
      "rel": ["author"],
      "href": "http://localhost:8080/api/users/1"
    },
    {
      "rel": ["labels"],
      "href": "http://localhost:8080/api/projects/1/labels"
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
### Create Label
Create a project label.

```http
PUT /api/projects/{projectId}/labels
```

#### Parameters
| Name         | Type        | In         | Description                                                                           |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                  |
| projectId    | integer     | path       | The project's unique identifier                                                       |
| name         | string      | body       | **Required**. Unique (within the project) and short name that defines the label       |

#### Default Response
```
Status: 201 Created
Location: /api/projects/{projectId}/labels/{labelNumber}
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
### Edit Label
Edit an already existing project label.

```http
PUT /api/projects/{projectId}/labels/{labelNumber}
```

#### Parameters
| Name         | Type        | In         | Description                                                                                                                          |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                                                           |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                                                                 |
| projectId    | integer     | path       | The project's unique identifier                                                                                                      |
| labelNumber  | integer     | path       | The label's identifier relative to the project                                                                                       |
| name         | string      | body       | **Required**. Unique and short name that defines the label                                                                           |

#### Default Response
```
Status: 200 OK
Location: /api/projects/{projectId}/labels/{labelNumber}
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
### Delete Label
Delete an existing project label.

```http
DELETE /api/projects/{projectId}/labels/{labelNumber}
```

#### Parameters
| Name         | Type        | In         | Description                                                                                                                          |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`                                                           |
| projectId    | integer     | path       | The project's unique identifier                                                                                                      |
| labelNumber  | integer     | path       | The label's identifier relative to the project                                                                                       |

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

------
### List Issue Labels
List the issue's current labels, in the order that they were created.

```http
GET /api/projects/{projectId}/issues/{issueNumber}/labels
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId   | integer     | path       | The project's unique identifier                                                       |
| issueNumber | integer     | path       | The issue's identifier relative to the project                                        |
| page        | integer     | query      | Specifies the current page of the list                                                |
| limit       | integer     | query      | Specifies the number of results per page (max. 100)                                   |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["label", "collection"],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["label"],
      "rel": ["item"],
      "properties": {
        "id": 1,
        "number": 1,
        "name": "label 1",
        "project": "project 1",
        "author": "user1"
      },
      "actions": [
        {
          "name": "delete-label-from-issue",
          "title": "Delete Label From Issue",
          "method": "DELETE",
          "href": "http://localhost:8080/api/projects/1/issues/1/labels/1",
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
              "name": "labelNumber",
              "type": "hidden",
              "value": 1
            }
          ]
        }
      ],
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/issues/1/labels/1"
        },
        {
          "rel": ["issue"],
          "href": "http://localhost:8080/api/projects/1/issues/1"
        },
        {
          "rel": ["project"],
          "href": "http://localhost:8080/api/projects/1"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/1"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/1/issues/1/labels"
        }
      ]
    },
    {
      "class": ["label"],
      "rel": ["item"],
      "properties": {
        "id": 2,
        "number": 2,
        "name": "label 2",
        "project": "project 1",
        "author": "user1"
      },
      "actions": [
        {
          "name": "delete-label-from-issue",
          "title": "Delete Label From Issue",
          "method": "DELETE",
          "href": "http://localhost:8080/api/projects/1/issues/1/labels/2",
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
              "name": "labelNumber",
              "type": "hidden",
              "value": 2
            }
          ]
        }
      ],
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/projects/1/issues/1/labels/2"
        },
        {
          "rel": ["issue"],
          "href": "http://localhost:8080/api/projects/1/issues/1"
        },
        {
          "rel": ["project"],
          "href": "http://localhost:8080/api/projects/1"
        },
        {
          "rel": ["author"],
          "href": "http://localhost:8080/api/users/1"
        },
        {
          "rel": ["labels"],
          "href": "http://localhost:8080/api/projects/1/issues/1/labels"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "add-label-to-issue",
      "title": "Add Label To Issue",
      "method": "PUT",
      "href": "http://localhost:8080/api/projects/1/issues/1/labels",
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
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/projects/1/issues/1/labels?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "http://localhost:8080/api/projects/1/issues/1/labels{?page,limit}"
    },
    {
      "rel": ["project"],
      "href": "http://localhost:8080/api/projects/1"
    },
    {
      "rel": ["issue"],
      "href": "http://localhost:8080/api/projects/1/issues/1"
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
### Add Label To Issue
Add a existing label to an issue. 

```http
PUT /api/projects/{projectId}/issues/{issueNumber}/labels
```

#### Parameters
| Name         | Type        | In         | Description                                                                           |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| content-type | string      | header     | Should be set to `application/x-www-form-urlencoded`                                  |
| projectId    | integer     | path       | The project's unique identifier                                                       |
| issueNumber  | integer     | path       | The issue's identifier relative to the project                                        |
| name         | string      | body       | **Required**. Unique (within the project) and short name that defines the label       |

#### Default Response
```
Status: 201 Created
Location: /api/projects/{projectId}/issues/{issueNumber}/labels
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
### Remove Label From Issue
Remove a existing label from an issue. 

```http
DELETE /api/projects/{projectId}/issues/{issueNumber}/labels/{labelNumber}
```

#### Parameters
| Name         | Type        | In         | Description                                                                           |
| ------------ | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept       | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId    | integer     | path       | The project's unique identifier                                                       |
| issueNumber  | integer     | path       | The issue's identifier relative to the project                                        |
| labelNumber  | integer     | path       | The label's identifier relative to the project                                        |

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