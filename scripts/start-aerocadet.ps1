[CmdletBinding()]
param(
    [switch]$Build
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$envFile = Join-Path $projectRoot '.env'

if (-not (Test-Path -LiteralPath $envFile)) {
    throw "Missing $envFile. Copy .env.example to .env and replace the placeholder secrets first."
}

$wslProject = (& wsl.exe -d Ubuntu -e wslpath -a $projectRoot).Trim()
if ($LASTEXITCODE -ne 0 -or [string]::IsNullOrWhiteSpace($wslProject)) {
    throw 'The Ubuntu WSL distribution is unavailable.'
}

& wsl.exe -d Ubuntu -e docker --config /tmp/aerocadet-empty-docker-config info *> $null
if ($LASTEXITCODE -ne 0) {
    Write-Host 'Starting the Docker service inside Ubuntu WSL...'
    & wsl.exe -d Ubuntu -u root -e service docker start
    if ($LASTEXITCODE -ne 0) { throw 'Docker could not be started inside Ubuntu WSL.' }
}

$upArguments = @('up', '-d', '--wait')
if ($Build) { $upArguments = @('up', '-d', '--build', '--wait') }

$composeArguments = @(
    '-d', 'Ubuntu', '-e',
    'docker', '--config', '/tmp/aerocadet-empty-docker-config',
    'compose', '--env-file', "$wslProject/.env",
    '--file', "$wslProject/docker-compose.yml"
) + $upArguments

& wsl.exe @composeArguments
if ($LASTEXITCODE -ne 0) { throw 'Docker Compose did not start AeroCadet successfully.' }

$health = Invoke-RestMethod -Uri 'http://localhost:8090/actuator/health' -TimeoutSec 15
if ($health.status -ne 'UP') { throw 'AeroCadet started but its backend health is not UP.' }

Write-Host 'AeroCadet is healthy: http://localhost:8090' -ForegroundColor Green
Write-Host 'Jenkins: http://localhost:8080'
