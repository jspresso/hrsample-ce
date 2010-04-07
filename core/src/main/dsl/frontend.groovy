external id:['abstractFrontController',
             'remotePeerRegistry',
             'guidGenerator',
             'connectorFactory',
             'modelConnectorFactory',
             'mvcBinder',
             'translationProvider',
             'modelCascadingBinder',
             'iconFactory',
             'actionFactory'
             ]

workspace('masterdata.workspace',
  icon:'masterdata-48x48.png') {
  nodeModule('masterdata.geography.module',
    icon:'geography-48x48.png') {
    filterModule('masterdata.cities.module',
      component:'City',
      detailView:'City.module.view')
  }
}

workspace('employees.workspace',
  icon:'people-48x48.png',
  grantedRoles:['administrator','staff-manager']) {
  filterModule('employees.module',
    icon:'employees-48x48.png',
    component:'Employee',
    detailView:'Employee.module.view',
    pageSize:4)
}

workspace('organization.workspace',
  icon:'structure-48x48.png',
  grantedRoles:['administrator','organization-manager']) {
  filterModule('companies.module',
    icon:'company-48x48.png',
    component:'Company',
    detailView:'Company.module.view')
}

action 'startupHrsampleAction',
  class:'org.jspresso.framework.application.frontend.action.module.ModuleSelectionAction',
  custom:[workspaceName:'organization.workspace', moduleName:'companies.module']

controller 'hrsample.name',
  icon:'people-48x48.png',
  context:'hrsample',
  language:'en',
  startup:'startupHrsampleAction',
  workspaces:['organization.workspace','employees.workspace','masterdata.workspace']

bean 'abstractViewFactory', class:'org.jspresso.framework.view.AbstractViewFactory',
  custom: [
    connectorFactory_ref:'connectorFactory',
    modelConnectorFactory_ref:'modelConnectorFactory',
    mvcBinder_ref:'mvcBinder',
    translationProvider_ref:'translationProvider',
    modelCascadingBinder_ref:'modelCascadingBinder',
    iconFactory_ref:'iconFactory',
    actionFactory_ref:'actionFactory',
    lovAction_ref:'lovAction',
    openFileAsBinaryPropertyAction_ref:'openFileAsBinaryPropertyAction',
    saveBinaryPropertyAsFileAction_ref:'saveBinaryPropertyAsFileAction',
    resetPropertyAction_ref:'resetPropertyFrontAction',
    binaryPropertyInfoAction_ref:'binaryPropertyInfoAction',
    defaultActionMapRenderingOptions:'LABEL_ICON']
              
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