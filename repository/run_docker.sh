#!/bin/bash

# builds a jar with the dependencies in there
gradle clean
gradle clean build jar -x test
gradle bootRepackage

docker-compose up -d
