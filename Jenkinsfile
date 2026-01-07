pipeline {
    agent any

    tools {
        // These names MUST match: Manage Jenkins -> Tools
        jdk   'jdk17'
        maven 'maven3'
    }

    environment {
        // MUST match: Manage Jenkins -> System -> SonarQube servers -> Name
        SONARQUBE_SERVER = 'sonarqube'

        // Change these to your project values (keep simple)
        SONAR_PROJECT_KEY  = 'java-mini-project'
        SONAR_PROJECT_NAME = 'java-mini-project'
    }

    stages {

        stage('Checkout code') {
            steps {
                // Webhook triggers the job; this checks out the same repo that contains Jenkinsfile
                checkout scm
            }
        }

        stage('SonarQube static code analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh """
                      mvn -B clean verify sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${SONAR_PROJECT_NAME}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                // IMPORTANT: Configure SonarQube webhook to Jenkins endpoint:
                // http://<JENKINS_HOST>:<PORT>/sonarqube-webhook/
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build application') {
            steps {
                // Build only after Quality Gate passes
                sh "mvn -B package -DskipTests"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar,target/*.war', fingerprint: true, allowEmptyArchive: true
        }
    }
}
