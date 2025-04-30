pipeline {
    agent any

    environment {
        BACKEND_IMAGE = 'e-commerce-fork-backend'
        FRONTEND_IMAGE = 'e-commerce-fork-frontend'
        DOCKER_CREDENTIALS_ID = 'docker-credentials'
        TEST_MARKER = 'EcommerceApplicationTests.java'
        DOCKER_USER = 'tomasmanriquez480'
        //DOCKER_PASSWORD = 'Tusach_251'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                    echo 'Building backend with Maven...'
                    script {
                        if (isUnix()) {
                            sh 'pwd; ls -al'
                            sh '/Users/tomasmanriquez/apache-maven-3.9.9/bin/mvn clean package -DskipTests'
                        } else {
                            bat '/Users/tomasmanriquez/apache-maven-3.9.9/bin/mvn clean package -DskipTests'
                        }
                    }
            }
        }

        stage('Run Unit Tests') {
            steps {
                    script {
                        def testDir = 'src/test/java/com/example/ecommerce'
                        def testMarker = TEST_MARKER.replace('/', File.separator)
                        def testFiles = []

                        if (isUnix()) {
                            testFiles = sh(
                                script: "find ${testDir} -name '*.java'",
                                returnStdout: true
                            ).trim().split('\n')
                        } else {
                            testFiles = bat(
                                script: "dir /S /B ${testDir}\\*.java",
                                returnStdout: true
                            ).trim().split('\r\n')
                        }

                        testFiles = testFiles.findAll { it?.trim() }

                        if (testFiles.size() == 1 && testFiles[0].replace('\\', '/').endsWith(TEST_MARKER)) {
                            echo "Only ${TEST_MARKER} found. Skipping tests."
                        } else {
                            echo 'Running JUnit tests...'
                            if (isUnix()) {
                                sh '/Users/tomasmanriquez/apache-maven-3.9.9/bin/mvn test'
                            } else {
                                bat '/Users/tomasmanriquez/apache-maven-3.9.9/bin/mvn test'
                            }
                        }
                    }
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images...'
                script {
                        if (isUnix()) {
                            sh 'echo $PATH'
                            sh "DOCKER_BUILDKIT=0 /usr/local/bin/docker build -t ${DOCKER_USER}/${BACKEND_IMAGE}:latest ."
                        } else {
                            bat "DOCKER_BUILDKIT=0 && /usr/local/bin/docker build -t ${DOCKER_USER}/${BACKEND_IMAGE}:latest ."
                        }

                        dir('ecommerce-frontend'){
                        if (isUnix()) {
                            sh "/usr/local/bin/docker build -t ${DOCKER_USER}/${FRONTEND_IMAGE}:latest ."
                        } else {
                            bat "/usr/local/bin/docker build -t ${DOCKER_USER}/${FRONTEND_IMAGE}:latest ."
                            }
                        }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                echo 'Pushing images to Docker Hub...'
                script {
                    sh 'docker context use default || true'
                }
                script {

                        if (isUnix()) {
                            //sh "docker login -u tomasmanriquez480 -p ${DOCKER_PASSWORD}"
                            sh "/usr/local/bin/docker push ${DOCKER_USER}/${BACKEND_IMAGE}:latest"
                            sh "/usr/local/bin/docker push ${DOCKER_USER}/${FRONTEND_IMAGE}:latest"
                        } else {
                            bat "/usr/local/bin/docker push ${DOCKER_USER}/${BACKEND_IMAGE}:latest"
                            bat "/usr/local/bin/docker push ${DOCKER_USER}/${FRONTEND_IMAGE}:latest"
                        }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up dangling Docker images...'
            script {
                if (isUnix()) {
                    sh 'which docker && docker --version && echo $PATH'
                    sh '/usr/local/bin/docker image prune -f'
                } else {
                    bat '/usr/local/bin/docker image prune -f'
                }
            }
        }
        failure {
            echo 'Build failed.'
        }
        success {
            echo 'Pipeline finished successfully.'
        }
    }
}
