param(
    [Parameter(Mandatory = $true)][string]$DockerCli,
    [Parameter(Mandatory = $true)][string]$Workspace
)

$ErrorActionPreference = 'Stop'
if (-not $env:JENKINS_HOME) { throw 'JENKINS_HOME is required to preserve CI deployment secrets.' }

$secretFile = Join-Path $env:JENKINS_HOME 'secrets\aerocadet-ci.env'
$workspaceFile = Join-Path $Workspace '.env.jenkins'

function Get-ContainerEnv([string]$containerName, [string]$key) {
    $json = & $DockerCli inspect --format '{{json .Config.Env}}' $containerName 2>$null
    if ($LASTEXITCODE -ne 0 -or -not $json) { return $null }
    $entry = @($json | ConvertFrom-Json) | Where-Object { $_ -like "$key=*" } | Select-Object -First 1
    if ($entry) { return $entry.Substring($key.Length + 1) }
    return $null
}

if (-not (Test-Path -LiteralPath $secretFile)) {
    $existingVolume = & $DockerCli volume inspect aerocadet-ci_postgres-data --format '{{.Name}}' 2>$null
    $volumeExists = $LASTEXITCODE -eq 0 -and $existingVolume
    $databasePassword = Get-ContainerEnv 'aerocadet-ci-postgres-1' 'POSTGRES_PASSWORD'
    if ($volumeExists -and -not $databasePassword) {
        throw 'The CI database volume exists, but its password cannot be recovered from the running container. Do not generate a new password for this volume.'
    }

    if (-not $databasePassword) { $databasePassword = [guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N') }
    $jwtSecret = Get-ContainerEnv 'aerocadet-ci-backend-1' 'JWT_SECRET'
    if (-not $jwtSecret) { $jwtSecret = [guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N') }
    $demoPassword = Get-ContainerEnv 'aerocadet-ci-backend-1' 'DEMO_ACCOUNT_PASSWORD'
    if (-not $demoPassword) { $demoPassword = [guid]::NewGuid().ToString('N') }

    $lines = @(
        'DATABASE_NAME=aerocadet',
        'DATABASE_USERNAME=aerocadet',
        "DATABASE_PASSWORD=$databasePassword",
        "JWT_SECRET=$jwtSecret",
        "DEMO_ACCOUNT_PASSWORD=$demoPassword",
        'DEMO_DATA_ENABLED=true',
        'APP_HOST_PORT=18090',
        'BACKEND_HOST_PORT=18081',
        'POSTGRES_HOST_PORT=15433'
    )
    [IO.File]::WriteAllLines($secretFile, $lines, [Text.Encoding]::ASCII)
}

Copy-Item -LiteralPath $secretFile -Destination $workspaceFile -Force
Write-Output 'CI environment prepared with persistent deployment credentials.'
