# language: es
@portal_claro_empresas
Característica: Acceso al portal Claro Empresas
  Como usuario de Claro Empresas
  Quiero abrir el portal empresarial
  Para comprobar que se encuentra disponible

  Escenario: Abrir portal Claro Empresas
    Dado que el usuario abre el portal Claro Empresas
    Entonces el portal Claro Empresas debería cargar correctamente

  @pagos_y_recargas
  Escenario: Acceder al Portal de Pagos Empresas
    Dado que el usuario abre el portal Claro Empresas
    Cuando accede a Pagos y Recargas
    Entonces debería ser dirigido al Portal de Pagos Empresas

  @seleccion_pago_soluciones_fijas
  Escenario: Seleccionar Pago Soluciones Fijas
    Dado que el usuario abre el portal Claro Empresas
    Cuando accede a Pagos y Recargas
    Y cierra el modal Información Importante
    Y selecciona Pago Soluciones Fijas
    Entonces la opción Pago Soluciones Fijas debería quedar seleccionada

  @ingreso_numero_cuenta_soluciones_fijas
  Escenario: Ingresar número de cuenta para Pago Soluciones Fijas
    Dado que el usuario abre el portal Claro Empresas
    Y cierra el modal Información Importante
    Y selecciona Pago Soluciones Fijas
    Y ingresa el número de cuenta "56220783"
    Entonces el campo número de cuenta debería contener "56220783"

  @clic_recaptcha_edge_soluciones_fijas
  Escenario: Validar clic normal del reCAPTCHA usando Edge
    Dado que el usuario abre el portal Claro Empresas usando Edge
    Y cierra el modal Información Importante
    Y selecciona Pago Soluciones Fijas
    Y ingresa el número de cuenta "56220783"
    Cuando hace clic normal en el reCAPTCHA usando Edge
    Entonces el reCAPTCHA debería quedar aprobado en Edge

  @clic_recaptcha_firefox_soluciones_fijas
  Escenario: Validar clic normal del reCAPTCHA usando Firefox
    Dado que el usuario abre el portal Claro Empresas usando Firefox
    Y cierra el modal Información Importante
    Y selecciona Pago Soluciones Fijas
    Y ingresa el número de cuenta "56220783"
    Cuando hace clic normal en el reCAPTCHA usando Firefox
    Entonces el reCAPTCHA debería quedar aprobado en Firefox

  @recaptcha_sesion_persistente
  Escenario: Resolver reCAPTCHA reutilizando la sesión persistente de Chrome
    Dado que el usuario abre el portal Claro Empresas
    Y cierra el modal Información Importante
    Y selecciona Pago Soluciones Fijas
    Y ingresa el número de cuenta "56220783"
    Cuando resuelve el reCAPTCHA usando la sesión persistente
    Entonces el reCAPTCHA debería quedar aprobado
    Cuando agrega la cuenta "56220783"
    Entonces la cuenta "56220783" debería quedar agregada
    Cuando hace clic en Continuar hacia Paga en línea
    Entonces debería ingresar a la pantalla Paga en línea
    Y deberían mostrarse los textos de detalle del pago:
      | Valor a pagar - Pago Soluciones Fijas HFC |
      | Pago total                                |
      | Detalle de tu pago                        |
    Y debería mostrarse la referencia de pago "56220783" en el detalle
    Cuando despliega Elige una opción y selecciona el medio de pago "PSE - NEQUI - DAVIPLATA"
    Entonces el medio de pago "PSE - NEQUI - DAVIPLATA" debería quedar seleccionado
