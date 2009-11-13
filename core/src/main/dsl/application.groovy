import org.jspresso.contrib.sjs.domain.Domain;
import org.jspresso.contrib.sjs.front.Front;

def domainBuilder = new Domain()

domainBuilder.Project('hrsample', mute:true) {
  namespace('org.jspresso.hrsample') {
    Domain {
      include('src/main/dsl/model.groovy')
    }
  }
}
if(!domainBuilder.isOK()) return -1;

def frontendBuilder = new Front(domainBuilder.getReferenceDomain())
frontendBuilder.Front(){
  namespace('org.jspresso.hrsample'){
    include('src/main/dsl/view.groovy')
    include('src/main/dsl/frontend.groovy')
  }
}
if(frontendBuilder.getNbrError() != 0) {
  return -1;
  println frontendBuilder.getError()
}
//println domainBuilder.getResultDomain()
//println frontendBuilder.getResultView()
//println frontendBuilder.getResultFront()

domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['modelOutputFileName'])
frontendBuilder.writeViewFile(project.properties['outputDir'],project.properties['viewOutputFileName'])
frontendBuilder.writeFrontEndFile(project.properties['outputDir'],project.properties['frontOutputFileName'])
