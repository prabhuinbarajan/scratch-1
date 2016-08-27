#!/bin/bash
docker build -t gcr.io/qubeship/jenkins-k8s-slave -f Dockerfile-jenkinsslave  .
#gcloud docker push gcr.io/qubeship/jenkins-k8s-slave
#gcr.io/cloud-solutions-images/jenkins-k8s-slave
