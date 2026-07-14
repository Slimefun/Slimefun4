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
    "1.16.5", "1.17.1", "1.18.2", "1.19.4", "1.20.6", "1.21.11", "26.1.2", "26.2"
)

# Format: Owner/Repo. Build order matters: InfinityLib first, then InfinityExpansion (Networks depends on it), then Networks.
$availableAddons = @(
    "Slimefun5/InfinityLib", "Slimefun5/InfinityExpansion", "Slimefun5/Networks", "Slimefun5/ExoticGarden",
    "Slimefun5/DynaTech", "Slimefun5/Galactifun", "Slimefun5/SlimeTinker", "Slimefun5/FluffyMachines",
    "Slimefun5/LiteXpansion", "Slimefun5/SensibleToolbox", "Slimefun5/ChestTerminal", "Slimefun5/ExtraGear",
    "Slimefun5/LuckyBlocks", "Slimefun5/MissileWarfare", "Slimefun5/SlimefunAdvancements", "Slimefun5/SoulJars",
    "Slimefun5/SMG", "Slimefun5/SimpleUtils", "Slimefun5/FoxyMachines", "Slimefun5/GeneticChickengineering",
    "Slimefun5/Supreme", "Slimefun5/FastMachines"
)

function Resolve-AllBranches($repos) {
    Write-Host "Resolving addon branches from GitHub..." -ForegroundColor DarkGray

    $work = {
        param($r)
        $out = & git ls-remote --symref "https://github.com/$r.git" 2>$null
        if ($LASTEXITCODE -ne 0 -or -not $out) { return $null }
        $default = $null
        $branches = @()
        foreach ($line in $out) {
            if ($line -match '^ref:\s+refs/heads/(\S+)\s+HEAD') { $default = $matches[1] }
            elseif ($line -match 'refs/heads/(\S+)$') { $branches += $matches[1] }
        }
        return @{ Default = $default; Branches = @($branches) }
    }

    $pool = [RunspaceFactory]::CreateRunspacePool(1, [Math]::Min(16, $repos.Count))
    $pool.Open()

    $tasks = foreach ($repo in $repos) {
        $ps = [PowerShell]::Create()
        $ps.RunspacePool = $pool
        $null = $ps.AddScript($work).AddArgument($repo)
        [PSCustomObject]@{ Repo = $repo; PS = $ps; Handle = $ps.BeginInvoke() }
    }

    $map = @{}
    foreach ($task in $tasks) {
        $result = $task.PS.EndInvoke($task.Handle)
        $map[$task.Repo] = if ($result.Count -gt 0) { $result[0] } else { $null }
        $task.PS.Dispose()
    }

    $pool.Close()
    $pool.Dispose()
    return $map
}

function Load-State {
    if (Test-Path $stateFile) {
        try { return Get-Content $stateFile -Raw | ConvertFrom-Json } catch { return $null }
    }
    return $null
}

function Save-State($version, $selections, $options, $branches) {
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $stateFile) | Out-Null
    [PSCustomObject]@{ version = $version; selections = $selections; options = $options; branches = $branches } |
        ConvertTo-Json -Depth 4 | Set-Content -Path $stateFile -Encoding UTF8
}

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
    $info = $script:branchMap[$addon]

    # Repo unreachable, or resolved with zero branches: nothing to pick. Show why and go back.
    if ($null -eq $info -or @($info.Branches).Count -eq 0) {
        $reason = if ($null -eq $info) { "could not be reached on GitHub" } else { "is empty (no branches)" }
        [Console]::Clear()
        Write-Frame @(
            (New-Row "=========================================" "Cyan"),
            (New-Row "   Branch for $addon" "Cyan"),
            (New-Row "=========================================" "Cyan"),
            (New-Row "" "Gray"),
            (New-Row "  This repository $reason." "Yellow"),
            (New-Row "" "Gray"),
            (New-Row "  Press [Enter] to go back." "DarkGray")
        )
        while ((Read-MenuKey) -ne 13) {}
        return $current
    }

    # Live branches from GitHub, plus a manual-entry escape hatch.
    $choices = @($info.Branches) + @("Custom...")
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

