pipeline {
    agent { label 'slave' }

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
                stage('Sonar Analysis') {
                      steps {
                        withSonarQubeEnv('sonarqube-server') {
                          sh """
                          mvn -B clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                            -Dsonar.organization=haridevops03 \
                            -Dsonar.projectKey=haridevops03_factorial \
                            -Dsonar.projectName=factorial
                          """
                        }
                      }
                    }
                
                stage('Quality Gate') {
                  steps {
                    timeout(time: 300, unit: 'SECONDS') {
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
