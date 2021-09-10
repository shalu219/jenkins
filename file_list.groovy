import groovy.io.*

def listfiles(dir) {
	dlist = []
	flist = []
	new File(dir).eachDir {dlist << it.name }
	dlist.sort()
	new File(dir).eachFile(FileType.FILES, {flist << it.name })
	flist.sort()
	return (dlist << flist).flatten()
}

fs = listfiles(".")
fs.each {
	println it
}

//LIST FILES IN A DIRECTORY
import static groovy.io.FileType.FILES
def dir = new File(".");
def files = [];
dir.traverse(type: FILES, maxDepth: 0) { files.add(it) };

///LIST FILES IN A DIRECTORY (without fullPath)
import static groovy.io.FileType.FILES
def rootPath = "/Users/shalu/rpm/liquimatch-uat-repo/noarch"
def dir = new File(rootPath);
def files = [];
dir.eachFileRecurse(FILES) {
  if(it.name.endsWith('.rpm')) {
    files << it.getName()
  }
}
return files;

//WORKING
import static groovy.io.FileType.FILES

List itList  = ["/Users/shalu/rpm/liquimatch-it-repo/noarch"]
List uatList  = ["/Users/shalu/rpm/liquimatch-it-repo/noarch"]
List prodList  = ["/Users/shalu/rpm/liquimatch-uat-repo/noarch"]

List default_item = ["None"]

if (Environment == 'IT') {
  return itList
} else if (Environment == 'UAT') {
  return uatList
} else if (Environment == 'PROD') {
  return prodList
} else {
  return default_item
}


//WORKING
import static groovy.io.FileType.FILES
def itDeploy  = "/Users/shalu/rpm/liquimatch-it-repo/noarch"
def uatDeploy  = "/Users/shalu/rpm/liquimatch-uat-repo/noarch"
def prodDeploy  = "/Users/shalu/rpm/liquimatch-prod-repo/noarch"
def uatPromote  = "/Users/shalu/rpm/liquimatch-it-repo/noarch"
def prodPromote  = "/Users/shalu/rpm/liquimatch-uat-repo/noarch"
def rootPath = ""

List default_item = ["None"]

if (EnvironmentAction.equals("IT_DEPLOY")){
  rootPath = itDeploy
} else if (EnvironmentAction.equals("UAT_DEPLOY")){
  rootPath = uatDeploy
} else if (EnvironmentAction.equals("PROD_DEPLOY")){
  rootPath = prodDeploy
} else if (EnvironmentAction.equals("UAT_PROMOTE")){
  rootPath = uatPromote
} else if (EnvironmentAction.equals("PROD_PROMOTE")){
  rootPath = prodPromote
} else {
  return default_item
}

def dir = new File(rootPath);
def files = [];
dir.eachFileRecurse(FILES) {
  if(it.name.endsWith('.rpm')) {
    files << it.getName()
  }
}
return files.reverse();





