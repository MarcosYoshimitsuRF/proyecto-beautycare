# --- Capa 1: Construcción (Build) ---
# Usamos una imagen oficial de Eclipse Temurin (JDK 21) con Maven
# para compilar nuestro proyecto.
FROM eclipse-temurin:21-jdk-jammy as builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de definición del proyecto (pom.xml)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargamos todas las dependencias de Maven.
# Esto se hace en un paso separado para aprovechar el caché de Docker.
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente de la aplicación
COPY src ./src

# Compilamos la aplicación y generamos el .jar
# Usamos -DskipTests para una construcción más rápida en el pipeline de Docker.
RUN ./mvnw clean install -DskipTests


# --- Capa 2: Ejecución (Runtime) ---
# Usamos una imagen base mucho más ligera (solo JRE) para ejecutar la aplicación,
# lo que reduce el tamaño final de la imagen y la superficie de ataque.
FROM eclipse-temurin:21-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el .jar compilado desde la capa 'builder'
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto 8081 (definido en docker-compose.yml y properties)
EXPOSE 8081

# El comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]