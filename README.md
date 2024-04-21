# happyTest
一些寻常或奇怪的小测试集合<br />
建议实现方法结合单元测试的示例来阅读<br />
有附带postman测试用的json文件，可以导入postman直接使用<br />
部分内容需要结合前端项目来实现完整流程，[点击前往前端项目](https://github.com/17lhf/vue-happy-test)<br/>
todo 的地方表示还不确定是否正确

## 已包含内容有：
### (1)multithreadSync 
**synchronized 和 @Trasational同时使用时，产生的多线程并发问题**

### (2)threadVariable 
**多线程的变量在执行中的变动影响与更新**

### (3)cryptology 
**密码学相关的库、方法使用实例**<br/>
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

### (4)unboundedWildcardsAndGenerics 
**用List&lt;?&gt;和List&lt;T&gt;来学习类型参数“&lt;T&gt;”和无界通配符“&lt;?&gt;”的区别**

### (5)exploreJVM 
**探索jvm中classLoader及双亲委托的内容**

### (6)judgeExp 
**关于mybatis的dao层传递参数时，xml里的if条件判断使用内置类型_parameter的探索**<br/>
**以及传入的数字值为0时，mybatis里的if条件判断会将其认为是‘’空字符串的问题**(有解决策略，也有规避方式)

### (7)property 
**关于优雅地读取yml配置文件中自定义的配置的实例(多种方式)**

### (8)lombokExplore 
**关于lombok的注解里的坑**（推荐最多只用lombok的getter和setter两个注解，其他的自己实现）<br />
可是，即便是使用getter和setter,因为作者在部分细节处逆规范，导致特定条件下会出问题。在本模块中也会对其进行实验

### (9)fileIO
**关于一些比较怪的文件操作及流的实例**<br />
**关于单文件/多文件通过接口上传实例**<br />
**关于CSV文件操作实例**<br />
**ZIP的压缩和解压**<br />
打包后不能用File读取jar里面的文件，否则会报错：java.io.FileNotFoundException: class path resource [xxx] cannot be resolved to absolute file path because it does not reside in the file system

### (10)subProcess
**调用子进程**

### (11)mp
**MybatisPlus的使用笔记**（包含有：updateById的字段忽略策略）  
**MybatisPlus的配置**（包含有:解决分页查询指定页码溢出时的自动处理，关于多参数时Map作为查询条件的话如何取值的问题）

### (12)uniformPackagingReturn
**统一封装应答对象**（包含有：解决返回String基本数据类型时的异常）

### (13)time
**关于时间的一些操作**

### (14)barcodImage
一些条形码图像处理的工具类(注意，需要引入新的包)<br />
**二维码图像处理工具类**

### (15)randomUtils
**一些随机生成的工具类**（应该都会比较粗糙，毕竟都是拿来作为假数据的）

### (16)sqlOperation
**一些复杂的sql操作，特别是一些统计时使用的搜索归类** <br />
- MySQL的DATE类型，在比较时会自动设置为零点零分零秒<br />
- 查询时，如果依据排序列有相同项，则mysql会随机取相同的项的数据，导致数据混乱。<br />

### (17)feignClient
**关于模拟客户端向其他服务发送请求的示例**（使用openfeign）

### (18)system
**一些关于系统的操作**
- 判定当前所处的系统环境
- 打印Spring或Spring Boot版本号
- 打印工程根目录绝对路径
- 打印当前可用于 Java 虚拟机的处理器数

### (18)math
**一些关于数学的操作（包含但不限于进制转换处理）**

### (19)textProcessing
**一些文本处理工具方法**

### (20)email
**邮箱相关示例**
简单的发送邮件、利用模板引擎（thymeleaf）构造邮件内容后发送邮件 <br />
- 若使用的是JavaMailSender, @Autowired时会爆红，但是实际运行没问题，其实这个是误报。因为这家伙必须要见到application.properties这个文件里
有它对应的配置才不会爆红，即便压根就没设置值。但是如果用的是yml弄配置，本身没问题，只是会爆红，如果不想看到爆红，
所以可以专门弄一个application.properties文件，里面放上空值的JavaMailSender相关配置。

### (21)schedules
**定时任务的cron依据配置文件设置**

### (22)objectUtils
**对象相关的一些工具方法** <br />
- 对象和Map键值对之间通过反射进行相互转换<br />
- 字符串转列表<br />

### (23)jaCoCo
**关于JaCoCo代码覆盖率的使用测试**

### (24)startRun
**关于项目启动时运行方法的实例**
事件监听器不应该运行潜在耗时的任务，因为它们默认是在同一个线程中执行。 考虑使用 ApplicationRunner 和 CommandLineRunner 代替。<br />
如果执行的时机在bean可以开始实例化前（比如BeanDefined都还没开始），请不要使用@Autowired等方式注入bean依赖项，因为你会获得null。<br />

### (25)multiThread
**关于带返回值的线程**

### (26)regexSamples
**正则表达式应用的一些实例**

### (n)others
**不知道咋归类的都丢这里** <br />
- 关于List.subList的坑的测试<br />
- 关于数组的一些测试(按数组下标取值是引用取值，如果是对象，改属性值将影响原数组的相应元素)<br />
- SerializationUtils.clone(obj); 深度拷贝，实际上此类更多时候是用于序列化和反序列化。
此用法执行速度慢，不适用于对执行速度要求高的项目。（无测试方法，CustomConfig.java中使用）<br />
- 浮点计算精度误差和strictfp的使用
- null和字符串转换的情况
- 字符串常量池（string pool）测试
- 带标签的循环（Labeled loop）（给循环命名）

### (n+1) 补充
Spring Boot文档：https://springdoc.cn/spring-boot/index.html  
**1.Linux运行jar包**<br/>
nohup java -jar xxx.jar --spring.profiles.active=prod &  <br/>
- 通过–spring.profiles.active指定不同的环境(如开发dev、测试test、生产prod等，主要看配置文件里怎么定义) <br />
- nohup （可选）表示本jar包是不挂断地运行命令，退出终端不会影响程序的运行。在默认情况下（非重定向时），会输出一个名叫 nohup.out 的文件到当前目
录下，如果当前目录的 nohup.out 文件不可写，输出重定向到 $HOME/nohup.out 文件中 <br />
- & （可选）是指在后台运行，但当用户推出(挂起)的时候，命令自动也跟着退出 <br />
两者结合就是：在后台不挂断地运行  

**2.获取在后台不断运行中的xxx.jar包的进程的进程编号**<br/>
ps aux|grep xxx.jar  

**3.立即强制停止运行在后台不断运行中的jar包进程**<br/>
kill -9 jar包对应的进程编号  

**4.使用外置配置文件（yml）**<br />
- SpringBoot外部配置配置文件，使用命令:--spring.config.location=<br/>
- =后面如果是一个文件夹，则会自动去找里面的所有yml文件，并忽略对应的jar包内的配置文件(因为有加载的优先级)
- （此时可以结合--spring.profiles.active来配置环境）。 如果是特定文件，则只会去找单个文件。<br/>
- 注意：jar包默认会优先加载同一目录底下的yml配置文件  

**5.部署相关**<br/>
①如果有涉及访问别的服务，则部署后需要进行网络通信是否能通的验证（ping或telnet）。如果访问还涉及SSL通信，
则也需要进行相应的通信测试。（当然，业务上的验证也不能遗漏）

**6.Spring Boot的Application执行**  
注意：JUnit测试时，如果使用的是@SpringBootTest @ExtendWith(SpringExtension.class) 注解，则是不会启动Tomcat和WebApplicationContext，
这意味着相关的监听器都会监听不到东西。

**7.查看Java编译后的Class文件字节码内容**
命令： javap -v xxx.class

**8. 特殊的注释标签**
- todo 说明文字：待实现的内容
- fixme 说明文字：待修复问题的内容
- xxx 说明文字 ：待商榷的内容
- &lt;editor-fold&gt; 和 &lt;/editor-fold&gt; ：idea提供的一种能够给我们的代码块进行归纳的标签

