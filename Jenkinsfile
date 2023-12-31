pipeline {
    agent any

    environment {
        KUBECONFIG_PATH = "/var/lib/jenkins/.kube/config"
        IAM_AUTHENTICATOR_PATH = "/root/bin/ncp-iam-authenticator"
    }

    stages {
        // Jenkins Credential에서 파일 가져오기
        stage('Fetch Credential File') {
            steps {
                script {
                    // Jenkins Credential ID 지정
                    def credentialId = 'application'
                    // Jenkins Credential에서 파일을 가져와서 프로젝트 디렉토리로 복사
                    withCredentials([file(credentialsId: credentialId, variable: 'CREDENTIAL_FILE')]) {
                        sh "cp $CREDENTIAL_FILE /var/lib/jenkins/workspace/nadeuliWaspp/src/main/resources/application.properties"
                    }
                }
            }
        }

        // Gradle 빌드 단계
        stage('Build with Gradle') {
            steps {
                script {
                    // Gradle 빌드
                    // gradlew에 실행 권한을 부여
                    sh 'chmod +x gradlew'
                    sh './gradlew clean bootJar'
                }
            }
        }

        // Docker 이미지 빌드 및 배포 단계
        stage('Build and Deploy Docker') {
            when {
                expression { currentBuild.resultIsBetterOrEqualTo('UNSTABLE') }
            }
            steps {
                script {
                    // 버전 자동 증가
                    def versionFile = '/var/lib/jenkins/was-version.txt'
                    def currentVersion = readFile(versionFile).trim()
                    def newVersion = (currentVersion as Float) + 0.01
                    newVersion = String.format('%.2f', newVersion) // 두 자리 소수점까지 표현
                    sh "echo $newVersion > $versionFile"

                    // Docker 이미지 빌드
                    sh "sudo docker build -t lsm00/nadeuliwas:$newVersion ."
                    // 이전에 실행 중이던 도커 컨테이너 중지 및 삭제
                    def existingContainerId = sh(script: 'docker ps -aq --filter name=nadeuliwas', returnStdout: true).trim()
                    if (existingContainerId) {
                        sh "docker stop $existingContainerId"
                        sh "docker rm $existingContainerId"
                    }
                    // 새로운 도커 컨테이너 실행
                    sh "docker run -d -p 82:8080 -dit --name nadeuliwas lsm00/nadeuliwas:$newVersion"

                    // Credential ID를 사용하여 Jenkins Credential에서 Secret text 가져오기
                    def ncpAccessKey = 'ncloud_access_key_id'

                    // ncp-iam-authenticator에 액세스 키 전달
                    withCredentials([string(credentialsId: ncpAccessKey, variable: 'NCLOUD_ACCESS_KEY')]) {
                    def ncpAuthCommand = "${IAM_AUTHENTICATOR_PATH} token --clusterUuid 453f1927-60c9-4579-b22c-5338336c32ce --region KR-2 --access-key-id ${NCLOUD_ACCESS_KEY}"
                    def ncpToken = sh(script: ncpAuthCommand, returnStdout: true).trim()
                    }
                    // Kubernetes Deployment 및 Service 적용
                    def kubernetesManifests = """
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nadeuliwas
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nadeuliwas
  template:
    metadata:
      labels:
        app: nadeuliwas
    spec:
      containers:
      - name: nadeuliwas
        image: lsm00/nadeuliwas:$newVersion
        ports:
        - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: nadeuliwas
spec:
  selector:
    app: nadeuliwas
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
"""

                    // Deployment 및 Service YAML 출력
                    echo kubernetesManifests

                    // Deployment 및 Service 적용
                    withCredentials([string(credentialsId: 'docker_hub_access_token', variable: 'DOCKERHUB_ACCESS_TOKEN')]) {
                        // Docker Hub에 로그인하고 이미지 푸시
                        sh "docker login -u lsm00 -p $DOCKERHUB_ACCESS_TOKEN"
                        sh "docker push lsm00/nadeuliwas:$newVersion"
                    }

                    // Kubernetes에 배포
                    withEnv(["KUBECONFIG=${KUBECONFIG_PATH}"]) {
                        writeFile file: 'deployment.yaml', text: kubernetesManifests
                        sh 'kubectl apply -f deployment.yaml'
                    }

                    // Docker 이미지가 있는지 확인
                    def danglingImages = sh(script: 'sudo docker images -q -f "dangling=true" | wc -l', returnStdout: true).trim()
                    if (danglingImages != '0') {
                        sh 'sudo docker rmi -f $(sudo docker images -q -f "dangling=true")'
                    } else {
                        echo '삭제할 로컬 이미지가 없습니다.'
                    }
                }
            }
        }

        // Slack 통지 단계
        stage("Slack Notification") {
            when {
                expression { currentBuild.resultIsBetterOrEqualTo('UNSTABLE') }
            }
            steps {
                echo 'Slack 통지 테스트'
            }
            post {
                success {
                    slackSend channel: '#jenkins', color: 'good', message: "Web 배포 성공"
                }
                failure {
                    slackSend channel: '#jenkins', color: 'danger', message: "Web 배포 실패"
                }
            }
        }
    }
}
