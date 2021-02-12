# Build, then run with command -y ./numericPrefixes.yaml 1.2.3.4.5
# to see a contrast with rc2YamlPrefixesFileNameUndefined.dockerfile
# This will have a return code of 0 and successfully match the ID
# with the specified prefixes file.
#
FROM openjdk:15
WORKDIR /IdPrefixFilter
COPY build/install/IdPrefixFilter/ .
ENTRYPOINT ["./bin/IdPrefixFilter"]

COPY build/resources/main/ .
#ENV com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName=./numericPrefixes.yaml
#Return code 2 for YamlPrefixesFileNameUndefined due to the above commented out
#except that the command recommended at the top of the file specifies
#the prefix file name. So there is no error.