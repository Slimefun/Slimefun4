#requires -Version 5.1
<#
    Interactive launcher for the Slimefun universal-jar dev server (Windows / PowerShell).

    Why a separate script instead of an in-build menu: Gradle runs in a background daemon with no
    attached console, so an interactive prompt inside build.gradle.kts has no keyboard to read and
    silently falls back to its defaults. Running the menus here - in your own terminal - gives full
    keyboard access, and we then invoke gradlew with the chosen flags. Building the flags
    programmatically also avoids PowerShell 5.1 splitting "-PmcVersion=1.8.8" at the dot.

    The previous selection is remembered in build/.last-run.json, so pressing Enter through the
    menus immediately re-runs the last configuration.

    Usage:   ./run.ps1
#>

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$stateFile = Join-Path $projectRoot "build/.last-run.json"

$versions = @(
    "1.8.8", "1.9.4", "1.10.2", "1.11.2", "1.12.2", "1.13.2", "1.14.4", "1.15.2",
    "1.16.5", "1.17.1", "1.18.2", "1.19.4", "1.20.6", "1.21.11", "26.1.2"
)

# Format: Owner/Repo. Build order matters: InfinityLib first, then InfinityExpansion (Networks depends on it), then Networks.
$availableAddons = @(
    "intisy/InfinityLib", "intisy/InfinityExpansion", "intisy/Networks", "intisy/ExoticGarden",
    "intisy/DynaTech", "intisy/Galactifun", "intisy/SlimeTinker", "intisy/FluffyMachines",
    "intisy/LiteXpansion", "intisy/SensibleToolbox", "intisy/ChestTerminal", "intisy/ExtraGear",
    "intisy/LuckyBlocks", "intisy/MissileWarfare", "intisy/SlimefunAdvancements"
)

$defaultBranch = "main"

$addonsRoot = Join-Path (Split-Path -Parent (Split-Path -Parent $projectRoot)) "addons"

function Get-LocalBranch([string]$repo) {
    $repoName = $repo.Split("/")[-1]
    $localDir = Join-Path $addonsRoot $repoName
    if (Test-Path (Join-Path $localDir ".git")) {
        try {
            $b = & git -C $localDir rev-parse --abbrev-ref HEAD 2>$null
            if ($b) { return $b.Trim() }
        } catch {}
    }
    return $defaultBranch
}

function Get-LocalBranches([string]$repo) {
    $repoName = $repo.Split("/")[-1]
    $localDir = Join-Path $addonsRoot $repoName
    if (Test-Path (Join-Path $localDir ".git")) {
        try {
            $raw = & git -C $localDir branch --format="%(refname:short)" 2>$null
            $branches = @($raw | Where-Object { $_ -ne "" })
            if ($branches.Count -gt 0) { return $branches + @("Custom...") }
        } catch {}
    }
    return @("main", "master", "Custom...")
}

function IsLocalAddon([string]$repo) {
    $repoName = $repo.Split("/")[-1]
    return Test-Path (Join-Path $addonsRoot $repoName)
}

function Load-State {
    if (Test-Path $stateFile) {
        try { return Get-Content $stateFile -Raw | ConvertFrom-Json } catch { return $null }
    }
    return $null
}

function Save-State($version, $selections) {
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $stateFile) | Out-Null
    [PSCustomObject]@{ version = $version; selections = $selections } |
        ConvertTo-Json -Depth 4 | Set-Content -Path $stateFile -Encoding UTF8
}

# Draws a whole frame anchored at the top-left, padding each line to the visible window width so the
# previous frame is overwritten in place. Anchoring at (0,0) and padding to the *window* (not buffer)
# width is what prevents the flicker and the wrapped/leftover rows: a buffer-width pad wraps lines in a
# narrower window, which breaks the one-row-per-item cursor math. Callers clear once on entry so a
# shorter frame cannot leave stale rows below.
function Write-Frame($lines) {
    [Console]::SetCursorPosition(0, 0)
    $width = [Console]::WindowWidth - 1
    if ($width -lt 1) { $width = 79 }
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $text = $lines[$i].Text
        if ($text.Length -gt $width) { $text = $text.Substring(0, $width) }
        $text = $text.PadRight($width)
        if ($i -lt $lines.Count - 1) {
            Write-Host $text -ForegroundColor $lines[$i].Color
        } else {
            Write-Host $text -ForegroundColor $lines[$i].Color -NoNewline
        }
    }
}

function Read-MenuKey {
    return $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown").VirtualKeyCode
}

function New-Row($text, $color) { return @{ Text = $text; Color = $color } }

