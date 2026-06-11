#requires -Version 5.1
<#
    Interactive launcher for the Slimefun universal-jar dev server (Windows / PowerShell).

    Why a separate script instead of an in-build menu: Gradle runs in a background daemon with no
    attached console, so an interactive prompt inside build.gradle.kts has no keyboard to read and
    silently falls back to its defaults. Running the menus here - in your own terminal - gives full
    keyboard access, and we then invoke gradlew with the chosen flags. Building the flags
    programmatically also avoids PowerShell 5.1 splitting "-PmcVersion=1.8.8" at the dot.

    The previous selection is remembered in scripts/.last-run.json, so pressing Enter through the
    menus immediately re-runs the last configuration.

    Usage:   ./run.ps1
#>

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$stateFile = Join-Path $PSScriptRoot ".last-run.json"

$versions = @(
    "1.8.8", "1.9.4", "1.10.2", "1.11.2", "1.12.2", "1.13.2", "1.14.4", "1.15.2",
    "1.16.5", "1.17.1", "1.18.2", "1.19.4", "1.20.6", "1.21.11", "26.1.2"
)

# Format: Owner/Repo. InfinityLib and Networks are shared libraries other addons depend on.
$availableAddons = @(
    "intisy/InfinityLib", "intisy/Networks", "intisy/InfinityExpansion", "intisy/ExoticGarden",
    "intisy/DynaTech", "intisy/Galactifun", "intisy/SlimeTinker", "intisy/FluffyMachines",
    "intisy/LiteXpansion", "intisy/SensibleToolbox", "intisy/ChestTerminal", "intisy/ExtraGear",
    "intisy/LuckyBlocks", "intisy/MissileWarfare", "intisy/SlimefunAdvancements"
)

# Offered when picking an addon branch; "Custom..." lets you type any ref. The Java-8 port lives on
# feature/java8-universal-jar, so it is the default.
$branchChoices = @("feature/java8-universal-jar", "master", "main", "Custom...")
$defaultBranch = $branchChoices[0]

function Load-State {
    if (Test-Path $stateFile) {
        try { return Get-Content $stateFile -Raw | ConvertFrom-Json } catch { return $null }
    }
    return $null
}

function Save-State($version, $selections) {
    $state = [PSCustomObject]@{ version = $version; selections = $selections }
    $state | ConvertTo-Json -Depth 4 | Set-Content -Path $stateFile -Encoding UTF8
}

# Renders the changing menu rows in place. The header is drawn once by the caller, which records the
# row below it as $script:menuTop; we move the cursor back there each frame and overwrite the rows
# (padded to the buffer width) instead of clearing the whole screen, which removes the flicker.
function Write-Rows($rows) {
    $ui = $Host.UI.RawUI
    $pos = $ui.CursorPosition
    $pos.X = 0
    $pos.Y = $script:menuTop
    $ui.CursorPosition = $pos
    $width = $ui.BufferSize.Width - 1
    foreach ($row in $rows) {
        Write-Host ($row.Text.PadRight($width)) -ForegroundColor $row.Color
    }
}

function Read-MenuKey {
    return $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown").VirtualKeyCode
}

function Select-Version($startIndex) {
    $index = $startIndex
    Clear-Host
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "   Slimefun5 - Select Server Version     " -ForegroundColor Cyan
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "Use [W]/[S] or [Up]/[Down] to move, [Enter] to select." -ForegroundColor DarkGray
    Write-Host ""
    $script:menuTop = $Host.UI.RawUI.CursorPosition.Y

    while ($true) {
        $rows = for ($i = 0; $i -lt $versions.Length; $i++) {
            if ($i -eq $index) { @{ Text = "  > $($versions[$i])"; Color = "Green" } }
            else { @{ Text = "    $($versions[$i])"; Color = "Gray" } }
        }
        Write-Rows $rows
        switch (Read-MenuKey) {
            { $_ -in 38, 87 } { $index--; if ($index -lt 0) { $index = $versions.Length - 1 } }
            { $_ -in 40, 83 } { $index++; if ($index -ge $versions.Length) { $index = 0 } }
            13 { return $index }
        }
    }
}

