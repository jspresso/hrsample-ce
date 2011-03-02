external id:[
  'abstractFrontControllerBase',
  'remotePeerRegistry',
  'guidGenerator',
  'abstractViewFactory',
  'dataSource'
  ]

workspace('masterdata.workspace',
  icon:'masterdata-48x48.png') {
    nodeModule('masterdata.geography.module',
    icon:'geography-48x48.png') {
      filterModule('masterdata.cities.module',
      component:'City',
      detailView:'City.module.view',
      startup:'filterModuleStartup')
    }
  }

workspace('employees.workspace',
  icon:'people-48x48.png',
  grantedRoles:['administrator','staff-manager']) {
    filterModule('employees.module',
    icon:'employees-48x48.png',
    component:'Employee',
    detailView:'Employee.module.view',
    startup:'filterModuleStartup',
    pageSize:4)
  }

workspace('organization.workspace',
  icon:'structure-48x48.png',
  grantedRoles:['administrator','organization-manager']) {
    filterModule('companies.module',
    icon:'company-48x48.png',
    component:'Company',
    detailView:'Company.module.view',
    startup:'filterModuleStartup')
  }

action 'startupHrsampleAction',
  class:'org.jspresso.framework.application.frontend.action.module.ModuleSelectionAction',
  custom:[workspaceName:'organization.workspace', moduleName:'companies.module']
  
action 'filterModuleStartup',
  class:'org.jspresso.framework.application.frontend.action.FrontendAction',
  wrapped:'initModuleFilterAction',
  next:'queryModuleFilterAction'

bean('abstractFrontController',
  parent:'abstractFrontControllerBase') {
    bean('userPreferenceStore',
      class:'org.jspresso.framework.util.preferences.JdbcPreferenceStore',
      custom:[keyColumnName:'ID',dataSource_ref:'dataSource', 'defaultRestrictions':[VERSION:'0']])
  }

controller 'hrsample.name',
  icon:'people-48x48.png',
  context:'hrsample',
  language:'en',
  startup:'startupHrsampleAction',
  workspaces:['organization.workspace','employees.workspace','masterdata.workspace']

bean 'viewFactoryBase', parent:'abstractViewFactory',
  custom: [
  defaultActionMapRenderingOptions:'LABEL_ICON'
  ]

spec('remote-recording') {
  bean('frontController',
  class:'org.jspresso.framework.application.frontend.controller.remote.RecordingRemoteController',
  parent:'abstractFrontController',
  custom:[
  remotePeerRegistry_ref:'remotePeerRegistry',
  guidGenerator_ref:'guidGenerator',
  commandsFileName:'/tmp/commands.xml'
  ]
  )
}