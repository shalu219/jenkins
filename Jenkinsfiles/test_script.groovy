#!/usr/local/bin/groovy
import static groovy.io.FileType.FILES
Environment = "PROD"

def itList  = "/Users/shalu/rpm/liquimatch-it-repo/noarch"
def uatList  = "/Users/shalu/rpm/liquimatch-it-repo/noarch"
def prodList  = "/Users/shalu/rpm/liquimatch-uat-repo/noarch"
List default_item = ["None"]

println (Environment);
//println (itList);

//def dir = new File("${itList}");
//def files = [];
//dir.traverse(type: FILES, maxDepth: 0) { files.add(it) };

//files.each {
//  println it.path
//}

def dir = []
def files = []

if (Environment == 'IT') {
  dir = new File("${itList}");
} else if (Environment == 'UAT') {
  dir = new File("${uatList}");
} else if (Environment == 'PROD') {
  dir = new File("${prodList}");
} else {
  print (default_item);
}

dir.traverse(type: FILES, maxDepth: 0) { files.add(it) }
//println ("IT files::");
//println (itList);
files.each {
  println it.path
}

/* working fine 
def dir = new File(rootPath);
def files = [];
dir.eachFileRecurse(FILES) {
  if(it.name.endsWith('.noarch.rpm')) {
    //files << it.getName()
    files << it
  }
}
def result = files.sort{ a,b -> b.lastModified() <=> a.lastModified() }*.name
//return files.reverse();
//return files
print (result);
*/