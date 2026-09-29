[CmdletBinding()]
param(
    [switch]$SkipTests
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$requiredFiles = @(
    '.env.example',
    '.gitignore',
    'README.md',
    'backend/pom.xml',
    'frontend/package.json',
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

Write-Host 'Foundation validation completed.' -ForegroundColor Green

