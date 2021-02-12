# Build, then run with argument 1.2.3.4.5 to test for return code 2
#
# See onlyCommandLineSpecifiesPrefixes.dockerfile and its comments
# for a contrast.
#
FROM openjdk:15
WORKDIR /IdPrefixFilter
COPY build/install/IdPrefixFilter/ .
ENTRYPOINT ["./bin/IdPrefixFilter"]

COPY build/resources/main/ .
#ENV com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName=./numericPrefixes.yaml
#Return code 2 for YamlPrefixesFileNameUndefined due to the above commented out