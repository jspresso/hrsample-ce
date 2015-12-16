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

controller 'hrsample.name',
    description: 'hrsample.description',
    icon:'people.png',
    context:'hrsample',
    language:'en',
    //startup:'startupHrsampleAction',
    workspaces:[
        'organization.workspace',
        'employees.workspace',
        'masterdata.workspace'
    ]

