mobileCompositePage('Employee.module.view') {
  sections {
    mobileForm(model: 'Employee', labelsPosition:'NONE') {
      fields {
        propertyView name: 'genderImageUrl'
      }
    }
    mobileForm(model: 'Employee') {
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
        propertyView name: 'photo'
        propertyView name: 'company'
        propertyView name: 'createTimestamp'
        propertyView name: 'lastUpdateTimestamp'
      }
    }
  }
}

mobileCompositePage('Company.module.view') {
  sections {
    mobileForm()
    mobileNavPage {
      selection {
        mobileListView(model:'Company-departments', actionMap:'masterActionMap')
      }
      nextPage {
        mobileCompositePage(actionMap:'detailActionMap') {
          sections {
            mobileForm()
            mobileNavPage {
              selection {
                mobileListView(model:'Department-teams', actionMap:'masterActionMap')
              }
              nextPage {
                mobileCompositePage(actionMap:'detailActionMap') {
                  sections {
                    mobileForm()
                    mobileNavPage {
                      selection {
                        mobileListView(model: 'Team-teamMembers', actionMap: 'masterActionMap')
                      }
                      nextPage {
                        mobileCompositePage {
                          sections {
                            mobileForm()
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
      }
    }
    mobileNavPage {
      selection {
        mobileListView(model: 'Company-employees', actionMap: 'masterActionMap')
      }
      nextPage {
        mobileCompositePage(actionMap: 'detailActionMap') {
          sections {
            mobileForm()
          }
        }
      }
    }
  }
}
