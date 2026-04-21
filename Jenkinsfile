pipeline {
    agent any

    stages {


        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
               bat 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t odms-app .'
            }
        }

        stage('Deploy') {
            steps {
                bat 'kubectl apply -f deployment.yaml'
                bat 'kubectl apply -f service.yaml'
            }
        }
    }
}