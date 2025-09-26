pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Backend Build') {
            steps {
                dir('Backend') {
                    bat 'mvnw.cmd clean install'
                }
            }
        }
        stage('Frontend Build') {
            steps {
                dir('Frontend') {
                    bat 'mvnw.cmd clean install'
                }
            }
        }
        stage('Backend Test') {
            steps {
                dir('Backend') {
                    bat 'mvnw.cmd test'
                }
            }
        }
        stage('Frontend Test') {
            steps {
                dir('Frontend') {
                    bat 'mvnw.cmd test'
                }
            }
        }
        stage('Backend Code Coverage') {
            steps {
                dir('Backend') {
                    bat 'mvnw.cmd jacoco:report'
                }
            }
        }
        stage('Frontend Code Coverage') {
            steps {
                dir('Frontend') {
                    bat 'mvnw.cmd jacoco:report'
                }
            }
        }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Archive Coverage Report') {
            steps {
                archiveArtifacts artifacts: 'Backend/target/site/jacoco/**'
                archiveArtifacts artifacts: 'Frontend/target/site/jacoco/**'
            }
        }
    }
}