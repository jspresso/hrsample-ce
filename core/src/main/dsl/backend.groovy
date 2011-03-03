external id:[
  'translationProvider',
  'abstractBackControllerBase'
  ]

bean('EmployeeServiceDelegateBean',
  class:'org.jspresso.hrsample.model.service.EmployeeServiceDelegate',
  custom:[translationProvider_ref:'translationProvider'])

bean('abstractBackController',
  parent:'abstractBackControllerBase') {
    bean('userPreferenceStore',
      class:'org.jspresso.framework.util.preferences.JdbcPreferenceStore',
      custom:[keyColumnName:'ID',dataSource_ref:'dataSource', 'defaultRestrictions':[VERSION:'0']])
  }
