# Ejecuta la automatizacion. Si hace falta, primero pide la clave del portal y el
# inicio de sesion de Google en el perfil de Chrome de la automatizacion.
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
# Fuera del proyecto (y de OneDrive); debe coincidir con drivers.PerfilChrome
$perfil = Join-Path $env:LOCALAPPDATA "PortalPagosEmpresas\chrome-automatizacion"

function Test-CuentaGoogle {
  $pref = Join-Path $perfil "Default\Preferences"
  if (-not (Test-Path $pref)) { return $false }
  try { return @((Get-Content $pref -Raw | ConvertFrom-Json).account_info).Count -gt 0 } catch { return $false }
}

Write-Host ""
Write-Host "=== Automatizacion Portal Pagos Empresas ===" -ForegroundColor Cyan

# 1) Clave del portal de pruebas (se guarda una sola vez en el usuario de Windows)
$clave = [Environment]::GetEnvironmentVariable('PORTAL_PAGOS_PASSWORD', 'User')
if (-not $clave) {
  Write-Host ""
  Write-Host "No esta configurada la clave del portal de pruebas (PORTAL_PAGOS_PASSWORD)." -ForegroundColor Yellow
  $segura = Read-Host "Escriba la clave del portal y presione Enter" -AsSecureString
  $clave = [Runtime.InteropServices.Marshal]::PtrToStringAuto(
    [Runtime.InteropServices.Marshal]::SecureStringToBSTR($segura))
  if (-not $clave) { throw "No se ingreso la clave." }
  [Environment]::SetEnvironmentVariable('PORTAL_PAGOS_PASSWORD', $clave, 'User')
  Write-Host "Clave guardada para este usuario de Windows." -ForegroundColor Green
}
$env:PORTAL_PAGOS_PASSWORD = $clave

# 2) Sesion de Google en el perfil de la automatizacion
if (-not (Test-CuentaGoogle)) {
  $chrome = @(
    "$env:ProgramFiles\Google\Chrome\Application\chrome.exe",
    "${env:ProgramFiles(x86)}\Google\Chrome\Application\chrome.exe",
    "$env:LOCALAPPDATA\Google\Chrome\Application\chrome.exe"
  ) | Where-Object { Test-Path $_ } | Select-Object -First 1
  if (-not $chrome) { throw "No se encontro chrome.exe" }

  New-Item -ItemType Directory -Force $perfil | Out-Null
  Write-Host ""
  Write-Host "El perfil de la automatizacion NO tiene sesion de Google." -ForegroundColor Yellow
  Write-Host "  1) En el Chrome que se abre, inicie sesion con la cuenta de Google de la automatizacion."
  Write-Host "  2) Cierre esa ventana de Chrome con la X."
  Write-Host "  Las pruebas empiezan solas cuando cierre Chrome."
  Start-Process -FilePath $chrome -Wait -ArgumentList @(
    "--user-data-dir=`"$perfil`"", "--profile-directory=Default", "--no-first-run",
    "https://accounts.google.com")

  if (Test-CuentaGoogle) {
    Write-Host "Sesion de Google guardada." -ForegroundColor Green
  } else {
    Write-Host "No se detecto sesion de Google; se continua igual (el captcha podria pedir imagenes)." -ForegroundColor Yellow
  }
} else {
  Write-Host "Perfil con sesion de Google: OK" -ForegroundColor Green
}

# 3) Pruebas (los tags se eligen en src\test\java\runners\GeneralRunner.java)
Write-Host ""
Write-Host "Ejecutando pruebas... no cierre esta ventana ni la de Chrome." -ForegroundColor Cyan
Write-Host "Si el captcha pide imagenes, resuelvalo en Chrome (hay 3 minutos)."
& .\gradlew.bat clean test --console=plain
$codigo = $LASTEXITCODE

Write-Host ""
if ($codigo -eq 0) {
  Write-Host "PRUEBAS OK" -ForegroundColor Green
} else {
  Write-Host "HUBO FALLOS (codigo $codigo)" -ForegroundColor Red
}
Write-Host "Reporte: $PSScriptRoot\target\site\serenity\index.html"
Write-Host "Evidencias Word: $PSScriptRoot\target\reportes"
exit $codigo
