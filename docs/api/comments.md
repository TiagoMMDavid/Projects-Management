# Comments

A comment is a short text, used to comment an issue. An issue can have a sequence of comments.

## Properties
* `id` - Unique and stable global identifier of a comment
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `number` - Stable identifier of a comment relative to an issue
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `content` - Content of the comment (short text)
    * mandatory
    * editable
    * type: **text**
    * example: `"This is my comment"`
* `createDate` - Date of the comment's creation (in ISO format)
    * mandatory
    * non editable, auto-assigned
    * type: **datetime**
    * example: `"2021-04-20T20:00:00.123456+01:00"`
* `issue` - Name of the issue where the comment was inserted
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"issue 1"`
* `issueNumber` - Stable identifier of a issue relative to a project
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `project` - Name of the project where the issue is inserted
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"project 1"`
* `projectId` - Unique and stable global identifier of a project
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `author` - Name of the comment's creator
    * mandatory
    * non editable, auto-assigned
    * type: **text**
    * example: `"John Doe"`
* `authorId` - Unique and stable global identifier of the comment's creator
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`

## Link relations
* [self](#get-comment)
* [issue](issues.md#get-issue)
* [author](users.md#get-user)
* [comments](#list-comments)

## Actions
* [List Comments](#list-comment)
* [Get Comment](#get-comment)
* [Create Comment](#create-comment)
* [Edit Comment](#edit-comment)
* [Delete Comment](#delete-comment)

------
### List Comments
List all created comments in an issue, in the order that they were created.

```http
GET /api/projects/{projectId}/issues/{issueNumber}/comments
```

#### Parameters
| Name          | Type        | In         | Description                                                                           |
| -----------   | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept        | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId     | integer     | path       | The project's unique identifier                                                       |
| issueNumber   | integer     | path       | The issue's identifier relative to the project                                        |
| page          | integer     | query      | Specifies the current page of the list                                                |
| limit         | integer     | query      | Specifies the number of results per page (max. 100)                                   |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["comment", "collection"],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["comment"],
      "rel": ["item"],
      "properties": {
        "id": 1,
        "number": 1,
        "content": "First comment",
        "createDate": "2021-04-20T20:00:00.123456+01:00",
        "issue": "issue 1",
        "issueNumber": 1,
        "project": "project 1",
        "projectId": 1,
        "author": "user1",
        "authorId": 1
      },
      "links": [
        {
          "rel": ["self"],
          "href": "/api/projects/1/issues/1/comments/1"
        },
        {
          "rel": ["issue"],
          "href": "/api/projects/1/issues/1"
        },
        {
          "rel": ["author"],
          "href": "/api/users/1"
        },
        {
          "rel": ["comments"],
          "href": "/api/projects/1/issues/1/comments"
        }
      ]
    },
    {
      "class": ["comment"],
      "rel": ["item"],
      "properties": {
        "id": 2,
        "number": 2,
        "content": "Second comment",
        "createDate": "2021-04-20T21:00:00.123456+01:00",
        "issue": "issue 1",
        "issueNumber": 1,
        "project": "project 1",
        "projectId": 1,
        "author": "user1",
        "authorId": 1
      },
      "links": [
        {
          "rel": ["self"],
          "href": "/api/projects/1/issues/1/comments/2"
        },
        {
          "rel": ["issue"],
          "href": "/api/projects/1/issues/1"
        },
        {
          "rel": ["author"],
          "href": "/api/users/1"
        },
        {
          "rel": ["comments"],
          "href": "/api/projects/1/issues/1/comments"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-comment",
      "title": "Create Comment",
      "method": "POST",
      "href": "/api/projects/1/issues/1/comments",
      "type": "application/json",
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
          "name": "content",
          "type": "text"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/api/projects/1/issues/1/comments?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "/api/projects/1/issues/1/comments{?page,limit}"
    },
    {
      "rel": ["project"],
      "href": "/api/projects/1"
    },
    {
      "rel": ["issue"],
      "href": "/api/projects/1/issues/1"
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
### Get Comment
Get a single comment.

```http
GET /api/projects/{projectId}/issues/{issueNumber}/comments/{commentNumber}
```

#### Parameters
| Name              | Type        | In         | Description                                                                           |
| -----------       | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept            | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId         | integer     | path       | The project's unique identifier                                                       |
| issueNumber       | integer     | path       | The issue's identifier relative to the project                                        |
| commentNumber     | integer     | path       | The comment's identifier relative to the issue                                        |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["comment"],
  "properties": {
    "id": 1,
    "number": 1,
    "content": "First comment",
    "createDate": "2021-04-20T20:00:00.123456+01:00",
    "issue": "issue 1",
    "issueNumber": 1,
    "project": "project 1",
    "projectId": 1,
    "author": "user1",
    "authorId": 1
  },
  "actions": [
    {
      "name": "edit-comment",
      "title": "Edit Comment",
      "method": "PUT",
      "href": "/api/projects/1/issues/1/comments/1",
      "type": "application/json",
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
          "name": "commentNumber",
          "type": "hidden",
          "value": 1
        },
        {
          "name": "content",
          "type": "text"
        }
      ]
    },
    {
      "name": "delete-comment",
      "title": "Delete Comment",
      "method": "DELETE",
      "href": "/api/projects/1/issues/1/comments/1",
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
          "name": "commentNumber",
          "type": "hidden",
          "value": 1
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/api/projects/1/issues/1/comments/1"
    },
    {
      "rel": ["issue"],
      "href": "/api/projects/1/issues/1"
    },
    {
      "rel": ["author"],
      "href": "/api/users/1"
    },
    {
      "rel": ["comments"],
      "href": "/api/projects/1/issues/1/comments"
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
### Create Comment
Add a new comment to an existing issue.

```http
POST /api/projects/{projectId}/issues/{issueNumber}/comments 
```

#### Parameters
| Name          | Type        | In         | Description                                                                           |
| -----------   | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept        | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| content-type  | string      | header     | Should be set to `application/json`                                                   |
| projectId     | integer     | path       | The project's unique identifier                                                       |
| issueNumber   | integer     | path       | The issue's identifier relative to the project                                        |
| content       | string      | body       | **Required**. The content of the comment                                              |

#### Default Response
```
Status: 201 Created
Location: /api/projects/{projectId}/issues/{issueNumber}/{commentNumber}
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

