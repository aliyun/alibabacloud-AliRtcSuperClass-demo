import Vue from 'vue';
import main from './src/main.vue';
let MyMessageConstructor = Vue.extend(main);
let instance;
let MyMessage = function (message, type = 'info') { //自定义传入的参数
  console.log(message);
  if (!instance) {
    instance = new MyMessageConstructor({
      el: document.createElement('div'),
      data: {
        type: type,
        message: message
      }
    })
  }
  instance.action = '';
  // 挂载实例
  instance.$mount();
  document.body.appendChild(instance.$el)
  return instance;
};

['success', 'warning', 'info', 'error'].forEach(type => {
  MyMessage[type] = message => {
    return MyMessage(message,type);
  };
});
export default MyMessage;