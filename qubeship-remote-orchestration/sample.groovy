/*node {
    sh 'env'
}*/
podTemplate(
     cloud: 'minikube',
     label: 'docker',
     containers: [
        //containerTemplate(name: 'jnlp', image: 'jenkinsci/jnlp-slave:2.62-alpine', args: '${computer.jnlpmac} ${computer.name}'),
        containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'golang', image: 'golang:1.6.3-alpine', ttyEnabled: true, command: 'cat')
      ],
      volumes: [secretVolume(secretName: 'shared-secret', mountPath: '/etc/shared-secrets')],
      ) {
        node ('docker') {
            sh 'env'
            stage 'Get a Maven project'
            //git 'https://github.com/jenkinsci/kubernetes-plugin.git'
            git 'https://github.com/pgrimard/spring-boot-hello-world.git'
            container(name:'maven', cloud:'minikube') {
                stage 'Build a Maven project'
                sh 'echo "hello world"'
                //sleep 60
                try {
                sh 'mvn clean install'
                }catch (Exception ex) {
                    ex.printStackTrace()
                }
            }
    
            stage 'Get a Golang project'
            git url: 'https://github.com/golang/example.git'
            container(name:'golang', cloud:'minikube') {
                stage 'Build a Go project'
                sh """
                mkdir -p /go/src/github.com/golang/
                ln -s `pwd` /go/src/github.com/golang/example
                export GOPATH=/go
                cd /go/src/github.com/golang/example/hello && go build
                """
            }
    
        }
    
}


