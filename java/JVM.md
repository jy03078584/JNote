#JVM
>摘要笔记：《深入理解Java虚拟机2》

[TOC]


##jvm类加载生命周期
【加载】——【验证】——【准备】——【解析】——【初始化】——【使用】——【卸载】

###[加载]
* 通过类的全限定类名获取二进制字节流
* 将字节流代表的静态存储结构转化为方法区的运行时数据结构
* 在内存中生成java.lang.Class对象,作为方法区这个累的访问入口

**对于数组对象，数组对象本身不依靠类加载器加载，但数组的元素对象则需要类加载器加载**

###[验证]
>验证阶段能确保Class文件中的字节流符合JVM规范

* 文件格式验证:
验证字节流是否符合Class文件格式的规范【是否以魔数开头、主次版本号是否在JVM处理范围类...】
* 元数据验证:
验证是否符合Java语言规范【是否有父类、子类是否继承了final修饰物、是否实现了接口的所有方法...】
* 字节码验证:
验证字节码定义的程序合法、符合逻辑【跳转指令不会跳转到方法体外、存取类型是否一致...】
* 符号引用验证


**验证阶段不是必须的,对于已经使用多次能确保安全准确的代码可以考虑使用 -Xverify:none关闭验证**


###[准备]
- 准备阶段为static变量(*实例变量实例化时在堆分配*)分配内存并设置初始值(只设置初始值并不完成赋值操作)

```java
//准备阶段 value被分配内存 并初始化为0 到【初始化】阶段才会赋值为123
public static int value = 123;

//对于被final修饰的变量 才会在【准备】阶段被赋值为123
public final static int value = 123;
```

###[解析]
- 类和接口解析 
- 字段解析
- 类方法(static)解析
- 接口方法解析


###[初始化]
【初始化】阶段是加载的最后阶段，是执行client()方法的过程,该过程中client()会收集类变量(static)和static代码块中语句对类变量进行赋值。

+ static块中语句能对变量的赋值没有顺序要求，**但只不能访问定义在static语句块后的变量**
```java
 static{
	i = 0;//static中能对定义在static块后的变量赋值
	System.out.println(i);//报错：static不能访问定义在static块后的变量
}
public static int i = 10;
```

##类加载器ClassLoader
* JVM依靠类加载器(ClassLoader)完成"通过一个类的全限定名来获取2进制字节流"的工作
* 两个类是否相等：前提必须是在同一个ClassLoader加载的类
* JVM类加载器采用双亲委派模型

  - 双亲委派模型能保证Java程序的稳定性，安全性。避免加载恶意代码
  - 实现双亲委派模型的代码在ClassLaoder.loadClass中实现。在实现自己的ClassLoader时为避免破坏双亲委派机制，JDK不提倡直接复写loadClass(),而是复写findClass()。
```java
  protected Class<?> loadClass(String name, boolean resolve)throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // 首先检查该类是否被父类加载过
            Class c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                	//父类加载该类抛出异常 则表明父类无法加载 
                }

                if (c == null) {
                    long t1 = System.nanoTime();

                    //无法被父类加载 则调用自己的findClass去加载该类
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
```

* "破坏双亲委派模式"，双亲委派的加载模式能让JVM更加安全稳定，但有些场景下需要破坏这种模式。典型的Spring中类加载器采用的是线程上下文类加载器(Thread Context ClassLoader),通过这种方式Spring能使用子类构加载器加载类。
```java
//web项目中默认为WebAppClassLoader
//因此无论Spring Jar放在Tomcat包下 还是web项目自己路径下 使用的都是同一个类加载器 
//《深入理解Java虚拟机》第9章问题
 ClassLoader ccl = Thread.currentThread().getContextClassLoader();
```


* JVM类加载器的种类
  1. 启动类加载器(Bootstrap ClassLoader)：负责启动<JAVA_HOME>下能被JVM识别的类
  2. 扩展类加载器(Extension ClassLoader): 负责加载ext下的类库 [可以被Developer扩展使用]
  3. 程序类加载器(Application ClassLoader): 因为是getSystemClassLoader()的返回对象，也称系统类加载器，负责加载程序ClassPath下的类库。程序中默认使用的就是该类加载器





<hr>

##类被加载的时机

**JVM规定有且只有以下5种情况，类才会被加载**

* 使用new实例化对象、读取/设置static字段(除开final static)、调用staitc方法
* 使用java.lang.reflect对类进行反射操作时
* 初始化一个类时,若父类未初始化会先初始化父类
* 被指定为主类的类会被优先初始化(void main)
* 使用JDK1.7的java.lang.invoke.MethodHandler

**以下情况不会初始化**

* 子类调用父类的static字段只会初始化父类，并不会初始化子类:即只有直接定义static字段的类才会被初始化
* MyClass[] mc = new MyClass[10]。new 数组的方式也不会初始化类，JVM会自动生成一个额外的代表数组的类
* 在B类调用A类的final static字段，JVM会在编译阶段做优化，将A中的final static字段转存到B类中，则在调用时并不会初始化A类

**类在初始化时要求父类全部初始化，接口则要求真正使用到父类接口时才会初始化**

<hr>
##JVM字节码执行引擎

###运行时栈帧结构
栈帧是用于支持JVM进行方法调用和方法执行的数据结构，是运行时数据区中虚拟机栈的元素。每个方法的调用返回都对应一个栈帧的进栈和出栈过程。
栈帧包括：

* 局部变量表：
  存放方法参数和方法内部定义的变量。局部变量表大小在编译阶段就已经确定，不受运行时影响。
* 操作树栈：
  提供方法执行过程中JVM操作指令存放/提取内容的场所。与局部变量一样在编译阶段大小就已确定
* 动态连接
* 方法返回
* 附加信息