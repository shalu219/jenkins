// Using Active Choices with Scriptler scripts (https://plugins.jenkins.io/uno-choice/)
//Scriptler Plug-in
Example
Environments.groovy in Scriptler (Jenkins > Scriptler)

return ["Select:selected", "DEV", "TEST", "STAGE", "PROD"]
HostsInEnv.groovy in Scriptler (Jenkins > Scriptler)

// Static content examples. These lists can be generated dynamically as an alternative.
List devList  = ["Select:selected", "dev1", "dev2"]
List testList  = ["Select:selected", "test1", "test2", "test3"]
List stageList = ["Select:selected", "stage1"]
List prodList  = ["Select:selected", "prod1", "prod2", "prod3", "prod4"]

List default_item = ["None"]

if (Environment == 'DEV') {
  return devList
} else if (Environment == 'TEST') {
  return testList
} else if (Environment == 'STAGE') {
  return stageList
} else if (Environment == 'PROD') {
  return prodList
} else {
  return default_item
}



Pipeline in Jenkinsfile

properties([
  parameters([
    [
      $class: 'ChoiceParameter',
      choiceType: 'PT_SINGLE_SELECT',
      name: 'Environment',
      script: [$class: 'GroovyScript', 
        fallbackScript: [classpath: [], sandbox: false, script: 'return[\'error\']'], 
        script: [classpath: [], sandbox: false, script: 'return ["IT", "UAT", "PROD"]']]
    ],
    [
      $class: 'CascadeChoiceParameter',
      choiceType: 'PT_SINGLE_SELECT',
      name: 'RPMpath',
      referencedParameters: 'Environment',
      script: [
        $class: 'GroovyScript', 
        fallbackScript: [classpath: [], sandbox: false, script: 'return[\'error\']'], 
        script: [classpath: [], sandbox: false, 
        script: """
        xx = ["/Users/shalu/rpm/liquimatch-${Environment}-repo/noarch"]
        return xx
        """]]
      ],
      [
        $class: 'FileSystemListParameterDefinition', 
        description: '', 
        name: 'version', 
        nodeName: 'master', 
        path: '/Users/shalu/rpm/liquimatch-development-repo/noarch', 
        regexExcludePattern: '', 
        regexIncludePattern: '', 
        selectedType: 'FILE', 
        sortByLastModified: false, 
        sortReverseOrder: true
      ],
      [
        $class: 'CascadeChoiceParameter',
        choiceType: 'PT_SINGLE_SELECT',
        name: 'version',
        referencedParameters: 'Host',
        script: [
          $class: 'FileSystemListParameterDefinition', 
          description: '', 
          path: '$Host', 
          regexExcludePattern: '', 
          regexIncludePattern: '', 
          selectedType: 'FILE', 
          sortByLastModified: false, 
          sortReverseOrder: true
        ]
      ]
   ]
 ])
])

pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo "<<${params.Environment}>>"
        echo "<<${params.Host}>>"
      }
    }
  }
}

