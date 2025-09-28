pipeline {
    agent any

    environment {
        PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKERHUB_REPO = 'jarkkok1/project'
        DOCKER_IMAGE_TAG = 'latest'
    }

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
        stage('Start Backend') {
            steps {
                dir('Backend') {
                    bat 'start /b mvnw.cmd spring-boot:run'
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
        stage('Docker Compose Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat """
                        docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                        docker-compose -f docker-compose.yml build
                        docker-compose -f docker-compose.yml push
                    """
                }
            }
        }
    }
}
/*
stage('Build Docker Image') {
            steps {
                dir('Backend') {
                    bat 'docker build -t %DOCKERHUB_REPO%:%DOCKER_IMAGE_TAG% .'
                }
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat '''
                        docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                        docker push %DOCKERHUB_REPO%:%DOCKER_IMAGE_TAG%
                    '''
                }
            }
        }
        stage('Build Frontend Docker Image') {
            steps {
                dir('Frontend') {
                    bat 'docker build -t %DOCKERHUB_REPO%-frontend:%DOCKER_IMAGE_TAG% .'
                }
            }
        }
        stage('Push Frontend Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat '''
                        docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                        docker push %DOCKERHUB_REPO%-frontend:%DOCKER_IMAGE_TAG%
                    '''
                }
            }
        }
*/