#### Forbidden
```
Status: 403 Forbidden
```

------
### Edit Comment
Edit an already existing comment.

```http
PUT /api/projects/{projectId}/issues/{issueNumber}/comments/{commentNumber}
```

#### Parameters
| Name          | Type        | In         | Description                                                                           |
| -----------   | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept        | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| content-type  | string      | header     | Should be set to `application/json`                                                   |
| projectId     | integer     | path       | The project's unique identifier                                                       |
| issueNumber   | integer     | path       | The issue's identifier relative to the project                                        |
| content       | string      | body       | **Required**. The content of the comment                                              |

#### Default Response
```
Status: 200 OK
Location: /api/projects/{projectId}/issues/{issueNumber}/{commentNumber}
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

#### Forbidden
```
Status: 403 Forbidden
```

------
### Delete Comment
Delete an existing comment.

```http
DELETE /api/projects/{projectId}/issues/{issueNumber}/comments/{commentNumber}
```

#### Parameters
| Name              | Type        | In         | Description                                                                           |
| -----------       | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept            | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| projectId         | integer     | path       | The project's unique identifier                                                       |
| issueNumber       | integer     | path       | The issue's identifier relative to the project                                        |
| commentNumber     | integer     | path       | The comment's identifier relative to the issue                                        |

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

#### Forbidden Operation
```
Status: 403 Forbidden
```

#### Resource Not Found
```
Status: 404 Not Found
```
