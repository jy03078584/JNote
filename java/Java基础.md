#Java基础
[TOC]
##关键字



***
###volatile


***
###transient
transient类型修饰符，用来修饰字段。用以表示被修饰的字段不会被序列化。另一种方式是实现Serializable的子接口Externalizable(该方式过于繁琐，一般都使用transient)

***
###static
- 修饰变量:静态变量被所有的对象所共享，在内存中只有一个副本，它当且仅当在类初次加载时会被初始化。

- 修饰方法:static方法一般称作静态方法，由于静态方法不依赖于任何对象就可以进行访问，因此对于静态方法来说，是没有this的，因为它不依附于任何对象
- 静态代码块:静态代码块以优化程序性能。static块可以置于类中的任何地方(除方法内部)，类中可以有多个static块。在类初次被加载的时候，会按照static块的顺序来执行每个static块，并且只会执行一次。只会在类加载的时候执行一次
- 静态变量在编译时已经确定内存位置，因此静态变量的声明和初始化顺序可颠倒
  static{ i = 0}
  private static int i;
- 静态内部类。内部类的一种。静态内部类不依赖外部类，且不能访问外部类的非static变量和方法
- 静态导包 import static com.xxx.ClassName.*  在使用静态变量和方法时不用再指明ClassName，但可读性降低

***
###final
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
##内部类
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
- 匿名内部类：最常用的内部类，常用于各种监听器里面。不能被修饰符修饰
- 静态内部类：静态内部类不依赖外部类，且不能访问外部类的非static变量和方法 
- 匿名/局部类只能访问final的变量：内部类中访问的变量本质并非是原方法中的变量，而是一个拷贝。防止在多线程操作时外部类线程结束导致变量结束。但同时为了保证数据一致性因此需要用final修饰保证copy出来的变量数据可靠性。
- 每个内部类都能独立实现一个接口，能在一定程度上处理Java多继承的尴尬
	

***
***
##传递方式
java中值传递/引用传递在于看待方式

- 对象就是传引用
- 原始类型就是传值
- String，Integer, Double等immutable类型因为没有提供自身修改的函数，每次操作都是新生成一个对象，所以要特殊对待。可以认为是传值。
- Integer 和 String一样。保存value的类变量是final属性，无法被修改，只能被重新赋值／生成新的对象。 当Integer 做为方法参数传递进方法内时，对其的赋值都会导致 原Integer 的引用被 指向了方法内的栈地址，失去了对原类变量地址的指向。对赋值后的Integer对象做得任何操作，都不会影响原来对象。


##String.hashCode()

 计算公式：*s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]* 【n:字符串长度】
 使用31做常量因子：31的2进制全为1，有利于离散数据。
 好处：
 - 可以节省内存，因为hash值在相邻，这样hash的数组可以比较小。
 - 值相邻，如果存放在容器，实际存放的内存的位置也相邻，则存取的效率也高。


##装箱拆箱
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

##创建对象的4种方式
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