function Select-Version($startIndex) {
    $index = $startIndex
    [Console]::Clear()
    while ($true) {
        $lines = @(
            (New-Row "=========================================" "Cyan"),
            (New-Row "   Slimefun5 - Select Server Version     " "Cyan"),
            (New-Row "=========================================" "Cyan"),
            (New-Row "[W]/[S] or [Up]/[Down] to move, [Enter] to select." "DarkGray"),
            (New-Row "" "Gray")
        )
        for ($i = 0; $i -lt $versions.Length; $i++) {
            if ($i -eq $index) { $lines += New-Row "  > $($versions[$i])" "Green" }
            else { $lines += New-Row "    $($versions[$i])" "Gray" }
        }
        Write-Frame $lines
        switch (Read-MenuKey) {
            { $_ -in 38, 87 } { $index--; if ($index -lt 0) { $index = $versions.Length - 1 } }
            { $_ -in 40, 83 } { $index++; if ($index -ge $versions.Length) { $index = 0 } }
            13 { return $index }
        }
    }
}

function Select-Branch($addon, $current) {
    $choices = Get-LocalBranches $addon
    $index = [Math]::Max(0, [Array]::IndexOf($choices, $current))
    [Console]::Clear()
    while ($true) {
        $lines = @(
            (New-Row "=========================================" "Cyan"),
            (New-Row "   Branch for $addon" "Cyan"),
            (New-Row "=========================================" "Cyan"),
            (New-Row "[W]/[S] or [Up]/[Down] to move, [Enter] to select." "DarkGray"),
            (New-Row "" "Gray")
        )
        for ($i = 0; $i -lt $choices.Length; $i++) {
            if ($i -eq $index) { $lines += New-Row "  > $($choices[$i])" "Green" }
            else { $lines += New-Row "    $($choices[$i])" "Gray" }
        }
        Write-Frame $lines
        switch (Read-MenuKey) {
            { $_ -in 38, 87 } { $index--; if ($index -lt 0) { $index = $choices.Length - 1 } }
            { $_ -in 40, 83 } { $index++; if ($index -ge $choices.Length) { $index = 0 } }
            13 {
                $choice = $choices[$index]
                if ($choice -eq "Custom...") {
                    [Console]::Clear()
                    $custom = Read-Host "Enter branch name for $addon"
                    if ([string]::IsNullOrWhiteSpace($custom)) { return $current }
                    return $custom.Trim()
                }
                return $choice
            }
        }
    }
}

function Select-Addons($lastSelections) {
    $count = $availableAddons.Length
    $doneIndex = $count
    $selected = New-Object bool[] $count
    $branches = New-Object string[] $count
    for ($i = 0; $i -lt $count; $i++) {
        $previous = $lastSelections | Where-Object { $_.repo -eq $availableAddons[$i] } | Select-Object -First 1
        if ($previous) { $selected[$i] = $true; $branches[$i] = $previous.branch }
        else { $branches[$i] = Get-LocalBranch $availableAddons[$i] }
    }
    $index = 0
    [Console]::Clear()
    while ($true) {
        $lines = @(
            (New-Row "=========================================" "Cyan"),
            (New-Row "   Slimefun5 - Select Addons to Build    " "Cyan"),
            (New-Row "=========================================" "Cyan"),
            (New-Row "[Space] toggle, [Enter] pick its branch, [Enter] on Done to launch." "DarkGray"),
            (New-Row "Select none to run the core only." "DarkGray"),
            (New-Row "" "Gray")
        )
        for ($i = 0; $i -lt $count; $i++) {
            $mark = if ($selected[$i]) { "[x]" } else { "[ ]" }
            $suffix = if ($selected[$i]) { "  ($($branches[$i]))" } else { "" }
            $text = "$mark $($availableAddons[$i])$suffix"
            if ($i -eq $index) { $lines += New-Row "  > $text" "Green" }
            else { $lines += New-Row "    $text" "Gray" }
        }
        $doneText = "Done - launch server"
        if ($index -eq $doneIndex) { $lines += New-Row "  > $doneText" "Yellow" }
        else { $lines += New-Row "    $doneText" "Yellow" }

        Write-Frame $lines
        switch (Read-MenuKey) {
            { $_ -in 38, 87 } { $index--; if ($index -lt 0) { $index = $doneIndex } }
            { $_ -in 40, 83 } { $index++; if ($index -gt $doneIndex) { $index = 0 } }
            32 { if ($index -lt $count) { $selected[$index] = -not $selected[$index] } }
            13 {
                if ($index -eq $doneIndex) {
                    $chosen = @()
                    for ($i = 0; $i -lt $count; $i++) {
                        if ($selected[$i]) {
                            $chosen += [PSCustomObject]@{ repo = $availableAddons[$i]; branch = $branches[$i] }
                        }
                    }
                    return ,$chosen
                } else {
                    $selected[$index] = $true
                    $branches[$index] = Select-Branch $availableAddons[$index] $branches[$index]
                    [Console]::Clear()
                }
            }
        }
    }
}

$state = Load-State
$lastVersion = if ($state) { $state.version } else { $versions[-1] }
$lastSelections = if ($state -and $state.selections) { @($state.selections) } else { @() }

$startIndex = [Array]::IndexOf($versions, $lastVersion)
if ($startIndex -lt 0) { $startIndex = $versions.Length - 1 }

$version = $versions[(Select-Version $startIndex)]
$selections = Select-Addons $lastSelections

Save-State $version $selections
[Console]::Clear()

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
