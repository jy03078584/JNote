# Java基础
[TOC]
## 关键字



***
### volatile
* 保证变量线程间可见性(仅仅保证变量在线程间可见性，但volatile并不能保证原子性)
    * JVM内存模型规定volatile变量修改后必须立即更新到主内存
    * JVM内存模型规定volatile变量在使用前必须先从主内存中刷新得到该变量的最新值
* 阻止JVM指令重排序(JDK1.5前 单例模式DLC不可靠原因)
```java
//单例模型 DLC方式
    class Singleton{
        private volatile static   Singleton instance;
        private Singleton(){}

        public Singleton getInstance(){
            if(instance == null){
                synchonized(Singleton.class){
                    if(instance == null){
                        //new Singleton需要3步
                        //1.内存开辟空间M;2.在M上构造Singleton [即调用构造器];3.将M内存赋值给instance变量
                        //JVM指令重排序优化可能导致2、3指令错位。多线程下则会导致instance占有了内存但并未实例化Singleton对象
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }
    } 
```


***
### transient
transient类型修饰符，用来修饰字段。用以表示被修饰的字段不会被序列化。另一种方式是实现Serializable的子接口Externalizable(该方式过于繁琐，一般都使用transient)

***
### static
- 修饰变量:静态变量被所有的对象所共享，在内存中只有一个副本，它当且仅当在类初次加载时会被初始化。

- 修饰方法:static方法一般称作静态方法，由于静态方法不依赖于任何对象就可以进行访问，因此对于静态方法来说，是没有this的，因为它不依附于任何对象
- 静态代码块:静态代码块以优化程序性能。static块可以置于类中的任何地方(除方法内部)，类中可以有多个static块。在类初次被加载的时候，会按照static块的顺序来执行每个static块，并且只会执行一次。只会在类加载的时候执行一次
- 静态变量在编译时已经确定内存位置，因此静态变量的声明和初始化顺序可颠倒
  static{ i = 0}
  private static int i;
- 静态内部类。内部类的一种。静态内部类不依赖外部类，且不能访问外部类的非static变量和方法
- 静态导包 import static com.xxx.ClassName.*  在使用静态变量和方法时不用再指明ClassName，但可读性降低

***
### final
- 修饰类:表明这个类不能被继承。final类中的成员变量可以根据需要设为final，但是final类中的所有成员方法都会被隐式地指定为final方法

- 修饰方法：第一个原因是把方法锁定，以防任何继承类修改它的含义；第二个原因是效率。在早期的Java实现版本中，会将final方法转为内嵌调用。但是如果方法过于庞大，可能看不到内嵌调用带来的任何性能提升。在最近的Java版本中，不需要使用final方法进行这些优化了，类的private方法隐式的被定义成final
- 修饰变量：
	1.修饰基本对象表示该变量值不会再改变
	2.修饰引用对象则只表示初始化后不会在指向其他对象(该对象内部属性却是可以改变的)
	3.static final修饰的变量可以在static块中初始化
- final好处：
	1.final关键字提高了性能。JVM和Java应用都会缓存final变量。
	2.final变量可以安全的在多线程环境下进行共享，而不需要额外的同步开销。
	3.使用final关键字，JVM会对方法、变量及类进行优化(例：直接初始化final变量 JVM会将该变量当作常量使用)。


***
***
## 内部类
- 成员内部类：最普通的内部类
	Outer outer = new Outer();
	Innter inner = outer.new Innter();
  内部类能无限制的访问外部类变量和方法(包括静态和private，因为在实例化内部类时会默认传递一个外部类对象作为构造函数参数)，当访问变量名有同名时默认访问内部类的变量，
  要访问外部的变量的方式：Outer.this.变量;
  外部类访问内部类则需要内部类的对象引用。
  内部类权限修饰:
  - private:只能外部类访问
  - protected:只能同一包下或者继承外部类访问
  - public:任何位置都能访问

