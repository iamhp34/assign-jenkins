pipeline {
    agent { label 'slave' }

    tools {
        jdk   'jdk17'
        maven 'maven3'
    }

    environment {
        SONARQUBE_SERVER   = 'sonarqube-server'
        SONAR_ORG          = 'haridevops03'
        SONAR_PROJECT_KEY  = 'haridevops03_factorial'
        SONAR_PROJECT_NAME = 'factorial'
    }

    stages {

        stage('Checkout code') {
            steps {
                checkout scm
            }
        }

        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-server') {
                    sh """
                    mvn -B clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                      -Dsonar.organization=${SONAR_ORG} \
                      -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                      -Dsonar.projectName=${SONAR_PROJECT_NAME}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo "Webhook not available on current SonarCloud plan → skipping blocking Quality Gate."
            }
        }

        stage('Build application') {
            steps {
                sh "mvn -B package -DskipTests"
            }
        }

        stage('Upload to Artifactory') {
            steps {
                withCredentials([string(credentialsId: 'artifact-cred', variable: 'ART_TOKEN')]) {
                    sh '''
                        set -e

                        ART_URL="http://13.200.21.76:8082"
                        JAR_DIR="/home/ec2-user/jenkins/workspace/assign/target"
                        REPO_PATH="libs-snapshot-local/assign/${BUILD_NUMBER}/"

                        echo "Files in target:"
                        ls -lah "$JAR_DIR"

                        for f in "$JAR_DIR"/*.jar; do
                          echo "Uploading $f"

                          curl -f -H "X-JFrog-Art-Api: $ART_TOKEN" \
                               -T "$f" \
                               "$ART_URL/artifactory/$REPO_PATH$(basename "$f")"
                        done

                        echo "Upload completed successfully"
                    '''
                }
            }
        }

    }
}
