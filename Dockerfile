# Build Stage
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM tomcat:9.0-jdk17-openjdk-slim
WORKDIR /usr/local/tomcat

# Remove default webapps
RUN rm -rf webapps/*

# Copy packaged WAR to webapps as ROOT.war so context path is /
COPY --from=builder /app/target/gokulmart.war webapps/ROOT.war

# Configure dynamic PORT binding for Render deployment
EXPOSE 8080

CMD ["sh", "-c", "sed -i 's/port=\"8080\"/port=\"'\"${PORT:-8080}\"'\"/g' conf/server.xml && catalina.sh run"]
