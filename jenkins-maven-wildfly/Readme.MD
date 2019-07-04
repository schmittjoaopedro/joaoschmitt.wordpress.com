# Continuous Integration with jenkins-pipeline for Java, Jira, Slack, Maven, Wildfly and Soapui.

One of the most difficulties that I have found during the Jenkins-pipeline development for a Java application with Maven and Wildfly was: how to integrated this old-fashioned java application with many different tools used by the development team? Today there are many posts demonstrating single pieces of the puzzle, however, there is no guide that unifies all these pieces in a single overall example. Therefore, my idea here is to grab all these different pieces of information that I had collected to solve a specific problem and create an applied scenario using declarative Jenkins-pipeline.

The idea here is to create a continuous integration process. This process uses a beta server to execute integration and functional tests to verify if the application is working correctly. To this, the following steps must be executed:

* Show the current version of the project managed by Jira;
* Build the application using maven, where the application tests are executed locally (Unitary and Integration tests);
* Un-deploy the current war version from the beta server;
* Copy jar libs for a network folder. This app requires these files to execute some internals;
* Restart the best server;
* Deploy new war version on beta;
* Execute functional tests (Integration, Services, and Load). These are Soapui tests that have their own pipeline on Jenkins;
* Create a tag on source code to manage the latest stable version;
* Publish the latest stable war on nexus;
* Send a notification on Slack about the build status;

The general idea is that the pipeline should execute the build and verify if everything works well before to publish the app on production. In this process, firstly we make the build locally (using maven) and then we publish the war on the beta server. After that, sub-processes on Jenkins are started (using the build directive) and all these are waited to execute completely, even if one of these brokes (directive catchError). It allows to execute all tests and verify the ones that broke, in the other case, the build would be stoped in the first failure and after fixing the broken test we must execute the build again to check if the next tests are ok. Finally, after all, tests had passed, we update the latest tag on git to point for the hash of the latest stable version, and we update the artifact on nexus. At this time, a continuous deployment could verify this build status and make a deploy on production. To communicate the build status, a slack message is sent for the app team.

The following Jenkins-pipeline file describes in details the process: