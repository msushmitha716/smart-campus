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
                bat 'mvn package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Docker Check') {
            steps {
                bat 'docker version'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t smart-campus:1.0 .'
            }
        }
    }

    post {
        success {
            echo 'Smart Campus CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'Smart Campus CI/CD pipeline failed.'
        }
    }
}