pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                bat 'mvnw.cmd clean install'
            }
        }
        stage('Test') {
            steps {
                bat 'mvnw.cmd test'
            }
        }
        stage('Code Coverage') {
            steps {
                bat 'mvnw.cmd jacoco:report'
            }
        }
    }
}