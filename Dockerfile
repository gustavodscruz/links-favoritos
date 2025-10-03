FROM openjdk:21-jdk-slim

WORKDIR /app

# Copia apenas pom.xml primeiro para cache das dependências
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Baixa dependências (fica em cache se pom.xml não mudar)
RUN ./mvnw dependency:go-offline -B

# Copia código fonte
COPY src ./src

# Expõe porta
EXPOSE 8080

# Comando para desenvolvimento com hot reload
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]