pipeline {
  agent any

  tools {
    maven "apache-maven-latest"
    jdk "temurin-latest"
  }

  stages {
    stage("Compile") {
      steps {
        withMaven() {
          sh "mvn -Pstaging clean compile"
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
              sh "mvn -Pstaging,testsuite-glassfish verify"
            }
        }
    }
 }
}