- 局部内部类：定义在方法块或者作用域的类。只能访问方法块或作用域的变量。不能被修饰符修饰
- 匿名内部类：最常用的内部类，常用于各种监听器里面。不能被修饰符修饰。给匿名内部类传递对象时，参数必须被final修饰。
- 静态内部类：静态内部类不依赖外部类，且不能访问外部类的非static变量和方法 
- 匿名/局部类只能访问final的变量：内部类中访问的变量本质并非是原方法中的变量，而是一个拷贝。防止在多线程操作时外部类线程结束导致变量结束。但同时为了保证数据一致性因此需要用final修饰保证copy出来的变量数据可靠性。
- 每个内部类都能独立实现一个接口，能在一定程度上处理Java多继承的尴尬
	
- 内部类一些特性
```java
public class InheritInner extends OutClass.Inner{
    /**
     * 继承一个内部类时 无参的构造函数会造成编译失败 只能提供带有外部类参数的构造函数
     * 因为内部类Inner的构造器本身隐含携带一个外部类OutClass的引用this
     * @param outClass
     */
    public InheritInner(OutClass outClass) {
        outClass.super();
    }
}

class OutClass {
     public void test(){
      //局部内部类可以有构造器 这是与匿名内部类最主要的区别
        class Innder2{
            public Innder2(){};
         }
     }
     
     public IInnerInterface test2(){
         //匿名内部类
         return new IInnerInterface() {
             public void test3() {
                 System.out.println("I'm Anonymous Inner Class");
             }
         };
     }
     
     //普通内部类
    class Inner{}
    
}
interface IInnerInterface{ void test3();}

```

## 接口与抽象类
>[摘自网络:http://www.cnblogs.com/dolphin0520/p/3811437.html](http://www.cnblogs.com/dolphin0520/p/3811437.html)

* 抽象类：对事物类型的一种抽象
  * 抽象方法必须为public或者protected（因为如果为private，则不能被子类继承，子类便无法实现该方法），缺省情况下默认为public。
  * 抽象类不能用来创建对象；
  * 如果一个类继承于一个抽象类，则子类必须实现父类的抽象方法。如果子类没有实现父类的抽象方法，则必须将子类也定义为为abstract类。
* 接口：对行为的一种抽象
* 接口和抽象类区别
  * 语法区别 
    * 抽象类可以提供成员方法的实现细节，而接口中只能存在public abstract 方法；
    * 抽象类可以有构造函数，而接口没有构造函数
    * 抽象类中的成员变量可以是各种类型的，而接口中的成员变量只能是public static final类型的；
    * 接口中不能含有静态代码块以及静态方法，而抽象类可以有静态代码块和静态方法；
    * 一个类只能继承一个抽象类，而一个类却可以实现多个接口。
    * 抽象类可以有默认方法的实现，JDK8以前接口没有方法的实现
  * 设计/概念区别
    * 抽象类是对事物类型的一种抽象,更注重"是不是"
    



***
***
## 传递方式
java中只有值传递
- 原始类型直接传递值
- 对象类型传递对象引用地址的值

## String.hashCode()

 计算公式：*s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]* 【n:字符串长度】
 使用31做常量因子：31的2进制全为1，有利于离散数据。
 好处：
 - 可以节省内存，因为hash值在相邻，这样hash的数组可以比较小。
 - 值相邻，如果存放在容器，实际存放的内存的位置也相邻，则存取的效率也高。
 
><<Effective Java>>作者解释：31是一个传统,也可以不适用31,但31可以通过移位来处理乘法操作从而获得性能上的提升,虚拟机会自动做优化

