mobileForm('City.module.view')

mobileForm('Employee.module.view')

mobileListView('Employee.table.view')

mobileCompositePage('Company.module.view') {
  sections {
    mobileForm()
  }
}
