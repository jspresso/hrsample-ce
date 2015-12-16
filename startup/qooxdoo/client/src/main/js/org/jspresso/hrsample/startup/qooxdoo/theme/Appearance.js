/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

qx.Theme.define("org.jspresso.hrsample.startup.qooxdoo.theme.Appearance",
{
  extend : org.jspresso.framework.theme.Appearance,

  appearances :
  {
    "workspace-tree-folder/icon": {
      base: true,
      style: function (states) {
        return {
          height: 0,
          width:0,
          margin: [30, 0, 0, 20]
        };
      }
    },

    "accordion-section/bar": {
      base: true,
      style: function (states) {
        return {
          show: "label"
        };
      }
    }

  }
});
