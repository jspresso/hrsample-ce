mobileCompositePage('Employee.filter.view') {
  sections {

    mobileForm {
      fields {
        propertyView name: 'name'
        propertyView name: 'contact.city'
      }
    }

    mobileForm (clientTypes: ['TABLET']) {
      fields {
        propertyView name: 'birthDate'
        propertyView name: 'gender'
        propertyView name: 'ssn'
      }
    }
  }
}

mobileForm('City.filter.view') {
  fields {
    propertyView name: 'name'
    propertyView name: 'zip', clientTypes: ['TABLET']
  }
}

mobileForm('Company.filter.view') {
  fields {
    propertyView name: 'name'
    propertyView name: 'contact.city.zip'
  }
}

mobileCompositePage('City.page.editor') {
  sections {
    mobileForm(fields: ['zip', 'name'])
  }
}

mobileCompositePage('City.page.view', actionMap: 'beanModuleActionMap', editorPage: 'City.page.editor') {
  sections {
    mobileForm()
    mobileMapView(name: 'map', mapContent: 'mapContent', defaultZoom: 12)
  }
}

mobileCompositePage('Employee.page.view', actionMap: 'beanModuleActionMap', inlineEditing: true) {
  sections {
    mobileForm(labelsPosition: 'NONE') {
      fields {
        image name: 'photo', scaledWidth: 150
      }
    }
    mobileForm(excludedReading:['company']){
      fields {
        propertyView name: 'company'

        propertyView name: 'name'
        propertyView name: 'firstName'
        enumerationPropertyView name: 'gender', radio: true
        propertyView name: 'married'
       }
    }

    mobileForm(position: 'RIGHT') {
      fields {
        propertyView name: 'contact'
      }
    }

    mobileMapView(model: 'contact', name: 'map',  mapContent: 'city.mapContent', position: 'RIGHT', defaultZoom: 12)

    mobileForm(excludedWriting:['createTimestamp', 'lastUpdateTimestamp'], position: 'RIGHT') {
      fields {
        propertyView name: 'birthDate'
        propertyView name: 'age'
        propertyView name: 'ssn'

        propertyView name: 'salary'
        propertyView name: 'preferredColor'

        propertyView name: 'createTimestamp', clientTypes: ['TABLET']
        propertyView name: 'lastUpdateTimestamp', clientTypes: ['TABLET']

      }
    }

    mobileForm(labelsPosition: 'NONE') {
      fields {
        image name: 'signature', drawable:true, scaledWidth: 300, scaledHeight: 200
      }
    }
  }
}

mobileCompositePage('Company.page.view', actionMap: 'beanModuleActionMap') {
  sections {

    // Main formular
    mobileForm()
/*
    mobileTree(rendered: 'name', borderType: 'NONE',
        icon: 'structure.png') {
      subTree('Company-employees.treeNode')
      subTree('Company-departments.treeNode') {
        subTree('Department-teams.treeNode')
      }
    }
*/
/*
    mobileActionView {
      action (parent:'saveModuleObjectFrontAction')
    }
*/

    // Attachments
    mobileCompositePage (
            parent: 'IAttachmentHolder.attachements.page', position: 'RIGHT')

    // Departments
    mobileNavPage() {
      selection {
        mobileListView(model:'Company-departments', actionMap: 'masterActionMap')
      }
      nextPage {
        mobileCompositePage(actionMap:'detailActionMap') {
          sections {

            mobileForm()
            /*
            mobileRepeater(model: 'Department-teams') {
              repeat {
                mobileBorder() {
                  north {
                    mobileForm()
                  }
                  center {
                    mobileListView(model: 'Team-members', actionMap: 'masterActionMap',
                      selectionMode:'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION')
                  }
                }
              }
            }
            */
            mobileNavPage() {
              selection {
                mobileListView(model:'Department-teams', actionMap: 'masterActionMap')
              }
              nextPage {
                mobileCompositePage(actionMap:'detailActionMap') {
                  sections {
                    mobileForm()
                    mobileNavPage() {
                      selection {
                        mobileListView(model: 'Team-members', actionMap: 'masterActionMap')
                      }
                      nextPage {
                        mobileCompositePage (parent:'Employee.page.view')
                      }
                    }
                  }
                }
              }
            }

          }
        }
      }
    }

    // Employees
    mobileNavPage() {
      selection {
        //mobileListView(model: 'Company-employees', actionMap: 'masterActionMap')
        mobileRepeater(model: 'Company-employees') {
          repeat {
            mobileForm() {
              fields {
                propertyView name: 'name'
                propertyView name: 'firstName'
                enumerationPropertyView name: 'gender', radio: true
              }
            }
          }
        }
      }
      nextPage {
        mobileCompositePage (parent: 'Employee.page.view', actionMap: 'detailActionMap')
      }
    }

    // Map
    mobileMapView(model: 'contact', name: 'map', mapContent: 'city.mapContent', defaultZoom: 12,
            icon: 'classpath:org/jspresso/framework/application/images/view-48x48.png')
  }
}

