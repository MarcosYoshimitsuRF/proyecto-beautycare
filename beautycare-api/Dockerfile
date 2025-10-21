# --- Capa 1: Construcción (Build) ---
# Usamos una imagen oficial de Eclipse Temurin (JDK 21) con Maven
FROM eclipse-temurin:21-jdk-jammy as builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de definición del proyecto (pom.xml)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargamos todas las dependencias de Maven.
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente de la aplicación
COPY src ./src

# --- INICIO DE LA CORRECCIÓN ---
# Elimina el application.properties ANTES de que Maven intente leerlo.
# Esto previene el MalformedInputException.
# La configuración se inyectará vía variables de entorno en docker-compose.
RUN rm src/main/resources/application.properties
# --- FIN DE LA CORRECCIÓN ---

# Compilamos la aplicación y generamos el .jar
RUN ./mvnw clean install -DskipTests


# --- Capa 2: Ejecución (Runtime) ---
# Usamos una imagen base mucho más ligera (solo JRE)
FROM eclipse-temurin:21-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el .jar compilado desde la capa 'builder'
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# El comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]