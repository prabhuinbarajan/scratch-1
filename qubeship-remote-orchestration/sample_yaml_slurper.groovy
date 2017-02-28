import org.yaml.snakeyaml.Yaml


node{
    try{
      stage "checkout"
      commithash="master"
      git_repo="https://github.com/Qubeship/api_toolchains"
      checkout poll:false,scm: [
            $class: 'GitSCM',
            branches: [[name: "${commithash}"]],
            userRemoteConfigs: [[
              url: "${git_repo}",
              credentialsId: 'qubeship:production:c2bf50b6-6aaf-4acc-adaa-6ff5b67381c4',
              refspec: '+refs/heads/*:refs/remotes/origin/* +refs/pull/*:refs/remotes/origin/pr/*'
            ]]
        ]
      gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD', label:"Get Commit id")
    } catch (Exception ex) {
        throw new RuntimeException(ex)
        //ex.printStackTrace()
    }
    pwd=sh (returnStdOut:true, script:'pwd')
    sh (script: 'cat qube.yaml')
    println(env.WORKSPACE + '/qube.yaml');
    
    def config = getConfig(env.WORKSPACE + '/qube.yaml');
    println( config.toString() );
    println config.name
    input 'ready?'



    
    def img = docker.image('alpine:3.3');
    
    stage 'Mirror'
    // First make sure the slave has this image.
    // (If you could set your registry below to mirror Docker Hub,
    // img would be unnecessary as maven32.inside would pull the image.)
    img.pull()
    // We are pushing to a private secure docker registry in this demo.
    // 'docker-registry-login' is the username/password credentials ID
    // as defined in Jenkins Credentials.
    // This is used to authenticate the docker client to the registry.
    //dockerhub
    
    withDockerRegistry([credentialsId: 'qubeship:production:688eb302-d852-4773-a083-80ae695d1944', url: 'https://index.docker.io/v1/']) {
        img.tag("prabhuinbarajan/alpine:1.1", false , true)
        img.push ("prabhuinbarajan/alpine:1.1", false , true)
    }
    
    
}

@NonCPS
def getConfig(file) {
    File qubeYaml = new File(file);
    FileInputStream fin = new FileInputStream(qubeYaml);
    def yaml = new Yaml();
    def config = yaml.load(fin) ;
    return config
}