function Select-Addons($lastSelections, $lastBranches) {
    $count = $availableAddons.Length
    $doneIndex = $count
    $selected = New-Object bool[] $count
    $branches = New-Object string[] $count
    for ($i = 0; $i -lt $count; $i++) {
        $previous = $lastSelections | Where-Object { $_.repo -eq $availableAddons[$i] } | Select-Object -First 1
        # Branch remembered from a prior run even if the addon was deselected, so toggling it off then
        # back on keeps the previously chosen branch instead of resetting to the default.
        $remembered = $null
        if ($lastBranches) {
            $prop = $lastBranches.PSObject.Properties[$availableAddons[$i]]
            if ($prop) { $remembered = $prop.Value }
        }
        if ($previous) {
            $selected[$i] = $true
            $branches[$i] = $previous.branch
        } elseif ($remembered) {
            $branches[$i] = $remembered
        } else {
            # No hardcoded default: use the repo's resolved HEAD, else its first branch, else blank.
            $info = $script:branchMap[$availableAddons[$i]]
            if ($info -and $info.Default) { $branches[$i] = $info.Default }
            elseif ($info -and @($info.Branches).Count -gt 0) { $branches[$i] = @($info.Branches)[0] }
            else { $branches[$i] = "" }
        }
    }
    # Start on "Done" so pressing Enter immediately re-runs the remembered configuration.
    $index = $doneIndex
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
            $info = $script:branchMap[$availableAddons[$i]]
            $mark = if ($selected[$i]) { "[x]" } else { "[ ]" }
            if ($null -eq $info) {
                $status = "  <unreachable>"
            } elseif (@($info.Branches).Count -eq 0) {
                $status = "  <empty>"
            } elseif ($selected[$i]) {
                $status = "  ($($branches[$i]))"
            } else {
                $status = ""
            }
            $text = "$mark $($availableAddons[$i])$status"
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
                    # Remember every addon's branch (selected or not) so deselecting doesn't lose the choice.
                    $script:rememberedBranches = [ordered]@{}
                    for ($i = 0; $i -lt $count; $i++) {
                        if ($branches[$i]) { $script:rememberedBranches[$availableAddons[$i]] = $branches[$i] }
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

function Select-Options($lastOptions) {
    # Toggleable launch options, each mapped to a gradle -P flag below. Defaults match the build:
    # ViaVersion auto-install is ON (the build enables it unless -PnoVia), localAddons is OFF.
    $via = if ($null -ne $lastOptions -and $null -ne $lastOptions.via) { [bool]$lastOptions.via } else { $true }
    $localAddons = if ($null -ne $lastOptions -and $null -ne $lastOptions.localAddons) { [bool]$lastOptions.localAddons } else { $false }
    $keepPlugins = if ($null -ne $lastOptions -and $null -ne $lastOptions.keepPlugins) { [bool]$lastOptions.keepPlugins } else { $false }
    $dumpItems = if ($null -ne $lastOptions -and $null -ne $lastOptions.dumpItems) { [bool]$lastOptions.dumpItems } else { $false }

    # Each label ends with the gradle -P flag it maps to, so it's clear what gets passed to runServer.
    $items = @(
        "Auto-install ViaVersion + ViaBackwards + ViaRewind  (-PnoVia when off)",
        "Build addon working copies as-is (skip git fetch/reset)  (-PlocalAddons)",
        "Keep existing plugin jars (don't clear the plugins folder)  (-PkeepPlugins)",
        "Dump untranslated-item audit on boot  (-PdumpItems)"
    )
    $values = @($via, $localAddons, $keepPlugins, $dumpItems)
    $count = $items.Length
    $doneIndex = $count
    # Start on "Done" so pressing Enter immediately launches with the remembered options.
    $index = $doneIndex
    [Console]::Clear()
    while ($true) {
        $lines = @(
            (New-Row "=========================================" "Cyan"),
            (New-Row "   Slimefun5 - Launch Options            " "Cyan"),
            (New-Row "=========================================" "Cyan"),
            (New-Row "[Space] toggle, [Enter] on Done to launch." "DarkGray"),
            (New-Row "" "Gray")
        )
        for ($i = 0; $i -lt $count; $i++) {
            $mark = if ($values[$i]) { "[x]" } else { "[ ]" }
            $text = "$mark $($items[$i])"
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
            32 { if ($index -lt $count) { $values[$index] = -not $values[$index] } }
            13 {
                if ($index -eq $doneIndex) {
                    return [PSCustomObject]@{ via = $values[0]; localAddons = $values[1]; keepPlugins = $values[2]; dumpItems = $values[3] }
                }
            }
        }
    }
}

$state = Load-State
$lastVersion = if ($state) { $state.version } else { $versions[-1] }
$lastSelections = if ($state -and $state.selections) { @($state.selections) } else { @() }
$lastOptions = if ($state -and $state.options) { $state.options } else { $null }
$lastBranches = if ($state -and $state.branches) { $state.branches } else { $null }

$startIndex = [Array]::IndexOf($versions, $lastVersion)
if ($startIndex -lt 0) { $startIndex = $versions.Length - 1 }

$version = $versions[(Select-Version $startIndex)]

# Resolve every addon's branches from GitHub up front (parallel) so the addon menu is instant.
$script:branchMap = Resolve-AllBranches $availableAddons

$selections = Select-Addons $lastSelections $lastBranches
$options = Select-Options $lastOptions

Save-State $version $selections $options $script:rememberedBranches
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

# Translate launch options to gradle -P flags (the build defaults to Via on, localAddons off).
if (-not $options.via) { $gradleArgs += "-PnoVia" }
if ($options.localAddons) { $gradleArgs += "-PlocalAddons" }
if ($options.keepPlugins) { $gradleArgs += "-PkeepPlugins" }
if ($options.dumpItems) { $gradleArgs += "-PdumpItems" }
Write-Host ("Options: ViaVersion={0}, localAddons={1}, keepPlugins={2}, dumpItems={3}" -f $options.via, $options.localAddons, $options.keepPlugins, $options.dumpItems) -ForegroundColor DarkGray
Write-Host ""

# --- Ensure a JDK is available for the Gradle wrapper ---
# gradlew bootstraps its JVM from JAVA_HOME / PATH only - it does NOT see IntelliJ's project SDK.
# So when Java isn't on the PATH, locate a JDK ourselves (no system PATH change needed): prefer an
# existing JAVA_HOME, then java already on PATH, then an IntelliJ-managed JDK (~/.jdks), then the
# IntelliJ-bundled JetBrains Runtime, then a system JDK. Needs Java 17+ (Gradle) / 21+ (the server).
function Test-JdkHome($p) { return ($p -and (Test-Path (Join-Path $p "bin\java.exe"))) }

if (-not (Test-JdkHome $env:JAVA_HOME)) {
    if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
        $jdkCandidates = New-Object System.Collections.Generic.List[string]
        Get-ChildItem "$env:USERPROFILE\.jdks" -Directory -ErrorAction SilentlyContinue |
            Sort-Object Name -Descending | ForEach-Object { $jdkCandidates.Add($_.FullName) }
        Get-ChildItem "C:\Program Files\JetBrains" -Directory -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -like 'IntelliJ IDEA*' } | Sort-Object Name -Descending |
            ForEach-Object { $jdkCandidates.Add((Join-Path $_.FullName "jbr")) }
        Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory -ErrorAction SilentlyContinue |
            Sort-Object Name -Descending | ForEach-Object { $jdkCandidates.Add($_.FullName) }
        Get-ChildItem "C:\Program Files\Java" -Directory -ErrorAction SilentlyContinue |
            Sort-Object Name -Descending | ForEach-Object { $jdkCandidates.Add($_.FullName) }

        $jdk = $jdkCandidates | Where-Object { Test-JdkHome $_ } | Select-Object -First 1
        if (-not $jdk) {
            Write-Host "ERROR: No Java found. Set JAVA_HOME to a JDK 17+ (or install one), then re-run." -ForegroundColor Red
            Write-Host "Looked in: JAVA_HOME, PATH, $env:USERPROFILE\.jdks, IntelliJ JBR, Eclipse Adoptium, Program Files\Java." -ForegroundColor Red
            exit 1
        }
        $env:JAVA_HOME = $jdk
        $env:PATH = (Join-Path $jdk "bin") + ";" + $env:PATH
        Write-Host "Using auto-detected JDK: $jdk" -ForegroundColor DarkGray
    }
} else {
    # JAVA_HOME is valid; make sure its java is also on PATH for the server JVM that gradle launches.
    $env:PATH = (Join-Path $env:JAVA_HOME "bin") + ";" + $env:PATH
}

& "$projectRoot\gradlew.bat" @gradleArgs
exit $LASTEXITCODE
