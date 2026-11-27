pipeline {
  agent any

  tools {
    maven "apache-maven-latest"
    jdk "temurin-jdk21-latest"
  }

  stages {
    stage("Install") {
      steps {
        withMaven() {
          sh "mvn -Pstaging clean install"
        }
      }
    }

    stage("Tests") {
      steps {
        withMaven() {
          sh "mvn -Pstaging test"
        }
      }
    }

    stage("Integration-Test") {
        steps {
            withMaven() {
              sh "wget https://download.eclipse.org/ee4j/glassfish/glassfish-8.0.5.zip"
              sh "unzip glassfish-8.0.5.zip"
              sh "glassfish8/bin/asadmin start-domain"

              sh "mvn -Pstaging,testsuite-glassfish verify"

              sh "glassfish8/bin/asadmin stop-domain"
            }
        }
    }
 }
}
