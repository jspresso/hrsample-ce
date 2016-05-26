paramSet 'gender', enumName: 'GENDER', mandatory: true, queryMultiselect: true,
    valuesAndIcons: ['M': 'male.png',
                     'F': 'female.png'],
    defaultValue: 'M'

Interface('Nameable') { string_64 'name', mandatory: true, translatable: true }

Interface('Traceable',
    interceptors: 'TraceableLifecycleInterceptor',
    icon: 'traceable.png',
    uncloned: ['createTimestamp',
               'lastUpdateTimestamp']) {
  date_time 'createTimestamp', timeZoneAware: true, readOnly: true
  date_time 'lastUpdateTimestamp', timeZoneAware: true, readOnly: true
}

Entity('City',
    extend: 'Nameable',
    icon: 'city.png',
    pageSize: 4,
    toString: 'name') {
  string_10 'zip', upperCase: true
  decimal 'longitude', maxValue: 190, minValue: -190, maxFractionDigit: 4
  decimal 'latitude', maxValue: 190, minValue: -190, maxFractionDigit: 4
  set 'neighbours', ref: 'City', reverse: 'City-neighbours'
}

Component('ContactInfo', 
  extension:'ContactInfoExtension') {
  
  string_256 'address'
  reference 'city', ref: 'City'
  string_32 'phone'
  string_128 'email', regex: '[\\w\\-\\.]*@[\\w\\-\\.]*', regexSample: 'contact@acme.com'
  
  html 'phoneAsHtml', computed:true, i18nNameKey:'phone'
}

Entity('Event', extend: 'Traceable') {
  html 'text', maxLength: 2048, id: 'Event-text'
  reference 'employee', ref: 'Employee', reverse: 'Employee-events'
}

Entity('Employee',
    extend: ['Nameable', 'Traceable'],
    interceptors: 'EmployeeLifecycleInterceptor',
    extension: 'EmployeeExtension',
    processor: 'EmployeePropertyProcessors',
    services: [EmployeeService: 'EmployeeServiceDelegate'],
    serviceBeans: ['EmployeeService': 'EmployeeServiceDelegateBean'],
    icon: 'male.png',
    uncloned: ['managedOu', 'ssn'],
    ordering: ['name': 'ASCENDING'],
    toString: 'fullName',
    toHtml: 'htmlDescription',
    autoComplete: 'name',
    pageSize: 20,
    rendered: ['name',
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
               'lastUpdateTimestamp'],
    queryable: ['name',
                'firstName',
                'gender',
                'birthDate',
                'company',
                'managedOu.manager',
                'managedOu.createTimestamp',
                'managedOu.manager.salary',
                'managedOu.contact.email',
                'managedOu.manager.company',
                'managedOu.manager.company.workforce']) {
  string_32 'firstName', mandatory: true, processors: 'FirstNameProcessor', translatable: true
  string_10 'ssn', regex: "[\\d]{10}", regexSample: '0123456789', unicityScope: 'empSsn'
  date 'birthDate', processors: 'BirthDateProcessor'
  date 'hireDate'
  enumeration 'gender', paramSets: 'gender'
  color 'preferredColor'
  bool 'married'
  decimal 'salary', minValue: 0, maxValue: 10000000, usingBigDecimal: true
  image 'photo', maxLength: 1048576, id: 'Employee-photo', fileFilter: ['images': ['.jpg']], fileName: 'photo.jpg',
      processors: 'PhotoProcessor', scaledHeight: 200
  image 'signature', maxLength: 1048576, id: 'Employee-signature', fileFilter: ['images': ['.png']],
      fileName: 'signature.png'
  password 'password', maxLength: 32
  reference 'contact', ref: 'ContactInfo', id: 'contact'
  list 'events', composition: true, ref: 'Event'
  list 'alternativeEvents', composition: true, ref: 'Event', nullElement: true
  set 'teams', ref: 'Team'
  list 'alternativeContacts', ref: 'ContactInfo', nullElement: true
  reference 'company', ref: 'Company', mandatory: true, reverse: 'Company-employees'
  reference 'managedOu', ref: 'OrganizationalUnit', reverse: 'OrganizationalUnit-manager'
  integer 'age', minValue: 0, maxValue: 150, sqlName: 'YEAR(current_date) - YEAR(BIRTH_DATE)', computed: true, cacheable: true
  imageUrl 'genderImageUrl', maxLength: 512, id: 'Employee-genderImageUrl', computed: true, scaledHeight: 100
  string_512 'fullName', computed: true, i18nNameKey:'name', id:'Employee-fullname'
  html 'htmlDescription', computed: true
  set 'users', ref:'User', reverse:'User-employee'
  enumeration 'language', enumName:'language', values:['fr', 'en'], defaultValue:'fr'
}

