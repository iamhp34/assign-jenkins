pipeline {
    agent slave

    tools {
        // MUST match: Manage Jenkins -> Tools
        jdk   'jdk17'
        maven 'maven3'
    }

    environment {
        // MUST match: Manage Jenkins -> System -> SonarQube servers -> Name
        SONARQUBE_SERVER   = 'sonarqube-server'

        // SonarCloud details
        SONAR_ORG          = 'HARIDEVOPS03'
        SONAR_PROJECT_KEY  = 'haridevops03_factorial'
        SONAR_PROJECT_NAME = 'factorial'
    }

    stages {

        stage('Checkout code') {
            steps {
                checkout scm
            }
        }

        stage('SonarQube static code analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh """
                      mvn -B clean verify sonar:sonar \
                        -Dsonar.organization=${SONAR_ORG} \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${SONAR_PROJECT_NAME}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build application') {
            steps {
                sh "mvn -B package -DskipTests"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar,target/*.war',
                             fingerprint: true,
                             allowEmptyArchive: true
        }
    }
}
