[CmdletBinding()]
param()

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot

function Invoke-NativeChecked {
    param([scriptblock]$Command, [string]$Description)
    & $Command
    if ($LASTEXITCODE -ne 0) { throw "$Description failed with exit code $LASTEXITCODE" }
}

Push-Location (Join-Path $projectRoot 'backend')
try {
    Invoke-NativeChecked { & .\mvnw.cmd -B test } 'Backend tests'
} finally { Pop-Location }

Push-Location (Join-Path $projectRoot 'frontend')
try {
    Invoke-NativeChecked { & npm.cmd ci --no-audit --no-fund } 'Frontend dependency installation'
    Invoke-NativeChecked { & npm.cmd run test:ci } 'Frontend tests'
    Invoke-NativeChecked { & npm.cmd run build } 'Frontend build'
} finally { Pop-Location }

Write-Host 'AeroCadet Freestyle build completed successfully.' -ForegroundColor Green
