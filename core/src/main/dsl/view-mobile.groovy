mobileForm('City.module.view')

mobileForm('Employee.module.view')

mobileListView('Employee.table.view')

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
