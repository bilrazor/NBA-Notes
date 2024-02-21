# NBA Notes - Aplicación Móvil
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)


![Logo NBA Notes](https://github.com/bilrazor/NBA-Notes/assets/113417155/a1d48f80-152e-44a9-81cd-a14ba05d31f1)

## Descripción General
NBA Notes es una aplicación móvil diseñada para facilitar la gestión de notas, con funcionalidades intuitivas y una interfaz amigable.

## Descripción General

NBA Notes es una aplicación móvil diseñada para facilitar la gestión de notas, con funcionalidades intuitivas y una interfaz amigable.

## 👨‍💻 Integrantes del Equipo

- Billy Daniel Hanover Tapia
- Sergio Fariñas Fernández
- Narciso Cordeiro Ríos

## Pantallas de la Aplicación

### Pantalla 1: Registrarse

- Icono de la Aplicación: Situado en la parte superior para identificación inmediata.
- Título: "Register User".
- Campos de Entrada: Usuario, Correo Electrónico, Contraseña.
- Botón de Registro: Para crear una nueva cuenta.
- Enlace a Iniciar Sesión: Para usuarios existentes.

### Pantalla 2: Inicio de Sesión

- Icono y Título: Identificación clara de la función de la pantalla.
- Campos de Usuario y Contraseña: Con íconos representativos.
- Botón de Inicio de Sesión: Para acceder a la cuenta.
- Enlace para Registrarse: Para nuevos usuarios.

### Pantalla Principal: NBA Notes

- Menú Hamburguesa y Media Luna: Para navegación y cambio de tema.
- Icono de Logout: Para cerrar sesión.
- Lista de Notas: Con opciones de favoritos y botón para añadir nuevas notas.

### Menú de Navegación

- Inicio, Pantalla de Perfil, Notas Favoritas: Opciones de navegación rápida.
- Ordenar Notas: Por fecha de creación en orden ascendente o descendente.

### Pantalla de Creación/Edición de Notas

- Herramientas de Edición: Incluye funcionalidades para la gestión de notas.

### Icono de Nota

Un icono creado en las coordenadas donde fue creada la nota. Si es clicado, te lleva a la nota.

### Pantalla con el Calendario de Notas

Este fragmento se encarga de mostrar un calendario y marcar de un color los días en los que se han creado/editado notas, además de señalar el día actual con un color diferente. También muestra el título de la nota (o notas) cuando haces clic en dicho día, los cuales son clicables y te llevarán a la vista detallada de dicha nota. Está compuesto por:

- **1 MCalendarView**: Es, en esencia, igual que un CalendarView, solo que forma parte de una librería de terceros que proporciona un nivel mayor de personalización, dando la oportunidad de marcar días.
- **1 TextView**: En él se muestra un texto que dice "Elige la fecha", el cual cambia para mostrar el título de la nota o notas que han sido editadas/creadas en el día seleccionado.


## Validaciones en el registro de usarios
Para el registro de nuevos usuarios en la aplicación NBA Notes, es importante tener en cuenta las siguientes validaciones para asegurar la integridad y seguridad de los datos:

### Username:

Debe tener una longitud mínima de 3 caracteres y no superar los 20 caracteres.
Es un campo obligatorio y no debe estar vacío.
### Email:

Debe ser un correo electrónico válido conforme a las normas estándar de formato de email.
La longitud máxima permitida es de 50 caracteres.
Este campo es obligatorio y no puede estar vacío.
### Contraseña:

La longitud de la contraseña debe ser de al menos 6 caracteres y no debe exceder los 40 caracteres.
Este campo es obligatorio y no debe estar vacío.

## Tecnologías Utilizadas
Para el desarrollo de NBA Notes, hemos utilizado tecnologías clave que nos ayudan a ofrecer una experiencia de usuario fluida y funcionalidades robustas:

- Java: Lenguaje de programación principal para la lógica de la aplicación Android. Nos permite crear una aplicación segura y eficiente.
- Spring Boot: Usado para el backend, facilita el desarrollo rápido de aplicaciones web y servicios REST, simplificando la configuración y despliegue.
- Android: Plataforma de desarrollo para la aplicación móvil, proporciona las herramientas y APIs necesarias para aprovechar al máximo el hardware y software de dispositivos Android.


## Contacto
Para cualquier consulta o sugerencia, no dudes en contactarme:

- **Email**: [billydht5@gmail.com](mailto:billydht5@gmail.com)

## Licencia
Este proyecto está licenciado bajo la Licencia Apache 2.0 .
