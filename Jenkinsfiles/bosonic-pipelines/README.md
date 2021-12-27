How to build and deploy LiquiMatch Mono
=======================================
* Repo
* LM jobs and Pipeline
  * Pipelines:
  * Jobs:
    * LM-Compile-And-Test
    * LM Version/Build/Release 
    * LM-Deploy
    * LM-Install
    * LM-Config
    * LM-Restart
    * LM-Build-Deploy-Restart
* To look for particular job status in Slack
* FAQ
  * LM Jobs Build Process - Compilation Process and Dependencies
  * Artifactory - Structure 
  * After Build "otcxn-jenkins" user makes a Push on Git Repo - Where? why? - is only for tag the build?

Repo:
=====
https://github.com/otcxn/bosonic-lm

LM jobs and Pipeline:
=====================
Pipelines:
----------
https://github.com/otcxn/jenkins/tree/master/Jenkinsfiles

Jobs:
-----

LM-Compile-And-Test
-------------------
* Jenkins Job: https://jenkins.otcxn.com:8443/view/LM/job/LM-Compile-And-Test/
* Purpose: Compiles the code and runs the tests
* Description: Creates new job when new branch is created, and deletes when it has been deleted. 
* Trigger: Any check in on that branch <br />
(Automatic trigger with webhook -  https://github.com/otcxn/bosonic-lm/settings/hooks/330844246)
* Commands to call: <br />
    * mvn clean install -DskipTests -U <br />
    * mvn test -f rest_gateway <br />
    * mvn test -f fixgateway <br />
* Pipeline script: [Jenkinsfile](https://github.com/otcxn/bosonic-lm/blob/master/Jenkinsfile )
* Input: None

LM Version/Build/Release 
------------------------
* Jenkins Job: https://jenkins.otcxn.com:8443/view/LM/job/LM-Version/
* Purpose: Versions the branch
* Description: it creates new release version on the specified 'branch' and packages the rpm at "/rpm/bosonic-lm-repo/noarch". <br />
(The rpm packaging script is present at: https://github.com/otcxn/devops-tools > rpm)
* Trigger:Manual one-click event initiated by a human
* Commands to call:
    * mvn clean -DskipTests -Darguments=-DskipTests release:prepare --batch-mode
    * mvn -DskipTests -Darguments=-DskipTests release:perform -B -s settings.xml
    * RPM packaging
* Pipeline script: [LM_build](https://github.com/otcxn/jenkins/blob/master/Jenkinsfiles/LM_build) 
* Input: fromBranch

LM-Deploy
---------
* Jenkins Job: https://jenkins.otcxn.com:8443/view/LM/job/LM-Deploy/
* Purpose: Executes the specified RPM version on the specified target machines
* Description: downloads the rpm package on the specified server at "/opt/bosonic/lm-package". Executes "rpm -Uv" to install package (we are NOT doing 'yum install'). All installed versions should be present at "/opt/bosonic/release/lm" on target server.
* Trigger: Manual executed by human
* Commands to call: ansible "liquimatch" role with tag "bosonic-lm"
* Pipeline script: [LM_mono_deploy](https://github.com/otcxn/jenkins/blob/master/Jenkinsfiles/LM_mono_deploy) 
* Input: 
    * RPM version
    * target machines/environment

LM-Install
----------
* Jenkins Job: https://jenkins.otcxn.com:8443/view/LM/job/LM-Install/
* Purpose: install/symlink the specified Release Version
* Description: Executes "install-lm.sh" script to create symlinks for specified modules on the specified target machines (Muti-select is available for 'modules' parameter)  <br />
(The install script is present at: https://github.com/otcxn/devops-tools > rpm)
* Trigger: Manual executed by human
* Commands to call: ansible "liquimatch" role with tag "symlink-install"
* Pipeline script: [LM_install_symlink](https://github.com/otcxn/jenkins/blob/master/Jenkinsfiles/LM_install_symlink)
* Input: 
    * target machines/environment
    * Module (multi select available)
    * Release version 

LM-Config
---------
* Jenkins Job: https://jenkins.otcxn.com:8443/view/LM/job/LM-Config/
* Purpose: Deploys the latest config from repo "ogg_darktraderconfig"
* Description:  deploys the config at "/opt/otcxn_dt/deployments/instance" on the box. <br />
Creates symlinks "config" for each instance to point on "dt-0.47-SNAPSHOT/config". <br />
Also, creates backup of config for UAT and PROD at "/opt/otcxn_dt/backup" <br />
* Trigger: Manual executed by human
* Commands to call: 
    * Pipeline uses approach "Publish over SSH"
    * copy and call script "config_softlinks.sh"
* Pipeline script: [LM_deploy_config](https://github.com/otcxn/jenkins/blob/master/Jenkinsfiles/LM_deploy_config) 
* Input: 
    * target machines/environment
    * Config Version

LM-Restart
----------
* Jenkins Job: https://jenkins.otcxn.com:8443/view/LM/job/LM-Restart/
* Purpose: Restart LM apps on specified target machine
* Description:  Copy and executes the script "restart.sh" on IT/QA, STOP-LM.sh and startLM.sh on UAT/PROD (the scripts are in "ansible/roles/liquimatch/templates"
* Trigger: 
    * Manual executed by human
    * Automated : everyday, New_York @ 11 AM for IT
* Commands to call: ansible "liquimatch" role with tag "restart"
* Pipeline script: [LM_restart](https://github.com/otcxn/jenkins/blob/master/Jenkinsfiles/LM_restart)
* Input: target machines/environment

LM-Build-Deploy-Restart
-----------------------
* Jenkins Job: https://jenkins.otcxn.com:8443/view/LM/job/LM-Build-Deploy-Restart/
* Purpose: one-click job for LM IT to call "LM-Version, LM-Deploy, LM-Install, LM-Config, LM-Restart, Continuos Integration Bosonic"
* Description: one-click job for LM IT.
  * It will: <br />
  -release from 'master' branch <br />
  -deploys latest rpm package <br />
  -installs 'all' modules to latest Release Version <br />
  -deploys latest config <br />
  -restarts LM <br />
  -runs test script 'login_test.js' <br />
* Trigger: Manual executed by human
* Commands to call: Calls each job with the default options: <br />
    fromBranch="master" <br />
    RPM version=<latest from the repo> <br />
    target machines/environment="IT" <br />
    module="all" <br />
    Release version=<pick from the latest build done> <br />
    Config Version="dt-0.47-SNAPSHOT" <br />
* Pipeline script: [LM-Build-Deploy-Restart](https://github.com/otcxn/jenkins/blob/master/Jenkinsfiles/LM-Build-Deploy-Restart)
* Input: None


To look for particular job status in Slack:
===========================================
type "in:#jenkins-notifications <job_name>" in search
eg, "in:#jenkins-notifications LM-compile-and-test"

FAQ:
====
LM Jobs Build Process - Compilation Process and Dependencies:
-------------------------------------------------------------
To compile the project, import New project from version control (git) on IntelliJ IDEA tool. <br />
To compile: mvn clean compile -DskipTests <br />
To run the basic build : mvn clean install -DskipTests -U  (this will not upload jars to artifactory) <br />

In bosonic-lm repo, the parent pom.xml has defined all dependencies/modules.


Artifactory - Structure:
------------------------
In project's main pom.xml, dev specify the structure for the output generated (which we are using in packaging RPM)

        <output.dir.prefix>../${project.version}</output.dir.prefix>
        <output.dir.suffix>${project.version}/lib</output.dir.suffix>

 'distributionManagement' defines the path to upload in artifactory depending on RELEASE/SNAPSHOT.

    <distributionManagement>
        <repository>
            <id>releases</id>
            <url>http://artifactory.otcxn.com:8081/artifactory/libs-release</url>
        </repository>
        <snapshotRepository>
            <id>snapshots</id>
            <name>artifactory-otcxn-snapshots</name>
            <url>http://artifactory.otcxn.com:8081/artifactory/libs-snapshot</url>
        </snapshotRepository>
    </distributionManagement>

Then, in each module pom.xml, the path for artifactory is defined at the beginning.
eg, in 'CrossingEngine' pom.xml, the path is specified as "\<groupId>/\<artifactId>/\<version>"

    <name>LM CrossingEngine</name>
    <groupId>com.bosonic.lm</groupId>
    <artifactId>CrossingEngine</artifactId>
    <version>2021.3.1-SNAPSHOT</version>
    <packaging>jar</packaging>
 

After Build "otcxn-jenkins" user makes a Push on Git Repo - Where? why? - is only for tag the build?
----------------------------------------------------------------------------------------------------
The github user "otcxn-jenkins" account is setup on our build server and creates a tag after the build job and creates a blank file with the same name tag at "/rpm/bosonic-lm-repo/tags/" (if we gonna need the tag somewhere in future). <br />
Although the repo "bosonic-lm" already tags the repo during build


    <scm>
        <connection>scm:git:git@github.com:otcxn/bosonic-lm.git</connection>
        <developerConnection>scm:git:git@github.com:otcxn/bosonic-lm.git</developerConnection>
        <url>https://github.com/otcxn/bosonic-lm</url>
        <tag>HEAD</tag>
    </scm>

                        <tagNameFormat>v@{project.version}</tagNameFormat>

I'll say, let's monitor the build job for some days, if we don't need the git tag by the pipeline, we can remove the part later from the pipeline

    git tag ${verNum}
    git push origin ${verNum}
 
