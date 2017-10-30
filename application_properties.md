Mostly when you start the application, you will connect everytime to the same server and same user. You can define values to prefill the fields by creating a directory and file **"./config/application.properties"** near to the downloaded .jar file.

In this file you can define the following default values for the fields:
```
# === Default settings for the UI ===
ui.username.default=
ui.startDate.default.day.range=1

# Jira root URL e.g. http://jira.your-domain.com
ui.jira.root.url.default=
```

# Example configuration
For instance your folder can look now like this:
```
./config/application.properties
./juTrack-gui.jar
```
and the application.properties file can look like this:
```
# === Default settings for the UI ===
ui.username.default=iBiber
ui.startDate.default.day.range=10

# Jira root URL e.g. http://jira.your-domain.com
ui.jira.root.url.default=http://ibiber.github.com/jutrack
```
Now everytime the application get started, the fields get prefilled with these values.

[back](./)
