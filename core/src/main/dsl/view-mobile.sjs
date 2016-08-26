mobileForm('Employee.filter.view') {
  fields {
    propertyView name: 'name'
  }
}

mobileForm('City.filter.view') {
  fields {
    propertyView name: 'name'
  }
}

mobileForm('Company.filter.view') {
  fields {
    propertyView name: 'name'
  }
}

mobileCompositePage('City.page.editor') {
  sections {
    mobileForm()
  }
}

mobileCompositePage('City.page.view', actionMap: 'beanModuleActionMap', editorPage: 'City.page.editor') {
  sections {
    mobileForm()
    mobileMapView(name: 'map', longitude: 'longitude', latitude: 'latitude')
  }
}

mobileCompositePage('Employee.page.view', actionMap: 'beanModuleActionMap') {
  sections {
//    mobileForm(labelsPosition: 'NONE') {
//      fields {
//        propertyView name: 'genderImageUrl'
//      }
//    }
    mobileForm(labelsPosition: 'NONE') {
      fields {
        image name: 'photo', scaledWidth: 150
      }
    }
    mobileForm(excludedReading:['company'], excludedWriting:['createTimestamp', 'lastUpdateTimestamp']) {
      fields {
        propertyView name: 'name'
        propertyView name: 'firstName'
        enumerationPropertyView name: 'gender', radio: true
        propertyView name: 'birthDate'
        propertyView name: 'age'
        propertyView name: 'ssn'
        propertyView name: 'salary'
        propertyView name: 'contact'
        propertyView name: 'married'
        propertyView name: 'preferredColor'
        propertyView name: 'company'
        propertyView name: 'createTimestamp'
        propertyView name: 'lastUpdateTimestamp'
      }
    }
    mobileForm(labelsPosition: 'NONE') {
      fields {
        image name: 'signature', drawable:true, scaledWidth: 300, scaledHeight: 200
      }
    }
    mobileMapView(model: 'contact', name: 'map',  longitude: 'city.longitude', latitude: 'city.latitude')
  }
}

mobileCompositePage('Company.page.view', actionMap: 'beanModuleActionMap') {
  sections {
    mobileForm()
    mobileNavPage() {
      selection {
        mobileListView(model:'Company-departments', actionMap: 'masterActionMap')
      }
      nextPage {
        mobileCompositePage(actionMap:'detailActionMap') {
          sections {
            mobileForm()
            mobileBorder() {
              center {
                mobileRepeater(model: 'Department-teams') {
                  repeat {
                    mobileBorder() {
                      north {
                        mobileForm()
                      }
                      center {
                        mobileListView(model: 'Team-teamMembers', actionMap: 'masterActionMap',
                          selectionMode:'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION')
                      }
                    }
                  }
                }
              }
            }
/*
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
                        mobileListView(model: 'Team-teamMembers', actionMap: 'masterActionMap')
                      }
                      nextPage {
                        mobileCompositePage (parent:'Employee.page.view')
                      }
                    }
                  }
                }
              }
            }
*/
          }
        }
      }
    }
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
    mobileMapView(model: 'contact', name: 'map', longitude: 'city.longitude', latitude: 'city.latitude')
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

/*
 * actions
 */
action ('myProfileModuleInitAction',
  class:'org.jspresso.hrsample.frontend.MyProfileModuleInitAction')
