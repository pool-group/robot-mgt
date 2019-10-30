node {
   def mvnHome
   stage('Preparation') { // for display purposes
      git branch: 'master', url: 'git@gitlab.tiwo.io:backend-card-games/robot-mgt.git'
      mvnHome = tool 'M3'
   }
   stage('Build') {
      if (isUnix()) {
         sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore=false clean package -U"
      } else {
         bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore=false clean package -U/)
      }
   }
   stage('Results') {
      junit '**/target/surefire-reports/TEST-*.xml'
      archive 'target/*.jar'
   }
   stage('Deploy Snapshot') {
      if (isUnix()) {
         sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore=false clean deploy"
      } else {
         bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore=false clean deploy/)
      }
    }
 //  stage('SonarQube analysis') {
 //      withSonarQubeEnv('SonarQube') {
 //        sh "/home/jenkins/sonar-scanner-3.0.0.702/bin/sonar-scanner"
 //      }
 //   }
}

