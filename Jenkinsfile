pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                 git branch: 'main',
                    credentialsId: 'github_personal_access_token',
                    url: 'https://github.com/taehyun08/nadeuli.git'
            }
        }

        stage('Check Code Changes') {
            steps {
                script {
                    // main 브랜치에서 변경된 파일 목록 가져오기
                    def changedFiles = sh(script: 'git diff --name-only origin/main', returnStdout: true).trim().split('\n')

                    // backend 디렉토리에 속한 파일만 추려내기
                    def backendChanges = changedFiles.grep('backend/')

                    echo "Changed Backend Files: ${backendChanges}"

            // backend 디렉토리에 변경이 있으면 nadeuliWas 아이템 실행
            if (backendChanges.isEmpty()) {
                echo 'No backend changes. Skipping nadeuliWas build.'
                                        currentBuild.result = 'ABORTED'
                        return
            } else {
                build job: 'nadeuliWas', wait: false
            }
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
                    // backend 디렉토리로 이동하고 Docker 이미지 빌드
                    dir('/var/lib/jenkins/workspace/nadeuli/backend/') {
                        sh 'sudo docker build -t lsm00/nadeuli:latest .'
                    }

                    // 이전에 실행 중이던 도커 컨테이너 중지 및 삭제
                    def existingContainerId = sh(script: 'docker ps -aq --filter name=nadeuliWas', returnStdout: true).trim()
                    if (existingContainerId) {
                        sh "docker stop $existingContainerId"
                        sh "docker rm $existingContainerId"
                    }

                    // 새로운 도커 컨테이너 실행
                    sh 'docker run -d -p 80:80 -dit --name nadeuliWas lsm00/nadeuli:latest'

                    withCredentials([string(credentialsId: 'docker_hub_access_token', variable: 'DOCKERHUB_ACCESS_TOKEN')]) {
                        // Docker Hub에 로그인하고 이미지 푸시
                        sh "docker login -u lsm00 -p $DOCKERHUB_ACCESS_TOKEN"
                        sh "docker push lsm00/nadeuli:latest"
                    }

                    // 빌드 후에 로컬 이미지 삭제
                    sh 'sudo docker rmi -f $(sudo docker images -q -f "dangling=true")'
                }
            }
        }

        stage("Slack Notification") {
            when {
                expression { currentBuild.resultIsBetterOrEqualTo('UNSTABLE') }
            }
            steps {
                echo 'Slack 통지 테스트'
            }
            post {
                success {
                    slackSend channel: '#jenkins', color: 'good', message: "배포 성공"
                }
                failure {
                    slackSend channel: '#jenkins', color: 'danger', message: "배포 실패"
                }
            }
        }
    }
}
