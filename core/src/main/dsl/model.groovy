Interface ('Nameable') { string_64 'name', mandatory:true }

Interface('Traceable',
  interceptors:'TraceableLifecycleInterceptor',
  icon:'traceable-48x48.png',
  uncloned:['createTimestamp', 'lastUpdateTimestamp']) {
    date_time 'createTimestamp', readOnly:true
    date_time 'lastUpdateTimestamp', readOnly:true
  }

Entity('City',
  extend:'Nameable',
  icon:'city-48x48.png'/*,
 pageSize:2*/) { string_10 'zip' }

Component('ContactInfo') {
  string_256 'address'
  reference  'city', ref:'City'
  string_32  'phone'
  string_128 'email', regex:'[\\w\\-\\.]*@[\\w\\-\\.]*', regexSample:'contact@acme.com'
}

Entity('Event',extend:'Traceable'){
  html 'text', maxLength:2048 , id:'Event-text'
}

Entity ('Employee',
  extend:['Nameable','Traceable'],
  interceptors:'EmployeeLifecycleInterceptor',
  extension :'EmployeeExtension',
  processor:'EmployeePropertyProcessors',
  services:[EmployeeService:'EmployeeServiceDelegate'],
  icon:'male-48x48.png',
  uncloned:['managedOu','ssn'],
  ordering:['name':'ASCENDING'],
  rendered:['name','firstName','gender','birthDate','age','ssn','salary','contact','married','preferredColor','photo','company','createTimestamp','lastUpdateTimestamp']) {
    string_32 'firstName', mandatory:true, processors:'FirstNameProcessor'
    string_10 'ssn', regex:"[\\d]{10}", regexSample:'0123456789', unicityScope:'empSsn'
    date 'birthDate', processors:'BirthDateProcessor'
    date 'hireDate'
    enumeration 'gender', enumName:'GENDER', mandatory:true, valuesAndIcons:[
      'M':'male-48x48.png',
      'F':'female-48x48.png']
    color 'preferredColor'
    bool 'married'
    decimal 'salary', minValue:0, usingBigDecimal:true
    binary 'photo', maxLength:1048576, id:'Employee-photo', fileFilter:['images':['.jpg','.bmp']]
    password 'password', maxLength:32
    reference 'contact', ref:'ContactInfo', id:'contact'
    list 'events', composition:true, ref:'Event'
    set 'teams', ref:'Team'
    reference 'company', ref:'Company', mandatory:true, reverse:'Company-employees'
    reference 'managedOu', ref:'OrganizationalUnit', reverse:'OrganizationalUnit-manager'
    integer 'age', minValue:0, maxValue:150, readOnly:true, computed:true
    string_512 'genderImageUrl', id:'Employee-genderImageUrl', readOnly:true, computed:true
  }

Entity('Company',
  extend:['Nameable', 'Traceable'],
  icon:'company-48x48.png',
  rendered:['name','contact','createTimestamp','lastUpdateTimestamp']) {
    refId 'contact', id:'contact'
    set 'departments', composition:true, ref:'Department'
    set 'employees', composition:true, ref:'Employee'
  }

Entity('OrganizationalUnit',
  extend:['Nameable', 'Traceable'],
  purelyAbstract:true,
  processor :'OrganizationalUnitPropertyProcessors',
  extension :'OrganizationalUnitExtension') {
    string_6 'ouId', regex:"[A-Z]{2}-[\\d]{3}", regexSample:'AB-123', mandatory:true
    refId 'contact', id:'contact'
    reference 'manager', id:'OrganizationalUnit-manager', ref:'Employee', mandatory:true, processors:['ManagerProcessor'], initializationMapping:['company':'company']
    reference 'company', ref:'Company', computed:true
  }

Entity('Department',
  extend:'OrganizationalUnit',
  extension :'DepartmentExtension',
  icon:'department-48x48.png',
  rendered:['ouId','name','manager','contact','createTimestamp','lastUpdateTimestamp']) {
    reference 'company', ref:'Company', reverse:'Company-departments', mandatory:true
    set 'teams', composition:true, ref:'Team'
    set 'employees', ref:'Employee', computed:true
  }

Entity('Team',
  extend:['OrganizationalUnit'],
  icon:'team-48x48.png',
  rendered:['ouId','name','manager','contact']){
    reference 'department', ref:'Department', mandatory:true, reverse:'Department-teams'
    set 'teamMembers', ref:'Employee', reverse:'Employee-teams'
  }