## 装箱拆箱
JDK5后提供装箱拆箱功能。
装箱拆箱可简单理解为：装箱即将基本类型封装成封装类；拆箱即封装类转为基本类型
装箱的本质是调用了XXX.valueOf()  【eg:Integer.valueOf(int)】
拆箱的本质是调用了XXX.xxxValue() 【eg:Integer.intValue()】
```java
Integer i1 = 10;
Integer i2 = 10;
Integer i3 = 200;
Integer i4 = 200;
int i5 = 10;
Integer i6 = 20;
Long i7 = 20L;
//==比较时 比较双方为封装类则比对象 否则比数字.若
System.out.pringln(i1 == i2);//true
System.out.pringln(i3 == i4);//false Integer有缓存机制(JVM针对Integer在-128——127会使用缓存)
System.out.pringln(i1 == i5);//true

//若==中某一边为表达式则会自动拆箱 比数字
System.out.pringln(i6 == (i1+i2));//true

//包装器类型equals()会触发装箱
//i1+i2：拆箱后运算20--->equals对20装箱---->20L与20比较
//equals不会针对类型处理
System.out.println(i7.equals(i1+i2));//false
```


## 类型信息
*Java中能在运行时识别对象和类的信息，主要依靠两种方式：1.传统RTTI;2.反射*

* RTTI(RunTimeTypeInfo)
  * 传统RTTI是在编译阶段已经知道所有类型的情况下，“主动”的方式实现多态从而达到运行时识别对象和类的效果
  * Class对象：Java中用来表示类信息的对象。Java中就是依靠Class对象来实现RTTI的
    * 获得Class对象的方式：1：Class.forName("全类名")；2：XXX.class 【前者会初始化类，后者并不会初始化类】  
    * 创建对象:Object object = Class.newInstance(); 使用newInstance能实现"虚拟构造器"。但**使用该方法的类必须带有默认构造器**
    * Class对象能获取类的所有信息：getName()、getSimpleName()、getInterfaces()...

* Reflect(反射机制)
  * RTTI能告知一个对象的确切类型,但前提是这个类型在编译阶段必须已知。而反射则能绕过这个限制,使得Java能依靠反射机制完全在运行时确定类信息
  * Java反射提供了Field、Method、Constructor等API。是的在运行时能动态获取类信息

* RTTI和Reflect本质都是需要依赖Class对象。区别只是RTTI需要在编译阶段就打开和检查.class文件,而Reflect则是在运行时打开和检查.class文件 
    
 

## 创建对象的4种方式
* new关键字
```java
Object o = new Object()
```
* 反射机制
```java 
MyObject mo=(MyObject)Class.forName("Customer").newInstance(); 
```
* 对实现了Cloneable接口的类调用clone()克隆对象
```java
MyObject mo1 = new MyObject();
MyObject mo2 = (MyObject)mo1.clone();
```
* 对于实现了Serializable的类进行反序列化
```java
ObjectInput oinput=new ObjectInputStream(new FileInputStream(filename));
MyObject mo = (MyObject) oinput.readObject();
```

* Java 对象由3个域构成：
  * 对象头，表述 Object 当前状态的信息（普通对象占 8字节，数组占12字节）
    * Mark Word : 用于存储对象自身的运行时数据。`hashCode`,'GC分代年龄'、`锁标志`等
    
    * Class Point : 所属Class指针
    * Array Length : 如果是数组,记录数组长度
  * 对象体
  * 补足位，不足8的倍数的时候，自动补齐
    
- 利用Redis原子特性生成简单的顺序序列号
```java
private static final Format NO_SEQUENCE_FORMAT = new DecimalFormat("0000");
// 生成格式:201904150001,201904150002
private String generateNo(String keyPrefix) {
		String nowDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
		String key= keyPrefix + nowDate;
		Long sequence = getOrderSequence(key);
		return nowDate + NO_SEQUENCE_FORMAT.format(sequence);
	}

private Long getOrderSequence(String key) {
		RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		Long increment = redisAtomicLong.getAndIncrement();
		if (increment == 0) {
			//设置过期时间（最完美过期时间应为当前距当天结束剩余时间，为方便此处直接设置1天）
			redisAtomicLong.expire(1, TimeUnit.DAYS);
			increment = redisAtomicLong.getAndIncrement();
		}
		return increment;
	}
```
