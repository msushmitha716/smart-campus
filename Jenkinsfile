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

        stage('Deploy to AWS EC2') {
            steps {
                sshagent(['ec2-ssh']) {
                    bat 'ssh -o StrictHostKeyChecking=no ec2-user@ec2-32-236-14-0.ap-southeast-2.compute.amazonaws.com "export JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto.x86_64; export PATH=/usr/lib/jvm/java-21-amazon-corretto.x86_64/bin:/usr/local/bin:/usr/bin:/bin; set -e; cd ~/smart-campus; git fetch origin master; git reset --hard origin/master; /usr/bin/mvn clean package -DskipTests; sudo docker build -t smart-campus:1.0 .; sudo docker rm -f smart-campus-app; sudo docker run -d --name smart-campus-app --restart unless-stopped -p 80:8090 -v /home/ec2-user/smart-campus/data:/app/data smart-campus:1.0" </dev/null'
                }
            }
        }
    }

    post {
        success {
            echo 'Smart Campus CI/CD pipeline completed successfully and deployed to AWS EC2.'
        }

        failure {
            echo 'Smart Campus CI/CD pipeline failed.'
        }
    }
}