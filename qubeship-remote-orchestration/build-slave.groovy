podTemplate(
     cloud: 'minikube',
     label: 'docker',
     containers: [
        //containerTemplate(name: 'jnlp', image: 'jenkinsci/jnlp-slave:2.62-alpine', args: '${computer.jnlpmac} ${computer.name}'),
        containerTemplate(name: 'docker', image: 'docker:1.11', ttyEnabled: true, command: 'cat'),
      ],
      volumes: [secretVolume(secretName: 'shared-secret', mountPath: '/etc/shared-secrets'), hostPathVolume(hostPath:'/var/run/docker.sock',mountPath:'/var/run/docker.sock') ],
      ) {
        node {
          checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github', url: 'https://github.com/Qubeship/qube-hybrid-agent']]])
          input 'ready?'
          container(name:'docker', cloud:'minikube') {
              def img = docker.image('qubeship/api-auth:latest')
              docker.withRegistry('https://gcr.io/', 'gcr:qubeship') {
                img.pull()
              }
          }
        }
        

}