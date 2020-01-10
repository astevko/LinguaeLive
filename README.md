https://github.com/GoogleCloudPlatform/app-maven-plugin
Requirements

Maven is required to build and run the plugin.  http://maven.apache.org/

You must have Google Cloud SDK installed.   https://cloud.google.com/sdk/

Cloud SDK app-engine-java component is also required. Install it by running:

*gcloud components install app-engine-java*
*gcloud components install bq*
*gcloud components install cloud-datastore-emulator*

Components

│     Status    │                         Name                         │            ID            │   Size   │
| ------------- | ---------------------------------------------------- | ------------------------ | -------- |
│ Installed     │ BigQuery Command Line Tool                           │ bq                       │  < 1 MiB │
│ Installed     │ Cloud Datastore Emulator                             │ cloud-datastore-emulator │ 18.4 MiB │
│ Installed     │ Cloud SDK Core Libraries                             │ core                     │ 12.7 MiB │
│ Installed     │ Cloud Storage Command Line Tool                      │ gsutil                   │  3.6 MiB │
│ Installed     │ gcloud app Java Extensions                           │ app-engine-java          │ 62.0 MiB │
│ Installed     │ gcloud app Python Extensions                         │ app-engine-python        │  6.0 MiB │

To install or remove components at your current SDK version, run:
  $ gcloud components install COMPONENT_ID
  $ gcloud components remove COMPONENT_ID

To update your SDK installation to the latest version, run:
  $ gcloud components update


Modify profile to update your $PATH and enable shell command
  $ ./google-cloud-sdk/install.sh

---
Login and configure Cloud SDK:

*gcloud init*


To build and launch the project:

    in .../LinguaeLive/ ➜ mvn clean
    in .../LinguaeLive/ ➜ mvn package
    in .../LinguaeLive/LinguaeLive-server/ ➜ mvn appengine:run -Denv=dev -am
    then open your browser at http://localhost:8888/index.html
    navigate throughout the applicaiton to guarantee the full build is operational.
    http://localhost:8888/index.html?locale=es#whatis:   will open the WhatIsView with es language binding
    
    
To use it in dev mode:

    in /LinguaeLive/LinguaeLive-client/ ➜ mvn gwt:codeserver -pl *-client -am
    then open your browser at http://localhost:8888/
    
    refreshing your browser will cause the web application to be incrementally recompiled.
    
    

---
Reference Documentation

App Engine Standard Environment:

    Using Apache Maven and the App Engine Plugin (standard environment)
    App Engine Maven Plugin Goals and Parameters (standard environment)

[INFO] App Engine Maven Plugin 2.0.0
  This Maven plugin provides goals to build and deploy Google App Engine
  applications.

This plugin has 14 goals:

appengine:cloudSdkLogin
  Login and set the Cloud SDK common configuration user.

appengine:deploy
  Stage and deploy the application and all configs to Google App Engine.

appengine:deployAll
  Stage and deploy the application and all configs to Google App Engine.

appengine:deployCron
  Stage and deploy cron.yaml to Google App Engine.

appengine:deployDispatch
  Stage and deploy dispatch.yaml to Google App Engine.

appengine:deployDos
  Stage and deploy dos.yaml to Google App Engine.

appengine:deployIndex
  Stage and deploy index.yaml to Google App Engine.

appengine:deployQueue
  Stage and deploy queue.yaml to Google App Engine.

appengine:genRepoInfoFile
  Generates repository information files for the Stackdriver Debugger.

appengine:help
  Display help information on appengine-maven-plugin.
  Call mvn appengine:help -Ddetail=true -Dgoal=<goal-name> to display parameter
  details.

appengine:run
  Run App Engine Development App Server synchronously.

appengine:stage
  Generates a deploy-ready application directory for App Engine standard or
  flexible environment deployment.

appengine:start
  Starts running App Engine Development App Server asynchronously.

appengine:stop
  Stops a running App Engine Development App Server.
---
[INFO] --- gwt-maven-plugin:1.0-rc-10:help (default-cli) @ LinguaeLive-client ---
[INFO] Maven Plugin for GWT 1.0-rc-10
  Starting fresh on building GWT projects with Maven

This plugin has 12 goals:

gwt:add-super-sources
  Add super-source directory to project resources.
  The super-source directory contains emulated classes for GWT. Super-sources in
  GWT need to be in a subdirectory of the GWT module, and you can automatically
  relocate the super-source content within a super subfolder.

gwt:add-test-super-sources
  Add super-source directory to project test resources.
  The super-source directory contains emulated classes for GWT. Super-sources in
  GWT need to be in a subdirectory of the GWT module, and you can automatically
  relocate the super-source content within a super subfolder.

gwt:codeserver
  Runs GWT's CodeServer (SuperDevMode).

gwt:compile
  Invokes the GWT Compiler on the project's sources and resources.

gwt:devmode
  Runs GWT's DevMode.

gwt:enforce-encoding
  Enforces that the source encoding is UTF-8.

gwt:generate-module
  Generates a GWT module definition from Maven dependencies, or merge
  <inherits/> with a module template.
  When no module template exist, the behavior is identical to using an empty
  file.
  
  META-INF/gwt/mainModule files from the project dependencies (not transitive)
  are used to generate <inherits/> directives. Those directives are inserted at
  the very beginning of the generated module (notably, they'll appear before any
  existing <inherits/> directive in the module template).
  
  If moduleShortName is specified (and not empty), it overwrites any existing
  rename-to from the module template.
  
  Unless the module template contains a source folder (either <source/> or
  <super-source/>, those three lines will be inserted at the very end of the
  generated module (this is to keep any includes or excludes or specific path
  from the module template):
  
  <source path='client'/>
  <source path='shared'/>
  <super-source path='super'/>

gwt:generate-module-metadata
  Generates the META-INF/gwt/mainModule file used by gwt:generate-module in
  downstream projects.

gwt:help
  Display help information on gwt-maven-plugin.
  Call mvn gwt:help -Ddetail=true -Dgoal=<goal-name> to display parameter
  details.

gwt:package-app
  Package the compiled GWT application into a WAR-like archive.

gwt:package-lib
  Package the compiled GWT library into a JAR archive.

gwt:test
  Runs the project's tests with the specific setup needed for GWTTestCase tests.
  Please note that some documentation is inherited from Surefire and cannot be
  changed, so versions (mainly) and other bits of documentation might be wrong
  or irrelevant.

---
Dev Server Admin Console
http://localhost:8888/_ah/admin/

