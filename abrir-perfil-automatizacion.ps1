# Abre Chrome NORMAL (sin Selenium) con el perfil que usa la automatizacion.
# Uselo una vez para iniciar sesion con su cuenta de Google; luego cierre esa
# ventana de Chrome y ejecute las pruebas: reutilizaran esa sesion.
param(
  # Fuera del proyecto (y de OneDrive); debe coincidir con drivers.PerfilChrome
  [string]$Perfil = (Join-Path $env:LOCALAPPDATA "PortalPagosEmpresas\chrome-automatizacion")
)

$chrome = @(
  "$env:ProgramFiles\Google\Chrome\Application\chrome.exe",
  "${env:ProgramFiles(x86)}\Google\Chrome\Application\chrome.exe",
  "$env:LOCALAPPDATA\Google\Chrome\Application\chrome.exe"
) | Where-Object { Test-Path $_ } | Select-Object -First 1

if (-not $chrome) { throw "No se encontro chrome.exe" }

New-Item -ItemType Directory -Force $Perfil | Out-Null

$preferencias = Join-Path $Perfil "Default\Preferences"
$conSesion = $false
if (Test-Path $preferencias) {
  try { $conSesion = @((Get-Content $preferencias -Raw | ConvertFrom-Json).account_info).Count -gt 0 } catch {}
}

Write-Host ""
if ($conSesion) {
  Write-Host "  Este perfil YA tiene una cuenta de Google. No necesita hacer nada mas." -ForegroundColor Green
  Write-Host "  (Se abre igual por si quiere revisarla o cambiarla.)"
} else {
  Write-Host "  Este perfil NO tiene cuenta de Google todavia." -ForegroundColor Yellow
}
Write-Host ""
Write-Host "  1) En la ventana de Chrome que se abre, inicie sesion con su cuenta de Google."
Write-Host "  2) Cierre esa ventana de Chrome con la X."
Write-Host "  3) Listo: ya puede ejecutar las pruebas."
Write-Host ""

Start-Process -FilePath $chrome -ArgumentList @(
  "--user-data-dir=`"$Perfil`"", "--profile-directory=Default", "--no-first-run",
  "https://accounts.google.com")
