FROM openjdk:15
WORKDIR /IdPrefixFilter
COPY build/install/IdPrefixFilter/ .
ENTRYPOINT ["./bin/IdPrefixFilter"]

COPY build/resources/main/ .
ENV com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName=./numericPrefixes.yaml