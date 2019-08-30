FROM maven:3.6.1-jdk-11 as build
WORKDIR /esci/build
COPY . /esci/build
ARG PROJECT_VERSION

RUN echo "Project version: $PROJECT_VERSION"

RUN mvn dependency:go-offline -DprojectVersion=${PROJECT_VERSION} install -DskipTest=true -Dmaven.javadoc.skip-true -B

FROM openjdk:11-jre
WORKDIR /esci/bin
ARG PROJECT_VERSION
ENV PROJECT_VERSION=$PROJECT_VERSION


COPY --from=build /esci/build/target/EthereumSmartContractIdentifier-${PROJECT_VERSION}.jar /esci/bin
CMD java -jar EthereumSmartContractIdentifier-${PROJECT_VERSION}.jar