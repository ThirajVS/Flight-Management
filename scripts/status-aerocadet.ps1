[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$wslProject = (& wsl.exe -d Ubuntu -e wslpath -a $projectRoot).Trim()

& wsl.exe -d Ubuntu -e docker --config /tmp/aerocadet-empty-docker-config compose `
    --env-file "$wslProject/.env" --file "$wslProject/docker-compose.yml" ps

$health = Invoke-RestMethod -Uri 'http://localhost:8090/actuator/health' -TimeoutSec 10
Write-Host "Backend health: $($health.status)" -ForegroundColor Green
