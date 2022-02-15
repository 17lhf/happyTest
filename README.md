# happyTest
一些寻常或奇怪的小测试集合

建议实现方法结合单元测试的示例来阅读

## 已包含内容有： 
###(1)multithreadSync 
<B>synchronized 和 @Trasational同时使用时，产生的多线程并发问题</B>

###(2)threadVariable 
<B>多线程的变量在执行中的变动影响与更新</B>

###(3)cryptology 
<B>密码学相关的库、方法使用实例</B><br/>
由于其中部分地方使用到了sun.*包，所以需要注意，这类包已经是“Deprecated and restricted API”，不推荐使用<br/>
需要谨慎考虑用到这个包的几个方法，最好是用平替的方法<br/>
【官方解释】：javac uses a special symbol table that does not include all Sun-proprietary classes.
When javac is compiling code it doesn't link against rt.jar by default. Instead it uses special symbol
file lib/ct.sym with class stubs.<br/>
大意是：javac在编译时，并不引用 rt.jar，用的是一个特别的symbol table（lib/ct.sym）,这个symbol table不包含所有的sun包的类。<br/>
【具体原因】：J2SE中的类大致可以划分为以下的各个包：java.*，javax.*，org.*，sun.*;除了“sun”包，其它各个包都是Java平台的
标准实现，并且今后也将被继续支持。一般说来，“sun”之类的包并不包含在Java平台的标准中，它与操作系统相关，在不同的操作系统
（如Solaris，Windows，Linux，Mac等等）中的实现也各不相同，并且可能随着J2SE版本不定期变化。因此，直接调用“sun”包的程序代码
并不是100％的Java实现。也就是说：“sun.*”包并不是API公开接口的一部分，调用“sun”包的程序并不能确保工作在所有Java平台上，事实
上，这样的程序并不能工作在今后的Java平台上。

###(4)unboundedWildcardsAndGenerics 
<B>用List&lt;?&gt;和List&lt;T&gt;来学习类型参数“&lt;T&gt;”和无界通配符“&lt;?&gt;”的区别</B>

###(5)exploreJVM 
<B>探索jvm中classLoader及双亲委托的内容</B>

###(6)judgeExp 
<B>关于mybatis的dao层传递参数时，xml里的if条件判断使用内置类型_parameter的探索</B><br/>
<B>以及传入的数字值为0时，mybatis里的if条件判断会将其认为是‘’空字符串的问题</B>(有解决策略，也有规避方式)

###(7)ymlConfig 
<B>关于优雅地读取配置文件中自定义的配置的实例</B>

###(8)lombokExplore 
<B>关于lombok的注解里的坑</B>（推荐最多只用lombok的getter和setter两个注解，其他的自己实现）

###(9)fileIO
<B>关于一些比较怪的文件操作及流的实例</B>
