Interface ('Nameable') { string_64 'name', mandatory:true, translatable: true }

Interface('Traceable',
interceptors:'TraceableLifecycleInterceptor',
icon:'traceable-48x48.png',
uncloned:[
  'createTimestamp',
  'lastUpdateTimestamp'
]) {
  date_time 'createTimestamp', timeZoneAware:true, readOnly:true
  date_time 'lastUpdateTimestamp', timeZoneAware:true, readOnly:true
}

Entity('City',
extend:'Nameable',
icon:'city-48x48.png',
pageSize:4,
toString:'name') { string_10 'zip', upperCase:true }

Component('ContactInfo') {
  string_256 'address'
  reference  'city', ref:'City'
  string_32  'phone'
  string_128 'email', regex:'[\\w\\-\\.]*@[\\w\\-\\.]*', regexSample:'contact@acme.com'
}

Entity('Event',extend:'Traceable'){
  html 'text', maxLength:2048 , id:'Event-text'
  reference 'employee', ref:'Employee', reverse:'Employee-events'
}

Entity ('Employee',
extend:['Nameable', 'Traceable'],
interceptors:'EmployeeLifecycleInterceptor',
extension :'EmployeeExtension',
processor:'EmployeePropertyProcessors',
services:[EmployeeService:'EmployeeServiceDelegate'],
serviceBeans:['EmployeeService':'EmployeeServiceDelegateBean'],
icon:'male-48x48.png',
uncloned:['managedOu', 'ssn'],
ordering:['name':'ASCENDING'],
toString:'fullName',
toHtml:'htmlDescription',
autoComplete:'name',
pageSize:3,
rendered:[
  'name',
  'firstName',
  'gender',
  'birthDate',
  'age',
  'ssn',
  'salary',
  'contact',
  'married',
  'preferredColor',
  'photo',
  'company',
  'createTimestamp',
  'lastUpdateTimestamp'
]) {
  string_32 'firstName', mandatory:true, processors:'FirstNameProcessor', translatable:true
  string_10 'ssn', regex:"[\\d]{10}", regexSample:'0123456789', unicityScope:'empSsn'
  date 'birthDate', processors:'BirthDateProcessor'
  date 'hireDate'
  enumeration 'gender', enumName:'GENDER', mandatory:true, valuesAndIcons:[
    'M':'male-48x48.png',
    'F':'female-48x48.png']
  color 'preferredColor'
  bool 'married'
  decimal 'salary', minValue:0, maxValue:10000000, usingBigDecimal:true
  binary 'photo', maxLength:1048576, id:'Employee-photo', fileFilter:['images':['.jpg']], fileName:'photo.jpg'
  password 'password', maxLength:32
  reference 'contact', ref:'ContactInfo', id:'contact'
  list 'events', composition:true, ref:'Event'
  list 'alternativeEvents', composition:true, ref:'Event', nullElement:true
  set 'teams', ref:'Team'
  list 'alternativeContacts', ref:'ContactInfo', nullElement:true
  reference 'company', ref:'Company', mandatory:true, reverse:'Company-employees'
  reference 'managedOu', ref:'OrganizationalUnit', reverse:'OrganizationalUnit-manager'
  integer 'age', minValue:0, maxValue:150, sqlName:'YEAR(BIRTH_DATE)', computed:true, cacheable:true
  imageUrl 'genderImageUrl', maxLength:512, id:'Employee-genderImageUrl', computed:true
  string_512 'fullName', computed:true
  html 'htmlDescription', computed:true
}

Entity('Company',
extend:['Nameable', 'Traceable'],
extension:'CompanyExtension',
icon:'company-48x48.png',
rendered:[
  'name',
  'contact',
  'createTimestamp',
  'lastUpdateTimestamp'
],
queryable:[
  'name',
  'contact.city',
  'contact.city.zip'
]) {
  refId 'contact', id:'contact'
  set 'departments', composition:true, ref:'Department'
  set 'employees', composition:true, ref:'Employee'
  
  integer 'workforce', computed:true
}

Entity('OrganizationalUnit',
extend:['Nameable', 'Traceable'],
purelyAbstract:true,
processor :'OrganizationalUnitPropertyProcessors',
extension :'OrganizationalUnitExtension') {
  string_6 'ouId', regex:"[A-Z]{2}-[\\d]{3}", regexSample:'AB-123', mandatory:true
  refId 'contact', id:'contact'
  reference 'manager', ref:'Employee', mandatory:false, processors:['ManagerProcessor'], initializationMapping:['company':'company']
  reference 'company', ref:'Company', computed:true
  html 'htmlDescription', computed:true
}

Entity('Department',
extend:'OrganizationalUnit',
extension :'DepartmentExtension',
icon:'department-48x48.png',
toHtml:'htmlDescription',ordering:['name':'ASCENDING'],
rendered:[
  'ouId',
  'name',
  'manager',
  'contact',
  'createTimestamp',
  'lastUpdateTimestamp'
]) {
  reference 'company', ref:'Company', reverse:'Company-departments', mandatory:true
  set 'teams', composition:true, ref:'Team'
  set 'employees', ref:'Employee', computed:true
  integer 'teamCount', computed:true, sqlName:'(SELECT COUNT(T.ID) FROM TEAM T WHERE T.DEPARTMENT_ID=ID)'
}

Entity('Team',
extend:['OrganizationalUnit'],
icon:'team-48x48.png',
toHtml:'htmlDescription',ordering:['name':'ASCENDING'],
rendered:[
  'ouId',
  'name',
  'manager',
  'contact'
]){
  reference 'department', ref:'Department', mandatory:true, reverse:'Department-teams'
  set 'teamMembers', ref:'Employee', reverse:'Employee-teams'
}

Entity('Preferences') {
  string_2048 'preferenceValue';
  string_128 'preferencePath';
}
