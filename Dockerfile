# ETAP 1: Budowanie aplikacji (Maven)
# Używamy obrazu z Mavenem i Javą 21, żeby zbudować plik .jar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Budujemy aplikację, pomijając testy (dla szybszego startu w Dockerze)
RUN mvn clean package -DskipTests

# ETAP 2: Uruchomienie (Czysta Java)
# Używamy lekkiego obrazu JRE tylko do uruchomienia
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Kopiujemy zbudowany plik .jar z Etapu 1
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]