/*
 * Controller
 */
controller ('hrsample.name',
  description: 'hrsample.description',
  icon:'people.png',
  context:'hrsample',
  language:'en',
  startup:'startupHRSampleAction',
  workspaces:[
      'organization.workspace',
      'employees.workspace',
      'masterdata.workspace',
      'parameters.mobile.workspace'
  ])

/*
 * Workspaces
 */
workspace('masterdata.workspace',
    icon:'masterdata.png', headerDescription:'masterdata.workspace.header') {
  
  nodeModule('masterdata.geography.module',
      icon:'geography.png') {
      
    filterModule('masterdata.cities.module',
        component:'City',
        filterView: 'City.filter.view',
        detailView: 'City.page.view',
        pageSize: 10,
        startup:'filterModuleStartup')
  }
}

workspace('employees.workspace',
    icon:'people.png',
    grantedRoles:[
        'administrator',
        'staff-manager'
    ]) {
  filterModule('employees.module',
      icon:'employees.png',
      component:'Employee',
      filterView: 'Employee.filter.view',
      detailView:'Employee.page.view',
      pageSize: 10,
      startup:'filterModuleStartup')
}

workspace('organization.workspace',
    icon:'structure.png',
    grantedRoles:[
        'administrator',
        'organization-manager'
    ]) {
  filterModule('companies.module',
      icon:'company.png',
      component:'Company',
      filterView: 'Company.filter.view',
      detailView:'Company.page.view',
      pageSize: 10,
      startup:'filterModuleStartup')
}

workspace('departments.workspace',
    icon:'department.png') {
  filterModule('departments.module',
      icon:'department.png',
      component:'Department')
}

workspace('parameters.mobile.workspace', 
  icon:'classpath:org/jspresso/framework/application/images/execute-48x48.png', 
  headerDescription:'parameters.mobile.workspace.header') {
    
  beanModule ('my.profile.module', icon:'people.png',
    moduleView:'my.profile.module.page',
    entry:'myProfileModuleInitAction',
    component:'Employee')
    
}
    
/*
 * login
 */
actionMap('secondaryLoginActionMapBase') {
  actionList(description:'login.action.title') {
    action ref:'helpFrontAction'
  }
}
actionMap('secondaryLoginActionMap',
  parents:['secondaryLoginActionMapBase'])

/*
 * Init action  
 */
action ('startupHRSampleAction',
  class:'org.jspresso.hrsample.frontend.UserSessionInitAction')

