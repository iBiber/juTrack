# juTrack - Jira Simulation
Simple simulation tool to simulate a Jira response to a request from juTrack. It can be used to do developer tests on juTrack without a jira installation.

## How it works
The simulation tool starts a REST web server on port 8080 and will always return the example.json on any request to /rest/api/2/search ignoring any parameter.

## How to use it
1. Start the simulation tool.
2. Start juTrack.
3. Enter into 'Jira root url': http://localhost:8080
4. Enter into 'Jira user name': user_01
5. Click Ok button.