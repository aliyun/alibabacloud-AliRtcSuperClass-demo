<template>
  <li class="hui-option" @click.stop="selectOptionClick">
    <span>{{ label }}</span>
  </li>
</template>

<script>
  export default {
    name:"HuiOption",
    props: ['label','value'],
    mounted(){
       
    },
    data() {
      return {
      }
    },
    methods:{
      selectOptionClick(){
        // console.log(this.label,);
        this.$parent.setVal({label:this.label,value:this.value});
        // this.$emit("optionVal",this.label);
      }
    }
  }
</script>