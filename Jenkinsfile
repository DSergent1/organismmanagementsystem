pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                echo 'Building application...'
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                bat 'mvn test'
            }
        }

        stage('Staging Deployment') {
            steps {
                echo 'Deploying to STAGING...'
                bat 'mkdir staging'
                bat 'copy target\\*.jar staging\\'
            }
        }

        stage('Staging Verification') {
            steps {
                echo 'Verifying staging environment...'
                script {
                    def status = 200  // simulate success

                    if (status != 200) {
                        error("Staging failed!")
                    }
                }
            }
        }

        stage('Production Deployment') {
            steps {
                echo 'Deploying to PRODUCTION...'
                bat 'mkdir production'
                bat 'copy staging\\*.jar production\\'
            }
        }
    }

    post {
        failure {
            echo 'Pipeline failed. Rolling back...'
            bat 'mkdir backup'
            bat 'copy production\\*.jar backup\\'
        }
    }
}