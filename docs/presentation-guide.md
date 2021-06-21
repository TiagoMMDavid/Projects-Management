# Phase 2 Presentation Guide

This document presents a summary of the presentation.
The steps below are organized in chronological order.

## Browser Demonstration
1. Demonstrate login page, failing at least once
2. After login, show browser local storage
3. Describe general application layout (navbar and content)
4. Demonstrate project functionalities (list, create, details, edit)
5. Try creating an issue without a start state
6. List states, emphasis on `closed` and `archived` state (can't edit or remove and `closed` has a transition to `archived`)
7. Create a start state, demonstrate state functionalities (edit, delete, change state), add transition to `closed` state
8. Show network requests on browser when searching states
9. Create issue with start state, demonstrate issue functionalities
10. Demonstrate issue comments functionalities (create, details, edit)
11. Fill issue comments to show pagination support
12. Bookmark to show deep-linking support
13. Close issue (show close date), and archive (show comments)
14. Demonstrate label functionalities (create, add to issue)
15. Delete project, try accessing bookmark (404)
16. Demonstrate users page
17. Log-out, try to access a page that requires authentication

## Code Structure Demonstration
1. Show directories organization
2. Show App start-up code
3. Demonstrate [fetchRoutes](../code/js/src/main/api/apiRoutes.ts), emphasize hypermedia support and error handling
4. Describe how credentials are stored and managed through [userSession](../code/js/src/main/utils/userSession.ts) and [LoginPage](../code/js/src/main/components/LoginPage.tsx)
5. Show overview of routes, explaining how the app supports deep-linking (through react-router)
6. Demonstrate [ProjectsPage](../code/js/src/main/components/projects/ProjectsPage.tsx), explaining how the component state is managed and how the API response changes the component structure based on the presence of an `action`
7. Show [CreateProject](../code/js/src/main/components/projects/CreateProject.tsx), demonstrate how the form works and how the API requests are made
8. Demonstrate [Paginated](../code/js/src/main/components/Paginated.tsx)