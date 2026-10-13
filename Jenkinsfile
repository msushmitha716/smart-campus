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

        stage('Docker Build Local') {
            steps {
                bat 'docker build -t smart-campus:1.0 .'
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                sshagent(['ec2-ssh']) {
                    // 1. Create the remote directory if it doesn't exist
                    bat 'ssh -o StrictHostKeyChecking=no ec2-user@ec2-32-236-14-0.ap-southeast-2.compute.amazonaws.com "mkdir -p ~/smart-campus/target" <NUL'
                    
                    // 2. Securely copy the jar built on Windows straight to EC2
                    bat 'scp -o StrictHostKeyChecking=no target/smart-campus-1.0.0.jar ec2-user@ec2-32-236-14-0.ap-southeast-2.compute.amazonaws.com:~/smart-campus/target/'
                    
                    // 3. Copy the Dockerfile over as well
                    bat 'scp -o StrictHostKeyChecking=no Dockerfile ec2-user@ec2-32-236-14-0.ap-southeast-2.compute.amazonaws.com:~/smart-campus/'

                    // 4. Run docker commands on EC2 instantly without waiting for Maven compilation
                    bat 'ssh -o StrictHostKeyChecking=no ec2-user@ec2-32-236-14-0.ap-southeast-2.compute.amazonaws.com "export JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto.x86_64; export PATH=/usr/lib/jvm/java-21-amazon-corretto.x86_64/bin:/usr/local/bin:/usr/bin:/bin; cd ~/smart-campus; sudo docker build -t smart-campus:1.0 .; sudo docker rm -f smart-campus-app; sudo docker run -d --name smart-campus-app --restart unless-stopped -p 80:8090 -v /home/ec2-user/smart-campus/data:/app/data smart-campus:1.0" <NUL'
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