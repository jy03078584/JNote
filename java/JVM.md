#JVM
>摘要笔记：《深入理解Java虚拟机2》

[TOC]

##JVM类加载机制
###jvm类加载生命周期
【加载】——【验证】——【准备】——【解析】——【初始化】——【使用】——【卸载】
<hr>

###类被加载的时机

**JVM规定有且只有一下5种情况，类才会被加载**

* 使用new实例化对象、读取/设置static字段(除开final static)、调用staitc方法
* 使用java.lang.reflect对类进行反射操作时
* 初始化一个类时,若父类未初始化会先初始化父类
* 被指定为主类的类会被优先初始化(void main)
* 使用JDK1.7的java.lang.invoke.MethodHandler

**以下情况不会初始化**

* 子类调用父类的static字段只会初始化父类，并不会初始化子类:即只有直接定义static字段的类才会被初始化
* MyClass[] mc = new MyClass[10]。new 数组的方式也不会初始化类，JVM会自动生成一个额外的类
* 在B类调用A类的final static字段，JVM会在编译阶段做优化，将A中的final static字段转存到B类中，则在调用时并不会初始化A类

**类在初始化时要求父类全部初始化，接口则并只要求在使真正使用到父类接口时才会初始化**
