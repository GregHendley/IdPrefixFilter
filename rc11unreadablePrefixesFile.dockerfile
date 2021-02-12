# Build and run this to test for return code 11
#
FROM openjdk:15
WORKDIR /IdPrefixFilter
COPY build/install/IdPrefixFilter/ .
ENTRYPOINT ["./bin/IdPrefixFilter"]

#Setup default prefix files
COPY build/resources/main/ .
ENV com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName=./numericPrefixes.yaml

#Setup test for IOException denied / return code 11
RUN ["chmod", "000", "numericPrefixes.yaml"]
RUN ["useradd", "filterRunner"]
RUN ["chown", "-R", "filterRunner", "./bin/", "./lib/" ]
USER filterRunner
