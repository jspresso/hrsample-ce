external id: ['defaultDomainIconProvider', 'refreshCardViewFrontAction']

template 'form',
    parent: 'decoratedView',
    labelsPosition: 'ABOVE',
    columnCount: 2

template 'table',
    parent: 'decoratedView'

template 'tree',
    parent: 'decoratedView'

template 'listView',
    parent: 'decoratedView'

form('Traceable.pane',
    borderType: 'NONE',
    fields: ['createTimestamp',
             'lastUpdateTimestamp'])

form('Company.pane',
    labelsPosition: 'ASIDE', borderType: 'NONE',
    widths: [name: 2]) {
  fields {
    propertyView name: 'name'
    propertyView name: 'contact.address'
    referencePropertyView name: 'contact.city', lovAction: 'lovActionWithCreate'
    propertyView name: 'contact.phone'
    propertyView name: 'contact.email'
  }
}

treeNode('Department-teams.treeNode',
    rendered: 'ouId',
    actionMap: 'masterDetailActionMap')

treeNode('Department-members.treeNode',
    rendered: 'name',
    actionMap: 'masterDetailActionMap')

treeNode('Company-employees.treeNode',
    rendered: 'name',
    actionMap: 'masterDetailActionMap')

treeNode('Company-departments.treeNode',
    rendered: 'ouId',
    actionMap: 'masterDetailActionMap')

tree('Company.tree',
    name: 'Company.tree.name',
    rendered: 'name', borderType: 'NONE',
    preferredHeight: 200,
    icon: 'structure.png',
    iconProvider: 'defaultDomainIconProvider') {
  subTree('Company-employees.treeNode')
  subTree('Company-departments.treeNode') {
    subTree('Department-teams.treeNode') //subTree('Department-employees.treeNode')
  }
}

table('decorated.translations.table', parent: 'translations.table', borderType: 'TITLED')

tabs('Company.tab.pane',
    selectFirstTab: true,
    views: ['Company.pane',
            'Company.tree',
            'IAttachmentHolder-attachments.view',
            'Traceable.pane'/*,
            'decorated.translations.table'*/],
    preferredHeight: 130)


table('Company-departments.table',
    actionMap: 'masterDetailActionMap', columnReordering: false, collapsible: false) {
  columns {
    propertyView name: 'ouId'
    propertyView name: 'teamCount'
    propertyView name: 'name'
    //propertyView name: 'manager'
    propertyView ref: 'OrganizationalUnit-manager.property'
    propertyView name: 'contact'
    propertyView name: 'createTimestamp'
    propertyView name: 'lastUpdateTimestamp'
  }
}

table('Department-teams.table',
    columns: ['ouId', 'name', 'manager'],
    actionMap: 'masterDetailActionMap')

action('addFromList',
    parent: 'lovOkFrontAction') {next(parent: 'addAnyToMasterFrontAction')}

split_vertical('Company.departments.and.teams.view',
    cascadingModels: true,
    top: 'Company-departments.table',
    permId: 'companySplit') {
  bottom {

    repeater(model: 'Department-teams') {
      actionMap {
        actionList {
          action parent: 'addToMasterFrontAction', custom: [elementEntityDescriptor_ref: 'Team']
        }
      }
      repeat {
        border {
          north {
            border(borderType: 'TITLED') {
              west {
                evenGrid(drivingCellCount: 1, drivingDimension: 'ROW') {
                  cells {
                    evenCell {actionView action: 'removeEntityCollectionFromMasterFrontAction'}
                    evenCell {actionView action: 'cloneEntityCollectionFrontAction'}
                  }
                }
              }
              center {
                form(labelsPosition: 'ABOVE', columnCount: 3, borderType: 'NONE') {
                  fields {
                    propertyView name: 'ouId'
                    propertyView name: 'name'
                    propertyView ref: 'OrganizationalUnit-manager.property'
                  }
                }
              }
            }
          }
          center {
            table(model: 'Team-members', borderType: 'NONE', horizontallyScrollable: false, preferredHeight: 120, permId: 'teamMembers.table') {
              actionMap {
                actionList('EDIT') {
                  action(parent: 'pickupAndAddAnyFrontAction', custom: [entityDescriptor_ref: 'Employee', initializationMapping: ['company': 'company']])
                  action(ref: 'removeAnyCollectionFromMasterFrontAction')
                }
              }
              columns {
                image name: 'photo', scaledHeight: 25, preferredWidth: 50
                propertyView ref: 'Employee-fullname.property'
              }
            }
          }
        }
      }

    }
  }
}

