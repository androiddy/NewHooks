# NewHooks
基于YAHFA和dexposed 结合的hook框架 支持安卓4.0-7.0

注:（app目录是demo，hookinject是apt代码，dexposed是框架）


2017-4-19 16:20    1.优化内部返回值转换逻辑；  2.优化apt自动生成代码匹配hook无返回值处理逻辑；

2017-4-18 16:35    1.优化内部hook参数处理逻辑,修复参数错乱hook失败问题；  2.新增DexLoaderReplace方法用于替换内存中的dex(实验)； 3.修复细节


2017-4-17 02:48    1.优化apt自动生成代码自动匹配参数类型方式内存错乱；  2.纠正hook回调机制； 3.修复小细节

2017-4-16 16:22    1.优化apt自动生成代码，修复某些hook方法(返回值为8种基本类型的方法)无法修改返回值问题； 2.优化注解配合apt生成代码 新增returnVal参数需要手动输入需要hook方法的返回值（具体使用方式请查看demo）


2017-4-15 16:21    1.优化YAHFA部分hook回调基于apt(结合dexposed回调共用一套)： 2.去除HookMethod｜OriginalHookMethod 注解：  3.删除原有hook方式,具体使用方式请查看demo


2017-4-14 14:23    1.优化HookMethod和OriginalHookMethod注解,去除参数由框架自动获取；   2.新增多hook方法(传入多个绑定Hook或者Hooks注解的类.class即可，返回List)


2017-4-14 13:31：  1.新增支持安卓5.0Hook：  2.纠正5.0Hook判断逻辑

2017-4-14 01:09：  1.新增支持安卓7.0Hook(实验)；  2.完善小细节；

2017-4-13 22:03：  1.修复某些类获取Name为null的问题；  2.完善hook返回信息；
 
2017-4-13 01:06：  1.纠正5.1art模式判断逻辑；
 
2017-4-12 24:56：  1.新增注解Hooks；  2.过时非注解hook方式；  3.优化Hook|Hooks注解type参数去除返回值参数，由框架自动获取；
  
2017-4-12 19:20：  1.新增注解方式Hook；  2.完善hook返回信息；  3.纠正hook过程判断逻辑；
 
