Build result: [![Build Status](https://travis-ci.org/iBiber/juTrack.svg?branch=master)](https://travis-ci.org/iBiber/juTrack)

Code quality check: [![Codacy Badge](https://api.codacy.com/project/badge/Grade/801b93ec6acd42ada9fbf0ee1bbee082)](https://www.codacy.com/app/iBiber/juTrack?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=iBiber/juTrack&amp;utm_campaign=Badge_Grade)

# juTrack
juTrack searches for tracks from the logged in user in jira to simplify time bookings.
It uses the Jira REST interface to gather your actions done in a Jira system.

## Spring Boot Web UI
The `jutrack-gui` module is now a Spring Boot web application that renders the Jira query form
and results view via Thymeleaf templates.

### Run locally
1. Build the project modules:
	```bash
	mvn clean package -pl jutrack-core -am
	```
2. Start the GUI module on port 8080:
	```bash
	mvn spring-boot:run -pl jutrack-gui
	```
3. Open http://localhost:8080 and submit a Jira query.

### Configuration
Customize defaults in [src/main/resources/application.properties](src/main/resources/application.properties):

| Property | Purpose | Default |
| --- | --- | --- |
| `ui.jira.root.url.default` | Prefills the Jira root URL field | empty |
| `ui.username.default` | Prefills the Jira user field | empty |
| `ui.startDate.default.day.range` | Number of days to subtract from today for the start date | `1` |

Templates live under [src/main/resources/templates/index.html](src/main/resources/templates/index.html).
