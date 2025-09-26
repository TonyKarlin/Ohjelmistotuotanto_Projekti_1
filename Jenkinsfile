pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Backend Test & Coverage') {
            steps {
                dir('Backend') {
                    bat 'mvn clean test jacoco:report'
                }
            }
        }
        stage('Frontend Test & Coverage') {
            steps {
                dir('Frontend') {
                    bat 'mvn clean test jacoco:report'
                }
            }
        }
    }
}