function Select-Addons($preselected) {
    $selected = New-Object bool[] $availableAddons.Length
    for ($i = 0; $i -lt $availableAddons.Length; $i++) {
        if ($preselected -contains $availableAddons[$i]) { $selected[$i] = $true }
    }
    $index = 0
    Clear-Host
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "   Slimefun5 - Select Addons to Build    " -ForegroundColor Cyan
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "[W]/[S] or [Up]/[Down] to move, [Space] to toggle, [Enter] to confirm." -ForegroundColor DarkGray
    Write-Host "Select none to run the core only." -ForegroundColor DarkGray
    Write-Host ""
    $script:menuTop = $Host.UI.RawUI.CursorPosition.Y

    while ($true) {
        $rows = for ($i = 0; $i -lt $availableAddons.Length; $i++) {
            $mark = if ($selected[$i]) { "[x]" } else { "[ ]" }
            if ($i -eq $index) { @{ Text = "  > $mark $($availableAddons[$i])"; Color = "Green" } }
            else { @{ Text = "    $mark $($availableAddons[$i])"; Color = "Gray" } }
        }
        Write-Rows $rows
        switch (Read-MenuKey) {
            { $_ -in 38, 87 } { $index--; if ($index -lt 0) { $index = $availableAddons.Length - 1 } }
            { $_ -in 40, 83 } { $index++; if ($index -ge $availableAddons.Length) { $index = 0 } }
            32 { $selected[$index] = -not $selected[$index] }
            13 {
                $chosen = @()
                for ($i = 0; $i -lt $availableAddons.Length; $i++) {
                    if ($selected[$i]) { $chosen += $availableAddons[$i] }
                }
                return ,$chosen
            }
        }
    }
}

function Select-Branch($addon, $current) {
    $index = [Math]::Max(0, [Array]::IndexOf($branchChoices, $current))
    Clear-Host
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "   Branch for $addon" -ForegroundColor Cyan
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "Use [W]/[S] or [Up]/[Down] to move, [Enter] to select." -ForegroundColor DarkGray
    Write-Host ""
    $script:menuTop = $Host.UI.RawUI.CursorPosition.Y

    while ($true) {
        $rows = for ($i = 0; $i -lt $branchChoices.Length; $i++) {
            if ($i -eq $index) { @{ Text = "  > $($branchChoices[$i])"; Color = "Green" } }
            else { @{ Text = "    $($branchChoices[$i])"; Color = "Gray" } }
        }
        Write-Rows $rows
        switch (Read-MenuKey) {
            { $_ -in 38, 87 } { $index--; if ($index -lt 0) { $index = $branchChoices.Length - 1 } }
            { $_ -in 40, 83 } { $index++; if ($index -ge $branchChoices.Length) { $index = 0 } }
            13 {
                $choice = $branchChoices[$index]
                if ($choice -eq "Custom...") {
                    Clear-Host
                    $custom = Read-Host "Enter branch name for $addon"
                    return $custom.Trim()
                }
                return $choice
            }
        }
    }
}

$state = Load-State
$lastVersion = if ($state) { $state.version } else { $versions[-1] }
$lastSelections = if ($state) { $state.selections } else { @() }
$lastAddons = @($lastSelections | ForEach-Object { $_.repo })

$startIndex = [Array]::IndexOf($versions, $lastVersion)
if ($startIndex -lt 0) { $startIndex = $versions.Length - 1 }

$versionIndex = Select-Version $startIndex
$version = $versions[$versionIndex]
$addons = Select-Addons $lastAddons

$selections = @()
foreach ($addon in $addons) {
    $previous = $lastSelections | Where-Object { $_.repo -eq $addon } | Select-Object -First 1
    $current = if ($previous) { $previous.branch } else { $defaultBranch }
    $branch = Select-Branch $addon $current
    $selections += [PSCustomObject]@{ repo = $addon; branch = $branch }
}

Save-State $version $selections
Clear-Host

$gradleArgs = @("runServer", "-PmcVersion=$version")
if ($selections.Count -gt 0) {
    $addonArg = ($selections | ForEach-Object { "$($_.repo)@$($_.branch)" }) -join ','
    $gradleArgs += "-Paddons=$addonArg"
    Write-Host "Launching Minecraft $version with addons:" -ForegroundColor Green
    $selections | ForEach-Object { Write-Host "  - $($_.repo) @ $($_.branch)" -ForegroundColor Green }
} else {
    $gradleArgs += "-PskipAddons"
    Write-Host "Launching Minecraft $version (core only)" -ForegroundColor Green
}
Write-Host ""

& "$projectRoot\gradlew.bat" @gradleArgs
exit $LASTEXITCODE
