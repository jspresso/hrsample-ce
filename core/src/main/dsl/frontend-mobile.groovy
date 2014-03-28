workspace('masterdata.workspace',
    icon:'masterdata-48x48.png') {
  nodeModule('masterdata.geography.module',
      icon:'geography-48x48.png') {
    filterModule('masterdata.cities.module',
        component:'City',
        filterView: 'City.filter.view',
        pageSize: 10,
        startup:'filterModuleStartup')
  }
}

workspace('employees.workspace',
    icon:'people-48x48.png',
    grantedRoles:[
        'administrator',
        'staff-manager'
    ]) {
  filterModule('employees.module',
      icon:'employees-48x48.png',
      component:'Employee',
      filterView: 'Employee.filter.view',
      detailView:'Employee.page.view',
      pageSize: 10,
      startup:'filterModuleStartup')
}

workspace('organization.workspace',
    icon:'structure-48x48.png',
    grantedRoles:[
        'administrator',
        'organization-manager'
    ]) {
  filterModule('companies.module',
      icon:'company-48x48.png',
      component:'Company',
      filterView: 'Company.filter.view',
      detailView:'Company.page.view',
      pageSize: 10,
      startup:'filterModuleStartup')
}

workspace('departments.workspace',
    icon:'department-48x48.png') {
  filterModule('departments.module',
      icon:'department-48x48.png',
      component:'Department')
}

controller 'hrsample.name',
    icon:'people-48x48.png',
    context:'hrsample',
    language:'en',
    startup:'startupHrsampleAction',
    workspaces:[
        'organization.workspace',
        'employees.workspace',
        'masterdata.workspace'
    ]

