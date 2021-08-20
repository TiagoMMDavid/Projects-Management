# Projects Management (DAW project)

## Overview
Projects Management is a system divided into two components, a server application (developed using [Spring](https://spring.io/)) and a web application (developed using [React](https://reactjs.org/)). The system has the purpose of managing project issues and associated information.

Each project can hold the information of its name, description and the issues associated with it. The issues are characterized by having a state, a set of labels and comments which are posted by users.

## Table of Contents
- [Functionalities](#functionalities)
- [Preview](#preview)
- [Documentation](#requirements)
- [Credits](#credits)

## Functionalities
The following functionalities are exposed:
- Management through Hypermedia HTTP API
- Functional and responsive Web Application (SPA) to manage user created projects
- Authentication feature
- Manage your projects
  - Create, Edit and Delete
  - Manage project labels
  - Manage project states and state transitions
  - Manage issues
    - Create, Edit and Delete
    - Change issue's state
    - Add/Delete labels to an issue
    - Add/Edit/Delete comments to an issue

## Preview


## Documentation
- Server Application
  - [How to Build and Run the App](code/jvm/README.md)
  - [API Documentation](docs/README.md)
- Web Application
  - [How to Build and Run the App](code/js/README.md)
- [Presentation Guide](docs/presentation-guide.md)

## Credits
This application was developed for the Web Application Development class (6th semester) at [ISEL](https://www.isel.pt/).

Developed by:
* [TiagoMMDavid](https://github.com/TiagoMMDavid)
* [PTKickass](https://github.com/PTKickass)
* [dvsshadow](https://github.com/dvsshadow)