Entity('Company',
    extend: ['Nameable', 'Traceable'],
    extension: 'CompanyExtension',
    icon: 'company.png',
    rendered: ['name',
               'contact',
               'createTimestamp',
               'lastUpdateTimestamp'],
    queryable: ['name',
                'contact.city',
                'contact.city.zip']) {
  refId 'contact', id: 'contact'
  set 'departments', composition: true, ref: 'Department'
  set 'employees', composition: true, ref: 'Employee'

  integer 'workforce', computed: true
}

Entity('OrganizationalUnit',
    extend: ['Traceable'],
    purelyAbstract: true,
    processor: 'OrganizationalUnitPropertyProcessors',
    extension: 'OrganizationalUnitExtension') {
  string_6 'ouId', regex: "[A-Z]{2}-[\\d]{3}", regexSample: 'AB-123', mandatory: true
  refId 'contact', id: 'contact'
  reference 'manager', ref: 'Employee', mandatory: false, processors: ['ManagerProcessor'],
      initializationMapping: ['company': 'company']
  reference 'company', ref: 'Company', computed: true
  html 'htmlDescription', computed: true
}

Entity('Department',
    extend: ['Nameable', 'OrganizationalUnit'],
    extension: 'DepartmentExtension',
    icon: 'department.png',
    toHtml: 'htmlDescription', ordering: ['name': 'ASCENDING'],
    rendered: ['ouId',
               'name',
               'manager',
               'contact',
               'createTimestamp',
               'lastUpdateTimestamp']) {
  reference 'company', ref: 'Company', reverse: 'Company-departments', mandatory: true
  set 'teams', composition: true, ref: 'Team'
  set 'employees', ref: 'Employee', computed: true
  integer 'teamCount', computed: true, sqlName: '(SELECT COUNT(T.ID) FROM TEAM T WHERE T.DEPARTMENT_ID=ID)'
}

Entity('Team',
    extend: ['Nameable', 'OrganizationalUnit'],
    icon: 'team.png',
    toHtml: 'htmlDescription', ordering: ['name': 'ASCENDING'],
    rendered: ['ouId',
               'name',
               'manager',
               'contact']) {
  reference 'department', ref: 'Department', mandatory: true, reverse: 'Department-teams'
  set 'teamMembers', ref: 'Employee', reverse: 'Employee-teams'
}

Entity('Preferences') {
  string_2048 'preferenceValue';
  string_128 'preferencePath';
}

Component('Link', extend: 'Nameable', rendered: 'name') {
  reference 'parent', ref: 'Link', mandatory: false
  set 'children', ref: 'Link'
}
 
Entity('User',
  extend: ['Traceable'],
  extension:'UserExtension',
  rendered:['login', 'password', 'employee', 'createTimestamp', 'lastUpdateTimestamp'],
  services:['UserService':'UserServiceDelegate']) {
  
  string_64 'login', mandatory:true, unicityScope:'community'
  password  'password', maxLength:32
  
  set 'roles', ref:'Role'
  
  // relations
  reference 'employee', ref:'Employee', i18nNameKey:'org.jspresso.hrsample.model.Employee'
  
  // computed
  reference 'mainRole', ref:'Role', computed:true
}

Entity('Role', 
  extend:['Traceable'],
  rendered:['roleId', 'createTimestamp', 'lastUpdateTimestamp'],
  icon:'employees.png') {
  
  string_30 'roleId', unicityScope:'roleId'
  
  set 'users', ref:'User', reverse:'User-roles'
}