// Overiden into HRSample EE
propertyView('Employee-fullname.property')
propertyView('OrganizationalUnit-manager.property')

border('Company.organization.view',
    north: 'Company.tab.pane',
    center: 'Company.departments.and.teams.view')

image('Employee-photo.pane',
    parent: 'decoratedView',
    // actionMap: 'binaryPropertyActionMap',
    preferredWidth: 400)


form('Employee.component.pane',
    columnCount: 3,
    name: 'fullName',
    // background: 'preferredColor',
    description: 'htmlDescription',
    labelsPosition: 'ASIDE'/*,
    verticallyScrollable: true*/) {
  fields {
    propertyView name: 'name'
    propertyView name: 'firstName'
    enumerationPropertyView name: 'gender', radio: true, orientation: 'HORIZONTAL'
    propertyView name: 'birthDate'
    propertyView name: 'age'
    propertyView name: 'ssn'
    propertyView name: 'salary'
    propertyView name: 'contact'
    propertyView name: 'married'
    propertyView name: 'preferredColor'
    //propertyView name: 'photo'
    referencePropertyView name: 'company', lovAction: 'testLovAction'
    //propertyView name: 'createTimestamp'
    //propertyView name: 'lastUpdateTimestamp'
  }
}

action('testLovAction', parent: 'lovAction',
    class: 'org.jspresso.framework.application.frontend.action.lov.LovAction',
    icon: 'female.png',
    custom: ['criteriaRefiner_ref': 'testCriteriaRefiner']) {
}

bean('testCriteriaRefiner', class: 'test.TestCriteriaRefiner')


bean('employeeCardSelector', class: 'org.jspresso.hrsample.view.EmployeeCardSelector')

form('Employee.married.pane', fields: ['name', 'firstName'])

form('Employee.notmarried.pane', columnCount: 3, fields: ['firstName', 'name', 'age'])

form('Employee.filter.pane', columnCount: 3, widthResizeable: false, fields: ['firstName',
                                                                              'name',
                                                                              'gender',
                                                                              'salary',
                                                                              'birthDate'],
    collapsible: false,
    widths: ['firstName': 1, 'name': 1, 'gender': 1, 'salary': 2, 'birthDate': 2])

form('Department.filter.pane', columnCount: 8,
    fields: ['ouId',
            'name',
            'manager.gender'])

/*
border('Employee.border.pane',
    center: 'Employee.component.pane') {
  north {
    border {
      east {
        image model: 'Employee-genderImageUrl',
            scrollable: false
      }
    }
  }
  //      south {
  //        basicCardView(selector:'employeeCardSelector',
  //           views:['MARRIED':'Employee.married.pane','NOT_MARRIED':'Employee.notmarried.pane'])
  //      }
}
*/

split_horizontal('Employee.pane',
    left: 'Employee.component.pane',
    right: 'Employee-photo.pane',
    preferredHeight: 200)

table('Employee-events.table',
    selectionMode: 'SINGLE_INTERVAL_CUMULATIVE_SELECTION',
    actionMap: 'eventsTableActionMap') {

  secondaryActionMap {
    actionList(renderingOptions: 'ICON') {
      action ref: 'moveBottomFrontAction'
    }
    actionList(renderingOptions: 'LABEL_ICON') {
      action ref: 'moveDownFrontAction'
    }
    actionList(renderingOptions: 'ICON') {
      action ref: 'moveTopFrontAction'
    }
    actionList(renderingOptions: 'LABEL_ICON') {
      action ref: 'moveUpFrontAction'
    }
  }
}

actionMap('eventsTableActionMap',
    parents: ['masterDetailActionMap'])

