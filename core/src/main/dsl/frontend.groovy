bean ('viewFactoryBase', parent:'abstractViewFactory',
  custom: [defaultActionMapRenderingOptions:'LABEL_ICON'])

bean('iconFactoryBase', parent:'abstractIconFactory') {
    bean('largeIconSize', class:'org.jspresso.framework.util.gui.Dimension', custom: [width: 32, height: 32])
    bean('mediumIconSize', class: 'org.jspresso.framework.util.gui.Dimension', custom: [width: 24, height: 24])
    bean('smallIconSize', class: 'org.jspresso.framework.util.gui.Dimension', custom: [width: 20, height: 20])
    bean('tinyIconSize', class: 'org.jspresso.framework.util.gui.Dimension', custom: [width: 16, height: 16])
}

action ('startupHrsampleAction',
  class:'org.jspresso.framework.application.frontend.action.module.ModuleSelectionAction',
  custom:[workspaceName:'organization.workspace', moduleName:'companies.module'])

action ('filterModuleStartup',
  class:'org.jspresso.framework.application.frontend.action.FrontendAction',
  wrapped:'initModuleFilterAction',
  next:'queryModuleFilterAction')


/*
 * Controler
 */
controller ('hrsample.name',
    icon:'people.png',
    context:'hrsample',
    language:'en',
    startup:'startupHrsampleAction',
    actionMap:'controllerActionMap',
    width:1100,
    height:600,
    workspaces:[
      'organization.workspace',
      'employees.workspace',
      'masterdata.workspace',
      //'departments.workspace',
    ])

action('helpFrontAction', 
  parent:'staticInfoFrontAction', name:'help.action.name', description:'', 
  custom:[messageCode:'help'])
    
actionMap('controllerActionMap') { 
  actionList {
    action ref:'showRunningExecutorsAction'
    action ref:'changePasswordAction'
  }
}

/*
 * Workspaces
 */
workspace('masterdata.workspace',
  icon:'masterdata.png') {

  nodeModule('masterdata.geography.module',
    icon:'geography.png') {
    
      filterModule('masterdata.cities.module',
          component:'City',
          detailView:'City.module.view',
          startup:'filterModuleStartup')
    }
}

workspace('employees.workspace',
  icon:'people.png',
  grantedRoles:[
    'administrator',
    'staff-manager']) {
  
filterModule('employees.module',
    icon:'employees.png',
    component:'Employee',
    //filterView:'Employee.filter.pane',
    moduleView:'Employee.table.view',
    detailView:'Employee.module.view',
    startup:'filterModuleStartup',
    pageSize:4)
}

workspace('organization.workspace',
  icon:'structure.png',
  grantedRoles:[
    'administrator',
    'organization-manager']) {
  
filterModule('companies.module',
    icon:'company.png',
    component:'Company',
    detailView:'Company.module.view',
    startup:'filterModuleStartup')
}

workspace('departments.workspace',
  icon:'department.png') {
filterModule('departments.module',
    icon:'department.png',
    component:'Department',
    filterView:'Department.filter.pane')
}

