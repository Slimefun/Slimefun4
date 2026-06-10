#requires -Version 5.1
<#
    Interactive launcher for the Slimefun universal-jar dev server (Windows / PowerShell).

    Why a separate script instead of an in-build menu: Gradle runs in a background daemon with no
    attached console, so an interactive prompt inside build.gradle.kts has no keyboard to read and
    silently falls back to its defaults (latest version, no addons). Running the menus here - in your
    own terminal - gives full keyboard access, and we then invoke gradlew with the chosen flags.
    Building the flags programmatically also avoids PowerShell 5.1 splitting "-PmcVersion=1.8.8" at the
    dot, which happens when you type that argument directly.

    Usage:   ./run.ps1
#>

$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

$versions = @(
    "1.8.8", "1.9.4", "1.10.2", "1.11.2", "1.12.2", "1.13.2", "1.14.4", "1.15.2",
    "1.16.5", "1.17.1", "1.18.2", "1.19.4", "1.20.6", "1.21.11", "26.1.2"
)

# Edit these as the addon forks are finalised (format: Owner/Repo). InfinityLib and Networks are
# shared libraries other addons depend on, so they are listed first.
$availableAddons = @(
    "intisy/InfinityLib", "intisy/Networks", "intisy/InfinityExpansion", "intisy/ExoticGarden",
    "intisy/DynaTech", "intisy/Galactifun", "intisy/SlimeTinker", "intisy/FluffyMachines",
    "intisy/LiteXpansion", "intisy/SensibleToolbox", "intisy/ChestTerminal", "intisy/ExtraGear",
    "intisy/LuckyBlocks", "intisy/MissileWarfare", "intisy/SlimefunAdvancements"
)

function Select-Version {
    $index = $versions.Length - 1
    while ($true) {
        Clear-Host
        Write-Host "=========================================" -ForegroundColor Cyan
        Write-Host "   Slimefun5 - Select Server Version     " -ForegroundColor Cyan
        Write-Host "=========================================" -ForegroundColor Cyan
        Write-Host "Use [W]/[S] or [Up]/[Down] to move, [Enter] to select." -ForegroundColor DarkGray
        Write-Host ""
        for ($i = 0; $i -lt $versions.Length; $i++) {
            if ($i -eq $index) { Write-Host "  > $($versions[$i])" -ForegroundColor Green }
            else { Write-Host "    $($versions[$i])" }
        }
        $key = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        switch ($key.VirtualKeyCode) {
            38 { $index--; if ($index -lt 0) { $index = $versions.Length - 1 } }
            87 { $index--; if ($index -lt 0) { $index = $versions.Length - 1 } }
            40 { $index++; if ($index -ge $versions.Length) { $index = 0 } }
            83 { $index++; if ($index -ge $versions.Length) { $index = 0 } }
            13 { return $versions[$index] }
        }
    }
}

function Select-Addons {
    $selected = New-Object bool[] $availableAddons.Length
    $index = 0
    while ($true) {
        Clear-Host
        Write-Host "=========================================" -ForegroundColor Cyan
        Write-Host "   Slimefun5 - Select Addons to Build    " -ForegroundColor Cyan
        Write-Host "=========================================" -ForegroundColor Cyan
        Write-Host "[W]/[S] or [Up]/[Down] to move, [Space] to toggle, [Enter] to confirm." -ForegroundColor DarkGray
        Write-Host "Select none to run the core only." -ForegroundColor DarkGray
        Write-Host "(Addons are not Java-8-ported yet - selecting one will fail its build.)" -ForegroundColor DarkYellow
        Write-Host ""
        for ($i = 0; $i -lt $availableAddons.Length; $i++) {
            $mark = if ($selected[$i]) { "[x]" } else { "[ ]" }
            if ($i -eq $index) { Write-Host "  > $mark $($availableAddons[$i])" -ForegroundColor Green }
            else { Write-Host "    $mark $($availableAddons[$i])" }
        }
        $key = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        switch ($key.VirtualKeyCode) {
            38 { $index--; if ($index -lt 0) { $index = $availableAddons.Length - 1 } }
            87 { $index--; if ($index -lt 0) { $index = $availableAddons.Length - 1 } }
            40 { $index++; if ($index -ge $availableAddons.Length) { $index = 0 } }
            83 { $index++; if ($index -ge $availableAddons.Length) { $index = 0 } }
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

$version = Select-Version
$addons = Select-Addons
Clear-Host

# Build the argument list programmatically so PowerShell never re-parses (and mis-splits) the
# dotted version string.
$gradleArgs = @("runServer", "-PmcVersion=$version")
if ($addons.Count -gt 0) {
    $gradleArgs += "-Paddons=$($addons -join ',')"
    Write-Host "Launching Minecraft $version with addons: $($addons -join ', ')" -ForegroundColor Green
} else {
    $gradleArgs += "-PskipAddons"
    Write-Host "Launching Minecraft $version (core only)" -ForegroundColor Green
}
Write-Host ""

& "$PSScriptRoot\gradlew.bat" @gradleArgs
exit $LASTEXITCODE
