pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    parameters {
        booleanParam(name: 'DEPLOY', defaultValue: true, description: 'Deploy the isolated CI stack after all tests pass')
        booleanParam(name: 'RUN_ANSIBLE', defaultValue: false, description: 'Run Ansible on an agent with the Ubuntu WSL distribution available')
    }

    environment {
        DOCKER_CLI = 'C:/Program Files/Docker/Docker/resources/bin/docker.exe'
        COMPOSE_PROJECT_NAME = 'aerocadet-ci'
        CI_APP_URL = 'http://localhost:18090'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Backend Build') {
            steps {
                dir('backend') {
                    script {
                        if (isUnix()) { sh './mvnw -B -DskipTests package' }
                        else { bat 'mvnw.cmd -B -DskipTests package' }
                    }
                }
            }
        }

        stage('Frontend Build') {
            steps {
                dir('frontend') {
                    script {
                        if (isUnix()) { sh 'npm ci --no-audit --no-fund && npm run build' }
                        else { bat 'npm.cmd ci --no-audit --no-fund && npm.cmd run build' }
                    }
                }
            }
        }

        stage('Automated Testing') {
            parallel {
                stage('Backend Tests') {
                    steps {
                        dir('backend') {
                            script {
                                if (isUnix()) { sh './mvnw -B test' }
                                else { bat 'mvnw.cmd -B test' }
                            }
                        }
                    }
                }
                stage('Frontend Tests') {
                    steps {
                        dir('frontend') {
                            script {
                                if (isUnix()) { sh 'npm run test:ci' }
                                else { bat 'npm.cmd run test:ci' }
                            }
                        }
                    }
                }
            }
        }

        stage('Test Report') {
            steps {
                junit testResults: 'backend/target/surefire-reports/*.xml,frontend/reports/junit.xml', allowEmptyResults: false
            }
        }

        stage('Docker Build') {
            when { expression { return params.DEPLOY } }
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                          set -eu
                          mkdir -p .docker-ci
                          SECRET="$(date +%s)-$BUILD_NUMBER-aerocadet-ci-signing-key"
                          printf '%s\n' \
                            'DATABASE_NAME=aerocadet' \
                            'DATABASE_USERNAME=aerocadet' \
                            "DATABASE_PASSWORD=$SECRET" \
                            "JWT_SECRET=$SECRET-$SECRET" \
                            'DEMO_DATA_ENABLED=true' \
                            'APP_HOST_PORT=18090' \
                            'BACKEND_HOST_PORT=18081' \
                            'POSTGRES_HOST_PORT=15433' > .env.jenkins
                          docker --config "$WORKSPACE/.docker-ci" compose --env-file .env.jenkins -p "$COMPOSE_PROJECT_NAME" build
                        '''
                    } else {
                        bat '''
                          @if not exist ".docker-ci" mkdir ".docker-ci"
                          @powershell -NoProfile -Command "$s=[guid]::NewGuid().ToString('N')+[guid]::NewGuid().ToString('N'); @('DATABASE_NAME=aerocadet','DATABASE_USERNAME=aerocadet',('DATABASE_PASSWORD='+$s),('JWT_SECRET='+$s+$s),'DEMO_DATA_ENABLED=true','APP_HOST_PORT=18090','BACKEND_HOST_PORT=18081','POSTGRES_HOST_PORT=15433') | Set-Content -Encoding ascii .env.jenkins"
                          @"%DOCKER_CLI%" --config "%WORKSPACE%/.docker-ci" compose --env-file "%WORKSPACE%/.env.jenkins" -p "%COMPOSE_PROJECT_NAME%" build
                        '''
                    }
                }
            }
        }

        stage('Compose Validation') {
            when { expression { return params.DEPLOY } }
            steps {
                script {
                    if (isUnix()) { sh 'docker --config "$WORKSPACE/.docker-ci" compose --env-file .env.jenkins -p "$COMPOSE_PROJECT_NAME" config --quiet' }
                    else { bat '"%DOCKER_CLI%" --config "%WORKSPACE%/.docker-ci" compose --env-file "%WORKSPACE%/.env.jenkins" -p "%COMPOSE_PROJECT_NAME%" config --quiet' }
                }
            }
        }

        stage('Deployment') {
            when { expression { return params.DEPLOY } }
            steps {
                script {
                    if (isUnix()) { sh 'docker --config "$WORKSPACE/.docker-ci" compose --env-file .env.jenkins -p "$COMPOSE_PROJECT_NAME" up -d --wait' }
                    else { bat '"%DOCKER_CLI%" --config "%WORKSPACE%/.docker-ci" compose --env-file "%WORKSPACE%/.env.jenkins" -p "%COMPOSE_PROJECT_NAME%" up -d --wait' }
                }
            }
        }

        stage('Ansible Convergence') {
            when { expression { return params.DEPLOY && params.RUN_ANSIBLE } }
            steps {
                script {
                    if (!isUnix()) {
                        bat '''
                          @for /f "delims=" %%i in ('wsl.exe -d Ubuntu -e wslpath -a "%WORKSPACE%"') do @set "WSL_WORKSPACE=%%i"
                          @wsl.exe -d Ubuntu -e env ANSIBLE_CONFIG=%WSL_WORKSPACE%/ansible/ansible.cfg ansible-playbook -i %WSL_WORKSPACE%/ansible/inventory %WSL_WORKSPACE%/ansible/playbook.yml -e project_dir=%WSL_WORKSPACE% -e compose_env_file=%WSL_WORKSPACE%/.env.jenkins -e compose_project_name=%COMPOSE_PROJECT_NAME% -e application_url=%CI_APP_URL% -e compose_build_policy=policy
                        '''
                    } else {
                        sh 'ANSIBLE_CONFIG=ansible/ansible.cfg ansible-playbook -i ansible/inventory ansible/playbook.yml -e project_dir="$WORKSPACE" -e compose_env_file="$WORKSPACE/.env.jenkins" -e compose_project_name="$COMPOSE_PROJECT_NAME" -e application_url="$CI_APP_URL"'
                    }
                }
            }
        }

        stage('Health Check') {
            when { expression { return params.DEPLOY } }
            steps {
                script {
                    if (isUnix()) { sh 'curl --fail --retry 12 --retry-delay 5 "$CI_APP_URL/actuator/health"' }
                    else { powershell 'for ($i=0; $i -lt 12; $i++) { try { $h=Invoke-RestMethod "$env:CI_APP_URL/actuator/health" -TimeoutSec 5; if ($h.status -eq "UP") { exit 0 } } catch {}; Start-Sleep 5 }; throw "AeroCadet health check failed"' }
                }
            }
        }
    }

    post {
        always {
            junit testResults: 'backend/target/surefire-reports/*.xml,frontend/reports/junit.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'backend/target/*.jar,frontend/dist/**,frontend/reports/*.xml', allowEmptyArchive: true, fingerprint: true
            deleteDir()
        }
        success { echo 'AeroCadet CI/CD completed with verified health.' }
        failure { echo 'AeroCadet deployment was blocked because a required stage failed.' }
    }
}