html('Event-text.pane',
    name: 'text',
    parent: 'decoratedView',
    actionMap: 'binaryPropertyActionMap',
    booleanWritabilityGates: ['!persistent'],
    editorConfiguration: """
      {
        "menubar": false,
        "branding": false,
        "statusbar": false,
        "resize": false,
        "toolbar": "undo redo | bold italic | bullist numlist | paste | table | image media | template | print",
        "plugins": "template image media table paste print",
        "paste_data_images": true,
        "templates": "/rest/rte/templates",
        "image_list": "/rest/rte/listImages",
        "image_advtab": true,
        "images_upload_url": "/rest/rte/uploadImage",
        "automatic_uploads": true,
        "file_browser_callback": "window.richFileManager",
        "file_browser_callback_types": "file image media",
        "file_picker_types": "file image media",
        "images_reuse_filename": true,
        "disable_toolbars_with_editor": true
      }
    """
)

bean('Company.report',
    parent: 'abstractReportDescriptor',
    custom: [reportDesignUrl   : 'classpath:org/jspresso/hrsample/report/Company.jasper',
             renderedProperties: ['title']]) {
  list('propertyDescriptors') {
    bean(class: 'org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor',
        name: 'title')
  }
}

bean('Company.chart',
    class: 'org.jspresso.hrsample.chart.CompanyChart',
    custom: [url  : 'classpath:com/fusioncharts/FCF_Column3D.swf',
             title: 'company.chart'])

actionMap('Company-module-am', parents: ['beanModuleActionMap']) {
  actionList('REPORT') {
    action(parent: 'staticReportAction',
        custom: [reportDescriptor_ref: 'Company.report'])
//    action(parent: 'chartAction',
//        description: 'company.chart',
//        custom: [chartDescriptor_ref: 'Company.chart'])
//    action(parent: 'editSelectedComponentAction', custom: [viewDescriptor_ref: 'Company.dialog.view'])
//    action(class:'testDdd.TestFrontAction', icon:'company.png')
//    action(class: 'org.jspresso.framework.application.frontend.action.FrontendAction', icon: 'company.png') {
//      wrapped(class: 'test.TestAsyncAction')
//    }
  }
}

tabs('Company.dialog.view', parent: 'Company.tab.pane')

//repeater('City.module.view', actionMap:'filterableBeanCollectionModuleActionMap') {
//  repeat {
//    border (model:'City') {
//      north {
//        form (fields:['name'], borderType:'NONE', labelsPosition:'ASIDE')
//      }
//      center {
//        form (fields:['zip', 'longitude', 'latitude'], columnCount:3, borderType:'NONE', labelsPosition:'ASIDE')
//      }
//    }
//  }
//}

//tabs('City.detail.view') {
//  actionMap(parents: ['beanModuleActionMap'])
//  views {
//    form parent: 'City.view'
//    mapView parent: 'City.map'
//  }
//}

border('City.detail.view',
    actionMap: 'cityDetailActionMap', borderType: 'TITLED') {
  center {
    propertyCardView(
        property: 'cardSelector',
        permId: 'city.card.permId',
        views: ['NOMAP': 'City.view',
                'MAP'  : 'City.map.view'])
  }
}

actionMap('cityDetailActionMap')

form('City.view', labelsPosition: 'ABOVE', borderType: 'NONE',
        columnCount: 2) {
  fields {
    propertyView name: 'name'
    propertyView name: 'zip'  }
}

border('City.map.view',
        north: 'City.view') {
  center {
    mapView(name: 'map', mapContent: 'mapContent', preferredWidth:500, preferredHeight:500, defaultZoom: 12
//        ,markerAction: 'saveModuleObjectFrontAction', routeAction: 'saveModuleObjectFrontAction', zoneAction: 'saveModuleObjectFrontAction'
    )
  }
}


bean('City.card.selector',
        class: 'org.jspresso.hrsample.frontend.CityCardSelector')

/*
action('refreshCityCardFrontAction',
        parent: 'refreshCardViewFrontAction',
        custom: [cardViewId: 'city.card.permId',
                 viewPath: ['-2']])
*/




