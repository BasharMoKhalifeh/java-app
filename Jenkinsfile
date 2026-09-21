```groovy
pipeline {
    agent {
        label 'docker'
    }

    environment {
        IMAGE_NAME = 'java-docker-app'
        IMAGE_TAG = "v${BUILD_NUMBER}"
        CONTAINER_NAME = 'java-docker-app'
    }

    stages {

        stage('Build') {
            steps {
                sh '''
                    docker build \
                        -t ${IMAGE_NAME}:${IMAGE_TAG} \
                        .
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                   

                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true

                    docker run -d \
                        --name ${CONTAINER_NAME} \
                        -p 8081:8080 \
                        ${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }
    }
}
```
