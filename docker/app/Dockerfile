FROM maven:3.5-jdk-8-alpine as build
WORKDIR /esci/build
COPY . /esci/build
ARG PROJECT_VERSION

RUN echo "Project version 1: $PROJECT_VERSION"

RUN mvn dependency:go-offline -DprojectVersion=${PROJECT_VERSION} install

FROM openjdk:8-jre-alpine
WORKDIR /esci/bin
ARG PROJECT_VERSION
ENV PROJECT_VERSION=$PROJECT_VERSION

RUN echo "Project version 2: $PROJECT_VERSION"

COPY --from=build /esci/build/target/EthereumSmartContractIdentifier-${PROJECT_VERSION}.jar /esci/bin
CMD java -jar EthereumSmartContractIdentifier-${PROJECT_VERSION}.jar