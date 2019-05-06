$.validator.addMethod("emailFmt",function (value,element,param) {
    if(param) {
        return new RegExp("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").test(value);
    }
},"请输入正确的邮箱！");