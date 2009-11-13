workspace('Masterdata.workspace',
  icon:'masterdata-48x48.png') {
  nodeModule('masterdata.geography.module',
    icon:'geography-48x48.png') {
    module('masterdata.cities.module',
      component:'City',
      view:'City.module.view')
  }
}

workspace('Employees.workspace',
  icon:'people-48x48.png',
  grantedRoles:['administrator','staff-manager']) {
  module('employees.module',
    icon:'employees-48x48.png',
    component:'Employee',
    view:'Employee.module.view',
    pageSize:4)
}

workspace('Organization.workspace',
  icon:'structure-48x48.png',
  grantedRoles:['administrator','organization-manager']) {
  module('companies.module',icon:'company-48x48.png',
  component:'Company',
  view:'Company.module.view')
}

controller('hrsample.name',
  icon:'people-48x48.png',
  context:'hrsample',
  language  :'en',
  workspaces:['Masterdata.workspace','Employees.workspace','Organization.workspace'])