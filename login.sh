#!/bin/bash
eval $(docker-machine env default)
docker login -e $(gcloud auth list | grep ACTIVE | grep qubeship.io | awk '{print $2}')  -u _token -p "$(gcloud auth print-access-token)" https://gcr.io

