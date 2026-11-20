# language: es
@infraestructura
Característica: Validación de la infraestructura WEB
  Como equipo de automatización
  Quiero ejecutar un escenario mínimo en Chrome
  Para comprobar la integración de Serenity, Cucumber, Selenium y Screenplay

  Escenario: Abrir una página WEB mínima
    Dado que el usuario abre una pagina web de prueba
    Entonces deberia visualizar el titulo "Serenity Web"
