node('k8s'){
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github', url: 'https://github.com/Qubeship/qube-hybrid-agent']]])
    input 'ready?'
    def img = docker.image('qubeship/api-auth:latest')
        docker.withRegistry('https://gcr.io/', 'gcr:qubeship') {
        img.pull()
    }
}