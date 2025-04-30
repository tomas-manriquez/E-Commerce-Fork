pipeline {
    agent any

    environment {
        BACKEND_IMAGE = 'e-commerce-fork-backend'
        FRONTEND_IMAGE = 'e-commerce-fork-frontend'
        DOCKER_CREDENTIALS_ID = 'docker-credentials'
        TEST_MARKER = 'src/test/java/com.example.ecommerce/EcommerceApplicationTests.java'
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
                //dir('E-commerce-Fork')
                {
                    echo 'Building backend with Maven...'
                    script {
                        if (isUnix()) {
                            sh 'pwd'
                            sh 'ls -la'
                            sh '/Users/tomasmanriquez/apache-maven-3.9.9/bin/mvn clean package -DskipTests'
                        } else {
                            bat '/Users/tomasmanriquez/apache-maven-3.9.9/bin/mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Run Unit Tests') {
            steps {
                dir('E-commerce-Fork') {
                    script {
                        def testDir = 'src/test/java'
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
                                sh 'mvn test'
                            } else {
                                bat 'mvn test'
                            }
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images...'
                script {
                    dir('E-commerce-Fork') {
                        if (isUnix()) {
                            sh "docker build -t ${BACKEND_IMAGE}:latest ."
                        } else {
                            bat "docker build -t ${BACKEND_IMAGE}:latest ."
                        }
                    }
                    dir('E-commerce-Fork/ecommerce-frontend') {
                        if (isUnix()) {
                            sh "docker build -t ${FRONTEND_IMAGE}:latest ."
                        } else {
                            bat "docker build -t ${FRONTEND_IMAGE}:latest ."
                        }
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                echo 'Pushing images to Docker Hub...'
                script {
                    withDockerRegistry(credentialsId: DOCKER_CREDENTIALS_ID) {
                        if (isUnix()) {
                            sh "docker push ${BACKEND_IMAGE}:latest"
                            sh "docker push ${FRONTEND_IMAGE}:latest"
                        } else {
                            bat "docker push ${BACKEND_IMAGE}:latest"
                            bat "docker push ${FRONTEND_IMAGE}:latest"
                        }
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
                    sh 'docker image prune -f'
                } else {
                    bat 'docker image prune -f'
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
