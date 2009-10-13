import org.jspresso.contrib.sjs.domain.Domain;

def domainBuilder = new Domain()
domainBuilder.Project('hrsample') {
  namespace('org.jspresso.hrsample') {
    Domain{
//      Interface ('Nameable') {
//        properties { string_64 'name',mandatory:true }
//      }
//      
//      Interface('Traceable',
//        interceptors:'TraceableLifecycleInterceptor') {
//          properties {
//            date_time 'createTimestamp', readOnly:true
//            date_time 'lastUpdateTimestamp', readOnly:true
//          }
//          uncloned properties:['createTimestamp', 'lastUpdateTimestamp']
//          iconImage URL:'traceable-48x48.png'
//        }
//      
//      Entity('City',extend:'Nameable') {
//        properties { string_10 'zip' }
//        iconImage 'URL:city-48x48.png'
//      }
//      
//      Component('ContactInfo') {
//        properties {
//          string_256 'address'
//          reference  'city', ref:'City'
//          string_32  'phone'
//          string_128 'email',regex:"[\\w\\-\\.]*@[\\w\\-\\.]*", regexSample:'contact@acme.com'
//        }
//      }
//      
//      Entity('Event',extend:'Traceable'){
//        properties {
//          text 'text', maxLength:2048 , id:'Event-text'
//        }
//      }
//      
//      Entity ('Employee',extend:['Nameable','Traceable'],
//        extension :'EmployeeExtension',
//        //interceptors:'EmployeeLifecycleInterceptor',
//        processor:'EmployeePropertyProcessors',
//        services:[EmployeeService:'EmployeeServiceDelegate']){
//          properties{
//            string_32 'firstName',mandatory:true,processors:'FirstNameProcessor'
//            string_10 'ssn',regex:"[\\d]{10}", regexSample:'0123456789', unicityScope:'empSsn'
//            date 'birthDate',processors:'BirthDateProcessor'
//            date 'hireDate'
//            enumeration 'gender',enumName:'GENDER',maxLength:1,mandatory:true,valuesAndIconImageUrls:[
//              'M':'male-48x48.png',
//              'F':'female-48x48.png']
//            reference 'contact', ref:'ContactInfo',id:'contact'
//            color 'preferredColor'
//            bool 'married'
//            decimal 'salary', minValue:0, usingBigDecimal:true
//            list 'events', composition:true, ref:'Event'
//            set 'teams', ref:'Team' //,composition:true ,reverse:'Employee-teams'
//            reference 'company', ref:'Company', mandatory:true, reverse:'Company-employees'
//            reference 'managedOu', ref:'OrganizationalUnit', reverse:'OrganizationalUnit-manager'
//            integer 'age', minValue:0, maxValue:150, readOnly:true, useExtension:true
//            binary 'photo', maxLength:1048576, id:'Employee-photo', fileFilter:[
//              'images':['.jpg','.bmp']
//              ]
//          }
//          iconImage URL:'male-48x48.png'
//          uncloned properties:['managedOu','ssn']
//          ordering properties:['name':'ASCENDING']
//          rendered properties:['name','firstName',
//            'gender','birthDate','age','ssn','salary','contact',
//            'married','preferredColor','photo','company',
//            'createTimestamp','lastUpdateTimestamp']
//        }
//      
//      Entity('Company',extend:['Nameable', 'Traceable']) {
//        properties{
//          set 'departments', composition:true, ref:'Department'
//          set 'employees', composition:true, ref:'Employee'
//          refId 'contact',id:'contact'
//        }
//        iconImage URL:'company-48x48.png'
//        rendered properties:[
//          'name','contact.phone','createTimestamp','lastUpdateTimestamp'
//          ]
//      }
//      
//      Entity('OrganizationalUnit',extend:['Nameable', 'Traceable'],purelyAbstract:true,
//        processor :'OrganizationalUnitPropertyProcessors',
//        extension :'OrganizationalUnitExtension',
//        ) {
//          properties {
//            string_6 'ouId', regex:"[A-Z]{2}-[\\d]{3}", regexSample:'AB-123', mandatory:true
//            refId 'contact',id:'contact'
//            reference 'manager', ref:'Employee', mandatory:true,
//              processors:['ManagerProcessor'],
//              initializationMapping:['company':'company'],
//              id:'OrganizationalUnit-manager'
//            reference 'company', ref:'Company', useExtension:true
//          }
//        }
//      
//      Entity('Department',extend:'OrganizationalUnit') {
//        properties {
//          reference 'company', ref:'Company', reverse:'Company-departments', mandatory:true
//          set 'teams', composition:true, ref:'Team'
//        }
//        iconImage URL:'department-48x48.png'
//        rendered properties:['ouId','name','manager','contact','createTimestamp','lastUpdateTimestamp']
//      }
//      
//      Entity('Team',extend:['OrganizationalUnit']){
//        
//        properties {
//          reference 'department', ref:'Department', mandatory:true, reverse:'Department-teams'
//          set 'teamMembers', ref:'Employee', reverse:'Employee-teams'
//        }
//        iconImage URL:'team-48x48.png'
//        rendered properties:['ouId','name','manager','contact']
//      }
    }
  }
}
domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['outputFileName'])