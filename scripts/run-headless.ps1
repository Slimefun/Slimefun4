#requires -Version 5.1
<#
    Non-interactive launcher for the Slimefun universal-jar dev server.

    Boots the server for a chosen MC version (optionally with addons), captures ALL output to a log
    file, waits until the server has finished starting (Paper's "Done (" line) plus a grace period so
    Slimefun's post-start item-loading and addon enable errors are captured, then force-stops the whole
    process tree and exits. This lets the boot be run head-less (e.g. from CI/automation) and the log
    inspected afterward, instead of the interactive run.ps1 which holds the console open forever.

    Runs gradle with --no-daemon so the forked Paper server is a descendant of this process and the
    recursive tree-kill reliably stops it (a plain daemon run leaves the server orphaned).

    Usage:
      ./run-headless.ps1 -McVersion 1.8.8 -AllAddons
      ./run-headless.ps1 -McVersion 26.1.2 -Addons "Slimefun5/InfinityExpansion@experimental"
      ./run-headless.ps1 -McVersion 1.20.6            # core only
#>
param(
    [string]$McVersion = "26.1.2",
    [string]$Addons = "",                 # "Owner/Repo@branch,..." (empty = core only, unless -AllAddons)
    [switch]$AllAddons,                   # build all known addons @ experimental
    [switch]$LocalAddons,                 # build the addons-src working copies as-is (skip git reset)
    [int]$ReadyTimeoutSec = 600,          # max wait for the server to finish starting
    [int]$GraceSec = 30,                  # extra capture after startup for post-start tasks/addon loading
    [string]$LogFile = ""
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

if ($AllAddons -and -not $Addons) {
    $branch = "experimental"
    $repos = @(
        "Slimefun5/InfinityLib", "Slimefun5/InfinityExpansion", "Slimefun5/Networks", "Slimefun5/ExoticGarden",
        "Slimefun5/DynaTech", "Slimefun5/Galactifun", "Slimefun5/SlimeTinker", "Slimefun5/FluffyMachines",
        "Slimefun5/LiteXpansion", "Slimefun5/SensibleToolbox", "Slimefun5/ChestTerminal", "Slimefun5/ExtraGear",
        "Slimefun5/LuckyBlocks", "Slimefun5/MissileWarfare", "Slimefun5/SlimefunAdvancements", "Slimefun5/SoulJars",
        "Slimefun5/SMG", "Slimefun5/SimpleUtils", "Slimefun5/FoxyMachines", "Slimefun5/GeneticChickengineering",
        "Slimefun5/Supreme"
    )
    $Addons = ($repos | ForEach-Object { "$_@$branch" }) -join ','
}

if (-not $LogFile) { $LogFile = Join-Path $projectRoot "build/headless-$McVersion.log" }
$errFile = "$LogFile.err"
New-Item -ItemType Directory -Force -Path (Split-Path -Parent $LogFile) | Out-Null
Remove-Item $LogFile, $errFile -ErrorAction SilentlyContinue

$argList = "runServer -PmcVersion=$McVersion --no-daemon "
$argList += if ($Addons) { "-Paddons=$Addons" } else { "-PskipAddons" }
if ($LocalAddons) { $argList += " -PlocalAddons" }

Write-Host "[headless] MC $McVersion | addons=$(if ($Addons) { $Addons } else { '(none)' })"
Write-Host "[headless] log: $LogFile"

$proc = Start-Process -FilePath "$projectRoot\gradlew.bat" -ArgumentList $argList `
    -WorkingDirectory $projectRoot -RedirectStandardOutput $LogFile -RedirectStandardError $errFile `
    -PassThru -WindowStyle Hidden

function Stop-Tree([int]$processId) {
    Get-CimInstance Win32_Process -Filter "ParentProcessId=$processId" -ErrorAction SilentlyContinue |
        ForEach-Object { Stop-Tree $_.ProcessId }
    Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
}

$deadline = (Get-Date).AddSeconds($ReadyTimeoutSec)
$ready = $false
while (-not $proc.HasExited -and (Get-Date) -lt $deadline) {
    if ((Test-Path $LogFile) -and (Select-String -Path $LogFile -Pattern 'Done \(' -Quiet -ErrorAction SilentlyContinue)) {
        $ready = $true
        break
    }
    Start-Sleep -Seconds 2
}

if ($ready) {
    Write-Host "[headless] server started; capturing for ${GraceSec}s then stopping..."
    Start-Sleep -Seconds $GraceSec
} elseif ($proc.HasExited) {
    Write-Host "[headless] process exited before startup (build/boot failure) - see log"
} else {
    Write-Host "[headless] timed out after ${ReadyTimeoutSec}s waiting for startup - stopping"
}

# Always sweep, even when the gradle process self-exited (a failed build still leaves the forked
# Paper server, the runServer wrapper JVM, and gradle daemons alive - those orphans hold plugin jars
# open and break the next run's copy step).
if (-not $proc.HasExited) { Stop-Tree $proc.Id }
$rootPattern = [regex]::Escape($projectRoot)
Get-CimInstance Win32_Process -Filter "Name='java.exe'" -ErrorAction SilentlyContinue |
    Where-Object {
        $_.CommandLine -and (
            $_.CommandLine -match 'paperclip|patched_|paper-' -or          # forked Paper server
            $_.CommandLine -match 'GradleWrapperMain.*runServer' -or        # the runServer wrapper JVM
            ($_.CommandLine -match 'GradleDaemon' -and $_.CommandLine -match $rootPattern)  # daemon for THIS project
        )
    } |
    ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }

Write-Host "HEADLESS_DONE ready=$ready exited=$($proc.HasExited) log=$LogFile"
