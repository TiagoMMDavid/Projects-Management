# Users

A user is a person who is authorized to use the API.

## Properties
* `id` - Unique and stable global identifier of a user
    * mandatory
    * non editable, auto-assigned
    * type: **number**
    * example: `1`
* `name` - Unique and short name that defines a user
    * mandatory
    * non editable
    * type: **text**
    * example: `"user1"`

## Link Relations
* [self](#get-user)
* [users](#list-users)

## Actions
* [List Users](#list-users)
* [Get User](#get-user)
* [Get Authenticated User](#get-authenticated-user)

------
### List Users
List all existing users, in the order that they were created.

```http
GET /api/users
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
  "class": ["user", "collection"],
  "properties": {
    "collectionSize": 2,
    "pageIndex": 0,
    "pageSize": 2
  },
  "entities": [
    {
      "class": ["user"],
      "rel": ["item"],
      "properties": {
        "id": 1,
        "name": "user1"
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/users/1"
        },
        {
          "rel": ["users"],
          "href": "http://localhost:8080/api/users"
        }
      ]
    },
    {
      "class": ["user"],
      "rel": ["item"],
      "properties": {
        "id": 2,
        "name": "user2"
      },
      "links": [
        {
          "rel": ["self"],
          "href": "http://localhost:8080/api/users/2"
        },
        {
          "rel": ["users"],
          "href": "http://localhost:8080/api/users"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/users?page=0&limit=10"
    },
    {
      "rel": ["page"],
      "hrefTemplate": "http://localhost:8080/api/users{?page,limit}"
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
### Get User
Get a single user.

```http
GET /api/users/{userId}
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |
| userId      | integer     | path       | The user's unique identifier                                                          |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["user"],
  "properties": {
    "id": 1,
    "name": "user1"
  },
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/users/1"
    },
    {
      "rel": ["users"],
      "href": "http://localhost:8080/api/users"
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
### Get Authenticated User
Get the currently authenticated user.

```http
GET /api/user
```

#### Parameters
| Name        | Type        | In         | Description                                                                           |
| ----------- | ----------- | ---------- | ------------------------------------------------------------------------------------- |
| accept      | string      | header     | Should be set to either `application/json` or `application/vnd.siren+json`            |

#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["user"],
  "properties": {
    "id": 1,
    "name": "user1"
  },
  "links": [
    {
      "rel": ["self"],
      "href": "http://localhost:8080/api/users/1"
    },
    {
      "rel": ["users"],
      "href": "http://localhost:8080/api/users"
    }
  ]
}
```

#### Requires Authentication
```
Status: 401 Unauthorized
```