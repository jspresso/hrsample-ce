external id:[
  'translationProvider',
  'abstractBackControllerBase',
  'dataSource'
]

bean('EmployeeServiceDelegateBean',
  class:'org.jspresso.hrsample.model.service.EmployeeServiceDelegate',
  custom:[translationProvider_ref:'translationProvider'])

bean('userPreferencesStore',
  class:'org.jspresso.framework.util.preferences.JdbcPreferencesStore',
  custom:[keyColumnName:'ID',dataSource_ref:'dataSource', 'defaultRestrictions':[VERSION:'0']])

//bean('customSecurityPlugin',
//  class:'org.jspresso.hrsample.development.SnifferSecurityPlugin',
//  custom:[fileName:'D:/tmp/security_dump.txt'])
