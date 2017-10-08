Build result: [![Build Status](https://travis-ci.org/iBiber/juTrack.svg?branch=master)](https://travis-ci.org/iBiber/juTrack)

Code quality check: [![Codacy Badge](https://api.codacy.com/project/badge/Grade/801b93ec6acd42ada9fbf0ee1bbee082)](https://www.codacy.com/app/iBiber/juTrack?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=iBiber/juTrack&amp;utm_campaign=Badge_Grade)

Vulnerabilities check:
jutrack-core [![Known Vulnerabilities jutrack-cli](https://snyk.io/test/github/ibiber/jutrack/badge.svg?targetFile=jutrack-core%2Fpom.xml)](https://snyk.io/test/github/ibiber/jutrack?targetFile=jutrack-core%2Fpom.xml)
jutrack-cli [![Known Vulnerabilities jutrack-cli](https://snyk.io/test/github/ibiber/jutrack/badge.svg?targetFile=jutrack-cli%2Fpom.xml)](https://snyk.io/test/github/ibiber/jutrack?targetFile=jutrack-cli%2Fpom.xml)
jutrack-gui [![Known Vulnerabilities jutrack-gui](https://snyk.io/test/github/ibiber/jutrack/badge.svg?targetFile=jutrack-gui%2Fpom.xml)](https://snyk.io/test/github/ibiber/jutrack?targetFile=jutrack-gui%2Fpom.xml)


# juTrack
juTrack searches for tracks from the logged in user in jira to simplify time bookings.
It uses the Jira REST interface to gather your actions done in a Jira system.

## Modules
This project contain the following modules:
* jutrack-core - Contains the code for the business logic 
* jutrack-cli - Boots the application in the Command Line Interface mode (CLI). The parameters are prompted and the results are printed to the command line.
* jutrack-gui - Boots the application in the Graphical User Interface mode (GUI). The parameters can be entered in fields and the results are presented in the GUI.
* jutrack-jirasimulator - contains a developer tool to simulate a jira server to test the user interfaces (GUI and CLI).
