workspace('Masterdata.workspace',
  icon:'masterdata-48x48.png') {
  nodeModule('masterdata.geography.module',
    icon:'geography-48x48.png') {
    filterModule('masterdata.cities.module',
      component:'City',
      detailView:'City.module.view')
  }
}

workspace('Employees.workspace',
  icon:'people-48x48.png',
  grantedRoles:['administrator','staff-manager']) {
  filterModule('employees.module',
    icon:'employees-48x48.png',
    component:'Employee',
    detailView:'Employee.module.view',
    pageSize:4)
}

workspace('Organization.workspace',
  icon:'structure-48x48.png',
  grantedRoles:['administrator','organization-manager']) {
  filterModule('companies.module',
    icon:'company-48x48.png',
    component:'Company',
    detailView:'Company.module.view')
}

controller('hrsample.name',
  icon:'people-48x48.png',
  context:'hrsample',
  language  :'en',
  workspaces:['Masterdata.workspace','Employees.workspace','Organization.workspace'])