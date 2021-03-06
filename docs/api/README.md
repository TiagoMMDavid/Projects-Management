# Authorization
All API routes (besides [Home](#home)) require authentication to access them. For testing purposes, three users have been created:
* Username: user1 | Password: pass1
* Username: user2 | Password: pass2
* Username: user3 | Password: pass3

This authentication information is passed via the `Authorization` header in all requests, using the `Basic` scheme.
### Example
```
Authorization: Basic dXNlcjE6cGFzczE=
```

# Collections
Listing resources in the API (projects, issues...) returns an [application/vnd.siren+json](#references) representation containing the various existent resources. If there are more pages to the result set, there are extra link relations present:

* next - URI to the next page
* previous - URI to the previous page

These link relations are hidden when there are no next and/or previous pages.

# Home
The home resource presents information about the server application and lists all available GET routes

```http
GET /api
```
#### Default Response
```
Status: 200 OK
```
```json
{
  "class": ["home"],
  "properties": {
    "name": "DAW Project",
    "group": "LI61D-G08",
    "uptimeMs": 2633953,
    "time": "2021-04-24T17:03:25.2541321+01:00"
  },
  "links": [
    {
      "rel": ["projects"],
      "href": "/api/projects"
    },
    {
      "rel": ["users"],
      "href": "/api/users"
    },
    {
      "rel": ["project"],
      "hrefTemplate": "/api/projects/{projectId}"
    },
    {
      "rel": ["labels"],
      "hrefTemplate": "/api/projects/{projectId}/labels"
    },
    {
      "rel": ["label"],
      "hrefTemplate": "/api/projects/{projectId}/labels/{labelNumber}"
    },
    {
      "rel": ["states"],
      "hrefTemplate": "/api/projects/{projectId}/states"
    },
    {
      "rel": ["state"],
      "hrefTemplate": "/api/projects/{projectId}/states/{stateNumber}"
    },
    {
      "rel": ["nextStates"],
      "hrefTemplate": "/api/projects/{projectId}/states/{stateNumber}/nextStates"
    },
    {
      "rel": ["issues"],
      "hrefTemplate": "/api/projects/{projectId}/issues"
    },
    {
      "rel": ["issue"],
      "hrefTemplate": "/api/projects/{projectId}/issues/{issueNumber}"
    },
    {
      "rel": ["issueLabels"],
      "hrefTemplate": "/api/projects/{projectId}/issues/{issueNumber}/labels"
    },
    {
      "rel": ["comments"],
      "hrefTemplate": "/api/projects/{projectId}/issues/{issueNumber}/comments"
    },
    {
      "rel": ["comment"],
      "hrefTemplate": "/api/projects/{projectId}/issues/{issueNumber}/comments/{commentNumber}"
    },
    {
      "rel": ["user"],
      "hrefTemplate": "/api/users/{userId}"
    }
  ]
}
```

# References
[Siren](https://github.com/kevinswiber/siren)
