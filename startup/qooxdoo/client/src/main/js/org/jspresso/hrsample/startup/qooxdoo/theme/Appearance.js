/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

qx.Theme.define("org.jspresso.hrsample.startup.qooxdoo.theme.Appearance",
{
  extend : darktheme.theme.Appearance,
  
  appearances :
  {
    "collapsable-panel" :
    {
      style : function(states)
      {
        return {
          decorator  : "group",
          padding    : 5,
          allowGrowY : !!states.opened || !!states.horizontal,
          allowGrowX : !!states.opened ||  !states.horizontal
        };
      }
    }
  }
});