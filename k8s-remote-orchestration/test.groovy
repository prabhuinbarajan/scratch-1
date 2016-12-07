podTemplate(label: 'docker', containers: [containerTemplate(image: 'jenkinsci/jnlp-slave')], cloud: 'minikube') {
    podTemplate( label: 'slave', containers: [
        containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'golang', image: 'golang:1.6.3-alpine', ttyEnabled: true, command: 'cat')
      ],
      volumes: [secretVolume(secretName: 'shared-secret', mountPath: '/etc/shared-secrets')],
      cloud: 'minikube') {
    
        node ('mypod') {
            stage 'Get a Maven project'
            git 'https://github.com/jenkinsci/kubernetes-plugin.git'
            container('maven') {
                stage 'Build a Maven project'
                sh 'mvn clean install'
            }
    
            stage 'Get a Golang project'
            git url: 'https://github.com/hashicorp/terraform.git'
            container('golang') {
                stage 'Build a Go project'
                sh """
                mkdir -p /go/src/github.com/hashicorp
                ln -s `pwd` /go/src/github.com/hashicorp/terraform
                cd /go/src/github.com/hashicorp/terraform && make core-dev
                """
            }
    
        }
    }
}


podTemplate(
     cloud: 'minikube',
     label: 'docker',
     containers: [
        containerTemplate(name: 'jnlp', image: 'jenkinsci/jnlp-slave:2.62-alpine', args: '${computer.jnlpmac} ${computer.name}'),
        containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'golang', image: 'golang:1.6.3-alpine', ttyEnabled: true, command: 'cat')
      ],
      volumes: [secretVolume(secretName: 'shared-secret', mountPath: '/etc/shared-secrets')],
      ) {
    
        node ('docker') {
            stage 'Get a Maven project'
            git 'https://github.com/jenkinsci/kubernetes-plugin.git'
            container('maven') {
                stage 'Build a Maven project'
                sh 'mvn clean install'
            }
    
            stage 'Get a Golang project'
            git url: 'https://github.com/hashicorp/terraform.git'
            container('golang') {
                stage 'Build a Go project'
                sh """
                mkdir -p /go/src/github.com/hashicorp
                ln -s `pwd` /go/src/github.com/hashicorp/terraform
                cd /go/src/github.com/hashicorp/terraform && make core-dev
                """
            }
    
        }
    
}



podTemplate(cloud:'minikube', label: 'mypod', containers: [
    containerTemplate(name: 'maven', image: 'jenkinsci/jnlp-slave', ttyEnabled: true, command: 'java', args: ' -jar /usr/share/jenkins/slave.jar  -jnlpUrl ${JENKINS_JNLP_URL} -secret ${JENKINS_SECRET}')
  ],
  volumes: [secretVolume(secretName: 'shared-secret', mountPath: '/etc/shared-secrets')]) {

    node ('mypod') {
        stage 'Get a Maven project'
        git 'https://github.com/jenkinsci/kubernetes-plugin.git'

        stage 'Build a Maven project'
        sh 'mvn clean install'

    }
}




HOSTNAME=kubernetes-58b97d0e2a9948ed89e11a6080775166-1e02fcbdd50b
KUBERNETES_PORT_443_TCP_PORT=443
KUBERNETES_PORT=tcp://10.0.0.1:443
TERM=xterm
MY_NGINX_SERVICE_PORT=tcp://10.0.0.91:80
KUBERNETES_SERVICE_PORT=443
KUBERNETES_SERVICE_HOST=10.0.0.1
CA_CERTIFICATES_JAVA_VERSION=20140324
MY_NGINX_SERVICE_PORT_80_TCP_PORT=80
JENKINS_NAME=kubernetes-58b97d0e2a9948ed89e11a6080775166-1e02fcbdd50b
MY_NGINX_SERVICE_PORT_80_TCP_PROTO=tcp
COLUMNS=277
MY_NGINX_SERVICE_SERVICE_PORT_PORT0=80
MY_NGINX_SERVICE_SERVICE_HOST=10.0.0.91
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
PWD=/home/jenkins
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
JENKINS_JNLP_URL=http://192.168.99.100:8080/computer/kubernetes-58b97d0e2a9948ed89e11a6080775166-1e02fcbdd50b/slave-agent.jnlp
LANG=C.UTF-8
JAVA_VERSION=8u102
MY_NGINX_SERVICE_SERVICE_PORT=80
JENKINS_URL=http://192.168.99.100:8080/
LINES=55
JENKINS_LOCATION_URL=http://192.168.99.100:8080/
SHLVL=1
JAVA_DEBIAN_VERSION=8u102-b14.1-1~bpo8+1
HOME=/home/jenkins
JENKINS_SECRET=8ef02eec7472b1a59ac7e5a33f85e94e05b4838f97d5561b0a044c0a7fd263d9
KUBERNETES_PORT_443_TCP_PROTO=tcp
KUBERNETES_SERVICE_PORT_HTTPS=443
MY_NGINX_SERVICE_PORT_80_TCP_ADDR=10.0.0.91
KUBERNETES_PORT_443_TCP_ADDR=10.0.0.1
MY_NGINX_SERVICE_PORT_80_TCP=tcp://10.0.0.91:80
KUBERNETES_PORT_443_TCP=tcp://10.0.0.1:443