# PortalPagosEmpresas

Esqueleto de automatización exclusivamente WEB con Java 11, Gradle 7.4, Serenity BDD,
Cucumber, Selenium, JUnit 4 y el patrón Screenplay.

## Ejecución

Chrome y un ChromeDriver compatible deben estar disponibles en el servidor. El driver puede
configurarse mediante `webdriver.chrome.driver` o incorporarse al `PATH` del proceso.

```powershell
.\gradlew.bat clean test aggregate
```

El reporte se genera en `target/site/serenity/index.html`.
# Portal-Pagos_Empresas

## reCAPTCHA con perfil persistente (rama de pruebas)

Chrome se abre con un perfil fijo en `%LOCALAPPDATA%\PortalPagosEmpresas\chrome-automatizacion`
(no temporal/incógnito), así las cookies de Google y del portal se conservan entre ejecuciones.
Está fuera del proyecto a propósito: si el proyecto vive en OneDrive, la sincronización dañaría
el perfil mientras Chrome lo usa.

Paso previo (una sola vez): iniciar sesión con la cuenta de Google en ese perfil, abriéndolo
con Chrome normal (sin Selenium, porque Google bloquea el login en navegadores automatizados):

1. Doble clic en **`abrir-perfil-automatizacion.bat`** (en la carpeta del proyecto).
2. En el Chrome que se abre, iniciar sesión con la cuenta de Google.
3. Cerrar esa ventana de Chrome con la X.

La ventana negra indica si el perfil ya tiene cuenta de Google. La sesión dura semanas o meses;
solo hay que repetirlo si el captcha vuelve a pedir imágenes o si se borra esa carpeta.

Cambiar de cuenta de Google: cerrar el Chrome de la automatización, borrar la carpeta
`%LOCALAPPDATA%\PortalPagosEmpresas\chrome-automatizacion` y volver a ejecutar
`ejecutar-pruebas.bat` (pedirá el inicio de sesión).

Nota: desde Chrome 136 no se puede automatizar el perfil por defecto (`...\Chrome\User Data`),
por eso se usa un perfil dedicado con la sesión de Google iniciada.

```powershell
# 1ra vez: si reCAPTCHA pide imágenes, el test espera (180 s) a que lo resuelva a mano
.\gradlew.bat clean test --tests runners.GeneralRunner "-Dcucumber.options=--tags @recaptcha_sesion_persistente"

# Siguientes ejecuciones: revisar en consola RECAPTCHA_RESULTADO=APROBADO_SIN_DESAFIO
```

Propiedades: `-Dperfil.chrome.dir=<ruta>`, `-Drecaptcha.manual=false`,
`-Drecaptcha.manual.segundos=300`, `-Dchrome.perfil.persistente=false` (vuelve al Chrome temporal).
No ejecutar en paralelo con el mismo perfil (Chrome lo bloquea).

## Ejecución en el servidor (manual, por MobaXterm)

1. Conectarse al servidor por Escritorio remoto: MobaXterm > Session > RDP. Usar siempre el
   **mismo usuario de Windows**: Chrome cifra la sesión de Google con la clave de ese usuario,
   así que con otro usuario no se ve.
2. Doble clic en **`ejecutar-pruebas.bat`** (carpeta del proyecto). El script:
   - pide la clave del portal (`PORTAL_PAGOS_PASSWORD`) si no está guardada (solo la primera vez);
   - si el perfil no tiene sesión de Google, abre Chrome para iniciarla con la **cuenta de
     Google de la automatización** y espera a que se cierre esa ventana;
   - ejecuta las pruebas con los tags de `GeneralRunner`.
3. Mantener la ventana de MobaXterm abierta (sin minimizar) mientras corre: las evidencias del
   Word son capturas del escritorio.
4. Si el captcha pide imágenes, resolverlo en Chrome (hay 3 minutos).

Resultados: `target\site\serenity\index.html` y `targeteportes\`.
