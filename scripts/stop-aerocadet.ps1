[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$wslProject = (& wsl.exe -d Ubuntu -e wslpath -a $projectRoot).Trim()

& wsl.exe -d Ubuntu -e docker --config /tmp/aerocadet-empty-docker-config compose `
    --env-file "$wslProject/.env" --file "$wslProject/docker-compose.yml" down

if ($LASTEXITCODE -ne 0) { throw 'Docker Compose did not stop cleanly.' }
Write-Host 'AeroCadet stopped. PostgreSQL and upload volumes were preserved.' -ForegroundColor Yellow
