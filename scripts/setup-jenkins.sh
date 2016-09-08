#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}/..

NS=platform

kubectl create ns ${NS}

gcloud compute images create jenkins-home-image --source-uri https://storage.googleapis.com/solutions-public-assets/jenkins-cd/jenkins-home.tar.gz
gcloud compute disks create platform-jenkins-home --image jenkins-home-image

PASSWORD=`openssl rand -base64 15`; echo "Your password is $PASSWORD"; sed -i.bak s#CHANGE_ME#$PASSWORD# jenkins/k8s/options

kubectl create secret generic jenkins --from-file=jenkins/k8s/options --namespace=${NS}

kubectl apply -f jenkins/k8s/