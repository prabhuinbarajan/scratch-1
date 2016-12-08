#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR/

# Get the Kernel Name
install_minikube=0
test_minikube=0
install_kubectl=0
export_certs=0
while [ $# -gt 0 ]; do
  case "$1" in
    --install-minikube) 
	  install_minikube=1
      ;;
    --test-minikube) 
	  test_minikube=1
      ;;
    --install-kubectl)
      install_kubectl=1
      ;;
    --export-certs)
	  export_certs=1
	  ;;
    *)
      printf "***************************\n"
      printf "* Error: Invalid argument.*\n"
      printf "***************************\n"
      exit 1
  esac
  shift
done


Kernel=$(uname -s)
case "$Kernel" in
    Linux)  Kernel="linux"              ;;
    Darwin) Kernel="darwin"                ;;
#    FreeBSD)    Kernel="freebsd"            ;;
* ) echo "Your Operating System -> ITS NOT SUPPORTED"   ;;
esac

#echo
#echo "Operating System Kernel : $Kernel"
#echo
# Get the machine Architecture
Architecture=$(uname -m)
case "$Architecture" in
  #  x86)    Architecture="x86"                  ;;
 #   ia64)   Architecture="ia64"                 ;;
  #  i?86)   Architecture="x86"                  ;;
    amd64)  Architecture="amd64"                    ;;
    x86_64) Architecture="amd64"                   ;;
#    sparc64)    Architecture="sparc64"                  ;;
* ) echo    "Your Architecture '$Architecture' -> ITS NOT SUPPORTED."   ;;
esac

osarch="$Kernel/$Architecture"
echo $osarch

if [ $osarch == "darwin/amd64" ]; then
	if [ $install_minikube == 1 ]; then
		curl -Lo minikube https://storage.googleapis.com/minikube/releases/v0.13.1/minikube-darwin-amd64 && \
   		chmod +x minikube && \
   		mv minikube /usr/local/bin/
	fi
	if [ $install_kubectl == 1 ]; then	
   		curl -Lo kubectl https://storage.googleapis.com/kubernetes-release/release/v1.3.0/bin/darwin/amd64/kubectl && \
   		chmod +x kubectl && \
   		mv kubectl /usr/local/bin/
  fi
elif [ $osarch == "linux/amd64"]; then
	if [ $install_minikube == 1 ]; then
		curl -Lo minikube https://storage.googleapis.com/minikube/releases/v0.13.1/minikube-linux-amd64 && \
   		chmod +x minikube && \ 
   		sudo mv minikube /usr/local/bin/
	fi
	if [ $install_kubectl == 1 ]; then	
	   	curl -Lo kubectl https://storage.googleapis.com/kubernetes-release/release/v1.3.0/bin/linux/amd64/kubectl && \
   		chmod +x kubectl && \ 
   		sudo mv kubectl /usr/local/bin/
  fi
fi

if [ $install_minikube == 1 ]; then
	minikube start
fi

if [ $test_minikube == 1 ]; then
	echo "launching dashboard : $(minikube ip)"
	minikube dashboard
	minikube docker-env
	kubectl create namespace test
	kubectl create -f my-nginx-deployment.yaml --record
	kubectl create -f my-nginx-service.yaml --record
	echo "minikube runs $(minikube service -n test my-nginx-service --url)"
	open $(minikube service -n test my-nginx-service --url)

  kubectl create secret generic shared-secret --from-literal=key1=supersecret --from-literal=key2=topsecret --namespace test
fi

if [ $export_certs == 1 ]; then
  echo "exporting certs"
  openssl pkcs12 -export -out ~/.minikube/minikube.pfx -inkey ~/.minikube/apiserver.key -in ~/.minikube/apiserver.crt -certfile ~/.minikube/ca.crt -passout pass:secret
  #validating kubernetes
  echo "testing kubernetes api with certs curl --cacert ~/.minikube/ca.crt --cert ~/.minikube/minikube.pfx:secret https://$(minikube ip):8443"
  curl --cacert ~/.minikube/ca.crt --cert ~/.minikube/minikube.pfx:secret https://$(minikube ip):8443
fi
