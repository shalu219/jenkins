pipeline {
    agent any

    stages {
        stage('Build') {
            parallel {
                stage('Build blockcore') {
                    echo 'Building blockcore'
                    repo init -u git@github.com:otcxn/otcxn-rfq-build.git -b blockcore -m default.xml
                    repo sync
                    timeStamp=$(TZ=":America/Los_Angeles" date +"%Y%m%d.%H%M%S")
                    revNum=$(repo forall otcxn/blockcore -c 'git rev-parse --short HEAD')
                    version=${timeStamp}.${revNum}

                    gradle --stacktrace clean build -Dversion="${version}"
                }
                stage('Build custodian') {
                    echo 'Building custodian'
                    repo init -u git@github.com:otcxn/otcxn-rfq-build.git -b custodian-module-process -m default.xml
                    repo sync
                    timeStamp=$(TZ=":America/Los_Angeles" date +"%Y%m%d.%H%M%S")
                    revNum=$(repo forall otcxn/custodian-module-process -c 'git rev-parse --short HEAD')
                    version=${timeStamp}.${revNum}

                    gradle --stacktrace clean build -Dversion="${version}"
                }
                stage('Build central') {
                    echo 'Building central'
                    repo init -u git@github.com:otcxn/otcxn-rfq-build.git -b central-command-service -m default.xml
                    repo sync
                    timeStamp=$(TZ=":America/Los_Angeles" date +"%Y%m%d.%H%M%S")
                    revNum=$(repo forall otcxn/central-command-service -c 'git rev-parse --short HEAD')
                    version=${timeStamp}.${revNum}

                    gradle --stacktrace clean build -Dversion="${version}"
                }
                stage('Build om') {
                    echo 'Building om'
                    repo init -u git@github.com:otcxn/otcxn-rfq-build.git -b om-module-process -m default.xml
                    repo sync
                    timeStamp=$(TZ=":America/Los_Angeles" date +"%Y%m%d.%H%M%S")
                    revNum=$(repo forall otcxn/om-module-process -c 'git rev-parse --short HEAD')
                    version=${timeStamp}.${revNum}

                    gradle --stacktrace clean build -Dversion="${version}"
                }
                stage('Build trader') {
                    repo init -u git@github.com:otcxn/otcxn-rfq-build.git -b trader-module-process -m default.xml
                    repo sync
                    timeStamp=$(TZ=":America/Los_Angeles" date +"%Y%m%d.%H%M%S")
                    revNum=$(repo forall otcxn/trader-module-process -c 'git rev-parse --short HEAD')
                    version=${timeStamp}.${revNum}

                    gradle --stacktrace clean build -Dversion="${version}"
                    echo 'Building trader'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
    }
}
