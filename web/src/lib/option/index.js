import HUIOption from '../select/src/option';

/* istanbul ignore next */
HUIOption.install = function(Vue) {
  Vue.component(HUIOption.name, HUIOption);
};

export default HUIOption;
