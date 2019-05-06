//自定义校验规则：phoneFmt
/*
* 使用步骤：
*    $.validator.addMethod()
* */
/*
* $.validator(rulename,fn,msg)
* 1、 rulename:校验规则的名称
* 2、 fn：实现具体校验逻辑的方法
* 3、msg：设置校验不通过时的错误提示信息
* */
$.validator.addMethod("phoneFmt",function (value,element,param) {
    /*
    * 1、value：获取用户在被校验的标签中输入的value值；
    * 2、element：被校验的标签的对象
    * 3、param：使用校验规则时，传入的值
    * */
    //书写校验逻辑

    // console.log(value+"  "+element+"   "+ param);
    if(param) {
        //对手机号合法性校验：正则
        //1、创建正则对象：
        /*
        * 方式一：              new RegExp(" 正则  ")
        * 方式二： 字面量       /正则表达式/
        * */
        // var phoneReg = new RegExp("^1[3456789]\\d{9}$");
        // var phoneCheckFlag = phoneReg.test(value);
        //
        // if(phoneCheckFlag){
        //     //手机号合法：return true
        //     return true;
        // }

        return new RegExp("^1[3456789]\\d{9}$").test(value);

    }

},"手机号不合法！");