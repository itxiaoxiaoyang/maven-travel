package com.xiaoyang.test;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/20/0020 9:26
 * @description TODO
 **/
public class FU {
    public FU() {
        System.out.println("父类的构造方法。。。");
    }

    public void me(){
        this.method01();
        System.out.println(this);
    }

    public void method01(){
        System.out.println("Fu.....");
    }
}
