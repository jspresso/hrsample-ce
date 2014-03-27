workspace('masterdata.workspace',
    icon:'masterdata-48x48.png') {
      nodeModule('masterdata.geography.module',
          icon:'geography-48x48.png') {
            filterModule('masterdata.cities.module',
                component:'City',
//                detailView:'City.module.view',
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
          //filterView:'Employee.filter.pane',
          moduleView:'Employee.table.view',
          detailView:'Employee.module.view',
          startup:'filterModuleStartup',
          pageSize:4)
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
          detailView:'Company.module.view',
          startup:'filterModuleStartup')
    }

workspace('departments.workspace',
    icon:'department-48x48.png') {
  filterModule('departments.module',
      icon:'department-48x48.png',
      component:'Department',
      filterView:'Department.filter.pane')
}

action 'startupHrsampleAction',
    class:'org.jspresso.framework.application.frontend.action.module.ModuleSelectionAction',
    custom:[workspaceName:'organization.workspace', moduleName:'companies.module']

action 'filterModuleStartup',
    class:'org.jspresso.framework.application.frontend.action.FrontendAction',
    wrapped:'initModuleFilterAction',
    next:'queryModuleFilterAction'
    
actionMap('controllerActionMap') {
  actionList {
    action ref:'showRunningExecutorsAction'
    action ref:'changePasswordAction'
  }
}

controller 'hrsample.name',
    icon:'people-48x48.png',
    context:'hrsample',
    language:'en',
    startup:'startupHrsampleAction',
    actionMap:'controllerActionMap',
    width:1200,
    height:800,
    workspaces:[
      'organization.workspace',
      'employees.workspace',
      'masterdata.workspace',
      //'departments.workspace',
    ]

bean 'viewFactoryBase', parent:'abstractViewFactory',
    custom: [
      defaultActionMapRenderingOptions:'LABEL_ICON'
    ]
