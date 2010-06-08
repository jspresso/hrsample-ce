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
  widths:[name:2],
  description:'company.editing'

treeNode 'Department-teams.treeNode',
  rendered:'ouId',
  actionMap:'masterDetail'

treeNode 'Department-employees.treeNode',
  rendered:'name',
  actionMap:'masterDetail'

treeNode 'Company-employees.treeNode',
  rendered:'name',
  actionMap:'masterDetail'

treeNode 'Company-departments.treeNode',
  rendered:'ouId',
  actionMap:'masterDetail'

tree('Company.tree',
  rendered:'name',
  preferredWidth:200,
  preferredHeight:200,
  icon:'structure-48x48.png') {
    subTree('Company-employees.treeNode')
    subTree('Company-departments.treeNode') { subTree('Department-teams.treeNode') //subTree('Department-employees.treeNode')
    }
  }

tabs 'Company.tab.pane',
  views:['Company.pane','Company.tree','Traceable.pane']

table 'Company-departments.table',
  actionMap:'masterDetail'

table 'Department-teams.table',
  columns:['ouId','name','manager'],
  actionMap:'masterDetail'

action('addFromList',
  parent:'lovOkFrontAction') { next(parent:'addAnyToMasterFrontAction') }

listView('Team-teamMembers.list', preferredWidth:300, preferredHeight:300) {
  actionMap {
    actionList('EDIT'){
      action(parent:'lovAction',
      custom:[
      autoquery:false,
      entityDescriptor_ref:'Employee',
      initializationMapping:['company':'company'],
      okAction_ref:'addFromList'
      ]
      )
      action(ref:'removeAnyCollectionFromMasterFrontAction')
    }
  }
}

split_vertical('Departments.and.teams.view',
  cascadingModels:true,
  top:'Company-departments.table') {
    bottom {
      border (
      center:'Department-teams.table',
      east:'Team-teamMembers.list',
      cascadingModels:true
      )
    }
  }

border 'Company.organization.view',
  model:'Company',
  north:'Company.tab.pane',
  center:'Departments.and.teams.view'

image 'Employee-photo.pane',
  parent:'decoratedView',
  actionMap:'binaryPropertyActionMap'


form 'Employee.component.pane',
  columnCount:3

border('Employee.border.pane',
  center:'Employee.component.pane') {
    north {
      border {
        east {
          image model:'Employee-genderImageUrl',
          scrollable:false
        }
      }
    }
  }

split_horizontal 'Employee.pane',
  left:'Employee.border.pane',
  right:'Employee-photo.pane'

table('Employee-events.table') {
  actionMap(parents:['masterDetail']) {
    actionList('ORGANIZE') {
      action ref:'moveDownFrontAction'
      action ref:'moveUpFrontAction'
    }
  }
}

propertyView 'Event-text.pane',
  name:'text',
  parent:'decoratedView',
  actionMap:'binaryPropertyActionMap'

bean('Company.report',
  parent:'abstractReportDescriptor',
  custom:[
  reportDesignUrl:'classpath:org/jspresso/hrsample/report/Company.jasper'
  ]) {
    list('propertyDescriptors') {
      bean(class:'org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor',
      name:'title'
      )
    }
  }

bean('Company.chart',
  class:'org.jspresso.hrsample.chart.CompanyChart',
  custom:[
  url:'classpath:com/fusioncharts/FCF_Column3D.swf',
  title:'company.chart'
  ]
  )

actionMap('Company-module-am'){
  actionList('FILE'){
    action(ref:'front.module.save')
    action(ref:'front.module.reload')
    action(parent:'staticReportAction',
      custom:[
      reportDescriptor_ref:'Company.report'
      ]
      )
    action(parent:'chartAction',
      description:'company.chart',
      custom:[
      chartDescriptor_ref:'Company.chart'
      ]
      )
  }
}

form 'City.module.view',
  labelsPosition:'ABOVE',
  actionMap:'beanModuleActionMap',
  columnCount:1

split_vertical('Employee.module.view',
  actionMap:'beanModuleActionMap',
  top:'Employee.pane') {
    bottom {
      split_horizontal(
      left:'Employee-events.table',
      right:'Event-text.pane',
      cascadingModels:true)
    }
  }

border 'Company.module.view',
  parent:'Company.organization.view',
  actionMap:'Company-module-am'

messageSource(basenames:'org.jspresso.hrsample.i18n.Messages')
