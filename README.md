# jenkins
Jenkins Pipeline

#VARIABLE ASSIGNMENT
cmd1 = "git -C ./" + sm + " branch -a | grep " + fb + " | tr -d \" \""
println(cmd1)
echo "cmd1=" + cmd1

myVar = sh(script:"<shell command>", returnStdout:true).trim()
check = sh(script:'which s3cmd', returnStdout:true).trim()		--> /usr/local/bin/s3cmd


#FILE EXISTS:
Using variable:
    def exists = fileExists 'file'
    if (exists) {
        echo 'Yes'
    } else {
        echo 'No'
    }
Using brackets:
    if (fileExists('file')) {
        echo 'Yes'
    } else {
        echo 'No'
    }

#SAMPLE STAGE BLOCK WITH OPTIONAL script, sh
        stage('SAMPLE') {
            steps {
                sh """#!/bin/bash
                set -e
                
                """
                script {
                    
                }
                input 'Do you want to continue the Build?'
                timeout(3) {    // timeout after 3 mins
                    script {
                        input(message: 'You have selected QA. Do you want to continue the Build?', ok: 'Proceed')
                        //input 'You have selected PROD. Do you want to continue the Build?'
                    }
                }
            }
        }



#REFERENCES:
https://www.jenkins.io/doc/book/pipeline/syntax/#script
https://stackoverflow.com/questions/43587964/jenkins-pipeline-if-else-not-working
https://stackoverflow.com/questions/61379959/jenkins-input-on-declarative-pipeline
https://devopscube.com/declarative-pipeline-parameters/
https://devops.stackexchange.com/questions/5203/passing-variables-between-scripts-in-a-jenkins-pipeline
https://github.com/devopscube/declarative-pipeline-examples/blob/master/parameters/Jenkinsfile.ActiveChoiceParameters
