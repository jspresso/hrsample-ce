template 'form',
  parent:'decoratedView',
  labelsPosition:'ABOVE',
  columnCount:2
  
template 'table',
  parent:'decoratedView'

form 'Traceable.pane',
  model:'Traceable',
  description:'traceable.editing',
  fields:['createTimestamp','lastUpdateTimestamp']

form 'Company.pane',
  labelsPosition:'ASIDE',
  fields:['name','contact.address','contact.city','contact.phone','contact.email'],
  width:[name:2],
  description:'company.editing'

treeNode 'Department-teams.treeNode',
  render:'ouId',
  actionMap:'masterDetail'
  
treeNode 'Department-employees.treeNode',
  render:'name',
  actionMap:'masterDetail'
      
treeNode 'Company-employees.treeNode',
  render:'name',
  actionMap:'masterDetail'
            
treeNode 'Company-departments.treeNode',
  render:'ouId',
  actionMap:'masterDetail'

tree('Company.tree',
  render:'name',
  icon:'structure-48x48.png') {
  subTree('Company-employees.treeNode')
  subTree('Company-departments.treeNode') {
    subTree('Department-teams.treeNode')
    //subTree('Department-employees.treeNode')
  }
}

tabs 'Company.tab.pane',
  views:['Company.pane','Company.tree','Traceable.pane']

table 'Company-departments.table',
  actionMap:'masterDetail'

table 'Department-teams.table',
  column:['ouId','name','manager'],
  actionMap:'masterDetail'

action('addFromList',
  parent:'lovOkFrontAction') {
  next(parent:'addAnyToMasterFrontAction')
}

table('Team-teamMembers.table') {
  actionMap {
    actionList('EDIT'){
      action(parent:'lovAction',
        custom:[
          autoquery:false,
          entity:'Employee',
          initializationMapping:['company':'company'],
          okAction_ref:'addFromList'
        ]
      )
      action(ref:'removeAnyCollectionFromMasterFrontAction')
    }
  }
}

split_Vertical('Departments.and.teams.view',
  cascadingModels:true,
  top:'Company-departments.table') {
  bottom {
    split_Horizontal (
      left:'Department-teams.table',
      right:'Team-teamMembers.table',
      cascadingModels:true
    )
  }
}

split_Vertical 'Company.organization.view',
  model:'Company',
  top:'Company.tab.pane',
  bottom:'Departments.and.teams.view'

form 'Employee.pane',
  columnCount:3

table 'Employee-events.table',
  actionMap:'masterDetail'

propertyView('Event-text.pane',
  name:'text',
  parent:'decoratedView',
  actionMap:'binaryPropertyActionMap')

actionMap('save-reload-module-am'){
  actionList('FILE',
    actions:[
      'saveModuleObjectFrontAction',
      'reloadModuleObjectFrontAction'
    ]
  )
}

actionMap('company-module-am'){
  actionList('FILE'){
    action(ref:'saveModuleObjectFrontAction')
    action(ref:'reloadModuleObjectFrontAction')
    action(parent:'staticReportAction',
      custom:[
        reportDescriptor_ref:'Company.report'
      ]
    )
    action(parent:'chartAction',
      //description:'company.chart',
      custom:[
        chartDescriptor_ref:'Company.chart'
      ]
    )
  }
}

form 'City.module.view',
  labelsPosition:'ABOVE',
  actionMap:'save-reload-module-am',
  columnCount:1

split_Vertical('Employee.module.view',
  actionMap:'save-reload-module-am',
  top:'Employee.pane') {
  bottom {
    split_Horizontal(
      left:'Employee-events.table',
      right:'Event-text.pane',
      cascadingModels:true)
  }
}

split_Vertical 'Company.module.view',
  parent:'Company.organization.view',
  actionMap:'company-module-am'
  
messageSource(basenames:'org.jspresso.hrsample.i18n.Messages')
  