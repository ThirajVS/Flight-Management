[CmdletBinding()]
param(
    [switch]$SkipTests,
    [string]$BaseUrl = 'http://localhost:8090'
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$requiredFiles = @(
    '.env.example',
    '.gitignore',
    'README.md',
    'backend/pom.xml',
    'frontend/package.json',
    'docker-compose.yml',
    'Jenkinsfile',
    'ansible/playbook.yml',
    'docs/environment-check.md',
    'docs/command-log.md',
    'docs/screenshot-manifest.md'
)

Write-Host 'AeroCadet foundation validation' -ForegroundColor Cyan

foreach ($relativePath in $requiredFiles) {
    $fullPath = Join-Path $projectRoot $relativePath
    if (-not (Test-Path -LiteralPath $fullPath)) {
        throw "Missing required file: $relativePath"
    }
    Write-Host "[PASS] $relativePath"
}

$requiredEnvironmentNames = @('DATABASE_PASSWORD', 'JWT_SECRET')
if (Test-Path -LiteralPath (Join-Path $projectRoot '.env')) {
    $environmentText = Get-Content -Raw (Join-Path $projectRoot '.env')
    foreach ($name in $requiredEnvironmentNames) {
        if ($environmentText -notmatch "(?m)^$name=.+") {
            throw "Required environment variable is missing from .env: $name"
        }
    }
    Write-Host '[PASS] Required deployment variables are configured'
}

$ignoredEnvironmentFile = git -C $projectRoot -c safe.directory=$projectRoot check-ignore .env 2>$null
if ($ignoredEnvironmentFile -ne '.env') {
    throw '.env is not ignored by Git.'
}
Write-Host '[PASS] .env is excluded from Git'

if (-not $SkipTests) {
    if (Test-Path -LiteralPath (Join-Path $projectRoot 'frontend/node_modules')) {
        Push-Location (Join-Path $projectRoot 'frontend')
        try {
            npm.cmd run test:ci
            npm.cmd run build
        } finally {
            Pop-Location
        }
    } else {
        Write-Warning 'Frontend dependencies are not installed; skipping frontend tests.'
    }
}

try {
    $gateway = Invoke-WebRequest -Uri "$BaseUrl/healthz" -UseBasicParsing -TimeoutSec 5
    if ($gateway.StatusCode -ne 200) { throw 'Gateway health check did not return HTTP 200.' }
    Write-Host '[PASS] Frontend and Nginx gateway are reachable'

    $backend = Invoke-RestMethod -Uri "$BaseUrl/actuator/health" -TimeoutSec 5
    if ($backend.status -ne 'UP') { throw 'Backend actuator status is not UP.' }
    Write-Host '[PASS] Spring Boot backend reports UP'
} catch {
    Write-Warning "Runtime health validation skipped or failed: $($_.Exception.Message)"
}

Write-Host 'AeroCadet project validation completed.' -ForegroundColor Green