split_vertical('Employee.module.view',
    actionMap: 'beanModuleActionMap',
    top: 'Employee.pane') {
  bottom {
    tabs(preferredHeight: 400) {
      views {
        mapView(model: 'contact', name: 'map', mapContent: 'city.mapContent', defaultZoom: 12)
        split_horizontal(name: 'events', left: 'Employee-events.table', right: 'Event-text.pane', cascadingModels: true)
      }
    }
  }
}

form('Department.module.view', actionMap: 'beanModuleActionMap', columnCount: 2) {
  fields {
    propertyView name: 'ouId'
    propertyView name: 'name'
    propertyView name: 'manager', booleanWritabilityGates: ['manager.teams']
    propertyView name: 'contact'
    propertyView name: 'createTimestamp'
    propertyView name: 'lastUpdateTimestamp'
  }
}

form('Team.module.view', actionMap: 'beanModuleActionMap', columnCount: 2)

border('Company.module.view',
    parent: 'Company.organization.view',
    actionMap: 'Company-module-am')

table('City.table.view', parent: 'filterableBeanCollectionModuleView', readOnly: true)

table('Company.table.view', parent: 'filterableBeanCollectionModuleView') {
  columns {
    propertyView (name: 'name', readOnly: true) {
      actionMap {
        actionList {
          action ref: 'saveModuleObjectFrontAction'
        }
      }
    }
    propertyView name: 'contact'
    propertyView name: 'createTimestamp'
    propertyView name: 'lastUpdateTimestamp'
    propertyView name: 'contact.city'
    propertyView name: 'contact.city.zip'
    propertyView name: 'workforce'
    propertyView name: 'budget'
  }
}

table('Employee.table.view', parent: 'filterableBeanCollectionModuleView', booleanWritabilityGates: ['!persistent_cb'],
    collapsible: false, singleClickEdit: true
/*, selectionMode:'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION'*/) {
  columns {
    propertyView name: 'name', description: 'htmlDescription'
    propertyView name: 'firstName'
    propertyView name: 'gender'
    propertyView name: 'birthDate'
    propertyView name: 'age'
    propertyView name: 'ssn'
    propertyView name: 'salary'
    propertyView name: 'contact'
    propertyView name: 'married'
    propertyView name: 'preferredColor'
    image        name: 'photo', scaledHeight: 18
    propertyView name: 'company', booleanWritabilityGates: ['!persistent']
    propertyView name: 'createTimestamp', preferredHeight:-1 // Hidden by default
    propertyView name: 'lastUpdateTimestamp', preferredHeight:-1 // Hidden by default
  }
}

split_horizontal('City.module.view', cascadingModels: true) {
  left {
    table(parent: 'filterableBeanCollectionModuleView', preferredWidth:300) {
      columns {
        propertyView name: 'longitude', background: 'longitudeBackground'
        propertyView name: 'latitude', background: 'latitudeBackground'
      }
    }
  }
  right {
    border parent:'City.detail.view'
  }
}

table('IAttachmentHolder-attachments.view', horizontallyScrollable: true, readOnly: true, borderType: 'NONE') {
  actionMap {
    actionList ('ADD') {
      action ref:'addAttachementAction'
      action ref:'removeEntityCollectionFromMasterFrontAction'
    }
  }
  columns {
    propertyView name:'name', readOnly:true, action:'openAttachementAction', preferredWidth:500
    propertyView name: 'sizeToDisplay', sortable: false
    propertyView name: 'attachedBy'
    propertyView name: 'createTimestamp'
  }
}

action('addAttachementAction', name: 'action.chooseAttachement',
        parent: 'openFileAction') {
  custom {
    bean('fileOpenCallback',
            class: 'org.jspresso.hrsample.frontend.AddAttachmentCallBack')
  }
}
action('openAttachementAction',
    class: 'org.jspresso.hrsample.frontend.OpenAttachementAction',
    icon: 'classpath:org/jspresso/framework/application/images/view-48x48.png',
    name: 'action.openAttachement', description: 'action.openAttachement',
    next: 'displayUrlFrontAction')

messageSource(basenames: 'org.jspresso.hrsample.i18n.Messages')

