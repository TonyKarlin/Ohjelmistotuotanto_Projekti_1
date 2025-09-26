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
                dir('Backend') {
                    bat 'mvnw.cmd clean install'
                }
            }
        }
        stage('Test') {
            steps {
                dir('Backend') {
                    bat 'mvnw.cmd test'
                }
            }
        }
        stage('Code Coverage') {
            steps {
                dir('Backend') {
                    bat 'mvnw.cmd jacoco:report'
                }
            }
        }
    }
}