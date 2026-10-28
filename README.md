Sistema de Entrenamiento Corporal y Preparación Física
Descripción general

El Sistema de Entrenamiento Corporal y Preparación Física es una aplicación móvil desarrollada en Kotlin con algunos componentes en Java, enfocada en apoyar al usuario en el seguimiento de su actividad física, nutrición y progreso corporal.

La aplicación cuenta con integración a Supabase como servicio backend, lo que permite la autenticación de usuarios, almacenamiento remoto de información y sincronización de datos en tiempo real.

Su arquitectura está diseñada para ofrecer una experiencia integral al usuario, abarcando cuatro módulos principales: Estadísticas, Ejercicios, Alimentos y Configuración.

Objetivo general

Desarrollar una aplicación móvil que permita registrar, monitorear y analizar la actividad física y los hábitos alimenticios del usuario, brindando información personalizada y visual sobre su progreso.

Objetivos específicos

Implementar un sistema de autenticación seguro mediante correo y contraseña.

Integrar detección de poses con MediaPipe para validar la correcta ejecución de ejercicios.

Almacenar y gestionar información de usuarios, ejercicios y alimentos en Supabase.

Calcular y mostrar estadísticas relevantes como el IMC, tiempo de ejercicio y calorías diarias.

Ofrecer recomendaciones alimenticias y permitir el registro del consumo calórico.

Arquitectura del sistema

El proyecto se compone de una aplicación móvil desarrollada en Android Studio, estructurada en XML para la interfaz de usuario y en Kotlin/Java para la lógica y detección de poses.

El backend está gestionado por Supabase, que proporciona servicios de:

Autenticación de usuarios.

Almacenamiento de datos en la nube.

Acceso remoto a las tablas que conforman la base de datos del sistema.

Estructura de módulos
1. Módulo de Estadísticas

Permite al usuario visualizar su progreso general.

Muestra gráficos sobre repeticiones, tiempo de ejercicio y calorías.

Permite actualizar datos personales como peso, edad, estatura y masa muscular.

Calcula y muestra el índice de masa corporal (IMC).

2. Módulo de Ejercicios

Presenta una lista de ejercicios clasificados por parte del cuerpo.

Ofrece rutinas predeterminadas o personalizadas.

Usa la cámara del dispositivo junto con MediaPipe para detectar y validar movimientos.

Registra automáticamente repeticiones completadas correctamente.

3. Módulo de Alimentos

Permite visualizar el metabolismo basal del usuario.

Presenta recomendaciones de comidas y bebidas clasificadas por: desayuno, comida y cena.

Permite registrar el consumo diario de calorías.

Los datos se sincronizan con la base de datos en Supabase.

4. Módulo de Configuración

Muestra los datos del usuario (nombre y correo).

Contiene la sección “Acerca de”.

Permite cerrar sesión y volver a la pantalla de inicio.

Base de datos — Estructura general (Supabase)

La base de datos se compone de las siguientes tablas:

Tabla	Descripción
alimentos	Lista de alimentos recomendados, con su tipo y calorías asociadas.
calorías	Registro del consumo diario de calorías de cada usuario.
colacion	Alimentos ligeros o snacks registrados por el usuario.
ejercicio	Catálogo de ejercicios disponibles con su descripción y parte del cuerpo asociada.
ejercicioparterealizado	Relación entre los ejercicios realizados y la parte del cuerpo trabajada.
ejerciciorealizado	Historial de ejercicios completados con sus repeticiones.
objetivosusuario	Metas personales establecidas por el usuario.
partetrabajocuerpo	Identificación de las áreas del cuerpo asociadas a cada ejercicio.
user	Tabla de autenticación (correo y contraseña) administrada por Supabase.
usuarioavances	Registros de progreso general, peso y mediciones actualizadas.
Flujo de uso

El usuario inicia sesión con su correo y contraseña.

Accede al panel principal con los cuatro módulos.

Desde el módulo Ejercicios, selecciona una rutina o ejercicio individual.

La cámara del dispositivo detecta sus movimientos mediante MediaPipe.

Los resultados (repeticiones y tiempo) se registran automáticamente.

El módulo Estadísticas refleja los avances y permite actualizar datos personales.

El módulo Alimentos muestra recomendaciones y permite registrar el consumo diario.

En Configuración, el usuario puede cerrar sesión o consultar información general.

Requisitos del sistema

Android Studio (versión mínima recomendada: Arctic Fox o superior)

SDK Android 8.0 (Oreo) o superior

Kotlin y Java instalados

Conexión a internet activa

Cuenta de Supabase configurada para autenticación y almacenamiento

Instalación

Clonar el repositorio:

git clone https://github.com/tu_usuario/nombre_repositorio.git


Abrir el proyecto en Android Studio.

Configurar las credenciales de Supabase en el archivo de conexión.

Sincronizar dependencias y ejecutar la aplicación en un dispositivo o emulador.

Estado del proyecto

Actualmente el proyecto se encuentra en su fase final del desarrollo

Credenciales para la base de datos en Supabase:



Créditos

Autor: Diego Olvera Gómez
Periodo: Octubre – Diciembre 2025
Proyecto de residencias profesionales — Centro de Cooperación Academia Industria (CCAI)