/*
 * My profile
 */
mobileCompositePage('my.profile.module.page', model:'Employee') {
  sections {

    mobileForm(labelsPosition:'NONE', name:'my.informations', borderType:'TITLED', position:'LEFT',
    excludedReading:['name', 'firstName'],
    excludedWriting:['fullName']) {
      fields {
        propertyView name:'fullName'
        propertyView name:'name'
        propertyView name:'firstName'
      }
    }

    mobileForm(labelsPosition:'NONE', name:'my.phone', borderType:'TITLED', position:'RIGHT',
    excludedReading:['contact.phone'],
    excludedWriting:['contact.phoneAsHtml']) {
      fields {
        propertyView name:'contact.phoneAsHtml'
        propertyView name:'contact.phone'
      }
    }

    mobileForm(labelsPosition:'NONE', name:'my.photo', borderType:'TITLED', position:'TOP') {
      fields {
        image name:'photo'
      }
    }

    mobileForm(labelsPosition:'NONE', name:'my.signature', borderType:'TITLED', position:'BOTTOM') {
      fields {
        image name:'signature', drawable:true
      }
    }

  }
}

/**
 * Common mobile UI
 */
mobileCompositePage ('IAttachmentHolder.attachements.page', icon:'classpath:org/jspresso/framework/application/images/save_file-48x48.png', name:'attachmentsLabel',
        editorPage: 'IAttachmentHolder.attachements.edit.page') {
  sections {
    mobileListView(model: 'IAttachmentHolder-attachments',
            rendered: 'htmlMobileDescription', showArrow: true,
            rowAction: 'openAttachementAction')
  }
  actionMap {
    actionList('ACTION', renderingOptions: 'LABEL_ICON') {
      action ref: 'addPictureAttachementAction'
    }
  }
}


mobileCompositePage('IAttachmentHolder.attachements.edit.page', name:'attachmentsLabel') {
  sections {
    mobileListView(model: 'IAttachmentHolder-attachments',
            rendered: 'htmlMobileDescription', showArrow: false,
            selectionMode: 'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION',
            selectionModel: 'IAttachmentHolder-selectedAttachments')
  }
  actionMap {
    actionList('ACTION', renderingOptions: 'LABEL_ICON') {
      action (class: 'org.jspresso.hrsample.frontend.mobile.RemoveAttachmentFrontAction',
              name: 'remove.name', icon: 'classpath:org/jspresso/framework/application/images/edit_remove-48x48.svg',
              booleanActionabilityGates: ['selectedAttachments'])
    }
  }
}



/*
 * actions
 */
action('myProfileModuleInitAction',
        class: 'org.jspresso.hrsample.frontend.MyProfileModuleInitAction')

action('addPictureAttachementAction', name: 'action.takeAPicture',
        icon: 'camera.svg',
        parent: 'openFileAction') {
  custom {
    bean('fileOpenCallback',
            class: 'org.jspresso.hrsample.frontend.AddAttachmentCallBack')
  }
}