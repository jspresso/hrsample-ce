import org.jspresso.contrib.sjs.domain.Domain;
import org.jspresso.contrib.sjs.front.Front;

def domainBuilder = new Domain()

domainBuilder.Domain(projectName:'hrsample', mute:true) {
  namespace('org.jspresso.hrsample') {
    include(project.properties['srcDir']+'/model.groovy')
  }
}
if(!domainBuilder.isOK()) {
  println domainBuilder.getErrorDomain()
  fail('SJS defined domain is invalid.')
}

def frontendBuilder = new Front(domainBuilder.getReferenceDomain())
frontendBuilder.Front(){
  namespace('org.jspresso.hrsample'){
    include(project.properties['srcDir']+'/view.groovy')
    include(project.properties['srcDir']+'/frontend.groovy')
  }
}
if(frontendBuilder.getNbrError() != 0) {
  println frontendBuilder.getError()
  fail('SJS defined frontend / views is invalid.')
}
//println domainBuilder.getResultDomain()
//println frontendBuilder.getResultView()
//println frontendBuilder.getResultFront()

domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['modelOutputFileName'])
frontendBuilder.writeViewFile(project.properties['outputDir'],project.properties['viewOutputFileName'])
frontendBuilder.writeFrontendFile(project.properties['outputDir'],project.properties['frontOutputFileName'])
