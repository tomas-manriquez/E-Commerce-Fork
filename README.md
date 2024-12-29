# E-Commerce-Grupo-1
* **Asignatura:** Taller de base de datos
* **Semestre y año**: 2-2024
* **Universidad Santiago de Chile**

## Requisitos previos para Backend

- Tener instalado **PostgreSQL**.
- Tener acceso al usuario de PostgreSQL con permisos suficientes para crear bases de datos y tablas.
- **Maven** instalado en tu máquina. Si no tienes Maven, sigue [este enlace](https://maven.apache.org/install.html) para instalarlo.
- Tener instalado **Stackbuilder** con la extensión de **postgis** en la versión 3.3.5.
- Tener acceso al archivo `.env` que contiene las variables de configuración para la base de datos.

Las variables de entorno necesarias son:
- `DB_HOST`: Dirección del servidor de la base de datos (por defecto, `localhost`).
- `DB_PORT`: Puerto del servidor de la base de datos de Postgres (por defecto, `5432`).
- `DB_MONGOPORT`: Puerto del servidor de la base de datos de MongoDB (por defecto, `27017`).
- `DB_NAME`: Nombre de la base de datos a crear.
- `DB_USERNAME`: Nombre de usuario para acceder a la base de datos.
- `DB_PASSWORD`: Contraseña del usuario de la base de datos.

## Preparación del entorno

1. **Clona el repositorio y navega al directorio del proyecto:**

   ```bash
   git clone <url-del-repositorio>
   cd <directorio-del-repositorio>
   ```
2. Si no está instalado, instala el driver de MongoDB para Node.js

   ```bash
   npm install mongodb
   ```

2. **Crea el archivo `.env`** en la raíz del proyecto y define las variables de entorno necesarias. Un ejemplo de archivo `.env` podría ser:

   ```bash
   DB_HOST=localhost
   DB_PORT=5432
   DB_MONGOPORT=27017
   DB_NAME=ecommerce
   DB_USERNAME=postgres
   DB_PASSWORD=admin
   ```
3. **Ejecución del script para la creación de la base de datos**
* ### Ejecución en Windows
* Abre PowerShell o el terminal de tu preferencia dentro de la carpeta 'db'. (Puedes acceder a esta abriendo un Powershell o terminal en el directorio raiz y ejecutar el comando `cd src\main\resources\db`)
* Ejecuta el siguiente script en PowerShell para reemplazar las variables de entorno en los archivos SQL y ejecutar los scripts:
* ```bash
    $envVars = Get-Content (Join-Path (Resolve-Path "..\..\..\..") ".env") | ForEach-Object {
      $key, $value = $_ -split '='
      if ($key -eq "DB_USERNAME" -or $key -eq "DB_NAME" -or $key -eq "DB_PASSWORD" -or $key -eq "DB_HOST" -or $key -eq "DB_PORT" -or $key -eq "DB_MONGOPORT") {
      [System.Environment]::SetEnvironmentVariable($key, $value, [System.EnvironmentVariableTarget]::Process)
      }
    }
    function Replace-EnvVarsInFile {
    param (
    [string]$filePath
    )
    (Get-Content $filePath) | ForEach-Object {
        $_ -replace '\${DB_USERNAME}', $env:DB_USERNAME `
        -replace '\${DB_NAME}', $env:DB_NAME `
        -replace '\${DB_PASSWORD}', $env:DB_PASSWORD `
        -replace '\${DB_HOST}', $env:DB_HOST `
        -replace '\${DB_PORT}', $env:DB_PORT `
        -replace '\${DB_MONGOPORT}', $env:DB_MONGOPORT
    } | Set-Content $filePath
    }
    Replace-EnvVarsInFile "createDB.sql"
    Replace-EnvVarsInFile "triggers.sql"
    Replace-EnvVarsInFile "populateDB.sql"
    Replace-EnvVarsInFile "storedProcedures.sql"
    Replace-EnvVarsInFile "initDB.sql"
    Replace-EnvVarsInFile "populateMongoDB.js"

    $env:PGPASSWORD = $env:DB_PASSWORD
    psql -h $env:DB_HOST -U $env:DB_USERNAME -d postgres -c "CREATE DATABASE $env:DB_NAME;"
    psql -h $env:DB_HOST -U $env:DB_USERNAME -d $env:DB_NAME -f initDB.sql
    node populateDatabase.js 
  ```
* ### Ejecución en Linux/MacOS
* Abre el terminal dentro de la carpeta 'db'. (Puedes acceder a esta abriendo terminal en el directorio raiz y ejecutar el comando `cd src\main\resources\db`)
* ```bash
  export $(cat .env | xargs) && envsubst < initDB.sql | psql -h $DB_HOST -p $DB_PORT -U $DB_USERNAME -d $DB_NAME
  ```
# Ejecución del Backend con Maven
1. **Compilar y ejecutar el backend**

Una vez que hayas configurado la base de datos, puedes proceder a ejecutar el backend con Maven.

* Abre una terminal en la raíz del proyecto.

* Ejecuta el siguiente comando para compilar y ejecutar el backend:

   ```bash
   mvn clean install
   ```
Este comando descargará las dependencias y compilará el proyecto. Después, para ejecutar la aplicación:
   
   ```bash
   mvn spring-boot:run
   ```
Esto iniciará el backend y podrás acceder a la aplicación en la URL configurada.

## Configuración FrontEnd
Dirigirse a la carpeta 'ecommerce-frontend' y revisar el archivo `README.md` para su configuración.
