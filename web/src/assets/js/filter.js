import Vue from "vue";

Vue.filter("roleNameFilter", (role) => {
  switch (role) {
    case 0:
      return "老师"
    case 1:
      return "学生"
    case 2:
      return "助教"
  }
})