## instructions setting up cloud environment
download gcloud SDK
https://cloud.google.com/sdk/docs/quickstart-mac-os-x

untar the package and run install.sh
run gcloud init 
region us-west-1a


gcloud config set compute/zone us-west1-a
gcloud config list
gcloud components install kubectl
gcloud config set container/cluster qube-1	
gcloud container clusters get-credentials qube-1	
kubectl cluster-info
kubectl config view
kubectl version
kubectl get nodes

TODO: create provisioning script
