# Todolist
这是一个只专注于帮助你记录、管理日常计划任务的界面简洁清爽工具类APP。

APP功能专一、操作简单、界面优美，记录只保存在本地，不上传到服务端，不需要担心隐私泄露问题。

## 功能简介
* 帮助你记录、管理日常计划任务
* 天气预报
* 其他功能如快递查询、基站查询、身份证查询、周公解梦、手机号码归属地、邮编查询等


### 项目截图


App体验：


### 项目中用到的知识
* 命名规范：
  * 主要参考Blankj：[Android 开发规范（完结版） - 简书](https://www.jianshu.com/p/45c1675bec69)
  * [《阿里巴巴Java开发手册》](https://yq.aliyun.com/articles/69327)
    以及解读版：[阿里巴巴java开发手册解读1-命名规约|香辣猪蹄儿](http://yvshuo.me/blog/docs/alibaba/index.html)
  * [《阿里巴巴Android开发手册》](https://yq.aliyun.com/articles/499254)
* Android基础:
  * [尚硅谷15天Android基础(复习笔记) - CSDN博客](http://blog.csdn.net/simplebam/article/details/70213167)
  * [《Android 第一行代码》](http://blog.csdn.net/guolin_blog/article/details/52032038)
* Android换肤:
  * [Android 探究 LayoutInflater setFactory - CSDN博客](http://blog.csdn.net/lmj623565791/article/details/51503977)
  * [Android主题换肤 无缝切换 - 简书](https://www.jianshu.com/p/af7c0585dd5b)
  * [fengjundev/Android-Skin-Loader: 一个通过动态加载本地皮肤包进行换肤的皮肤框架](https://github.com/fengjundev/Android-Skin-Loader)


### 项目中的用到的开源框架
* bugtags-移动时代首选 Bug 管理系统:[Bugtags 使用说明 - CSDN博客](http://blog.csdn.net/ObjectivePLA/article/details/51037804)
* Blankj/AndroidUtilCode：[终于等到你--权限工具类 - 简书](https://www.jianshu.com/p/333b09b7e000)
* ButterKnife：[[Android开发] ButterKnife8.5.1 使用方法教程总结 - CSDN博客](http://blog.csdn.net/niubitianping/article/details/54893571)
* Gson-解析Json数据:[你真的会用Gson吗?Gson使用指南（一） - 简书 ](https://www.jianshu.com/p/e740196225a4)
  ,这个作者写的Gson系列教程很好,值得拜读
* Rx系列
  * 实战:[rengwuxian/RxJavaSamples: RxJava 2 和 Retrofit 实战](https://github.com/rengwuxian/RxJavaSamples)
  * RxJava:
     * 目前最好的RxJava文章,没有之一:[给初学者的RxJava2.0教程(一) - 简书 ](https://www.jianshu.com/p/464fa025229e)
     * 其他RxJava文章推荐:[RxJava2 学习资料推荐](http://mp.weixin.qq.com/s/UAEgdC2EtqSpEqvog0aoZQ)
     * [RxJava之过滤操作符 - 行云间 - CSDN博客](http://blog.csdn.net/io_field/article/details/51378909)
  * RxLifecycle:[解决RxJava内存泄漏（前篇）：RxLifecycle详解及原理分析 -
    CSDN博客](http://blog.csdn.net/mq2553299/article/details/78927617)
    * 其中 RxLifecycle 已经推荐使用 AutoDispose :[Android架构中添加
      AutoDispose解决RxJava内存泄漏 - CSDN博客](http://blog.csdn.net/mq2553299/article/details/79418068)
  * RxPermissions:[使用RxPermissions（基于RxJava2） - CSDN博客](http://blog.csdn.net/u013553529/article/details/68948971)
    * 关于Android权限知识详解:
      * Android自定义权限与使用
        * [Android自定义权限与使用 - CSDN博客 ](http://blog.csdn.net/u014088294/article/details/51924223)
      * Android 运行时权限功能 - 针对6.0 / Android M 以上系统而言
        * 作用:用户不需要在安装软件的时候一次性授权所有申请的权限，而是可以在软
          件的使用过程中再对某一项权限申请进行授权。比如说一款相讥应用在运行时申
          请了地理位置定位权限，就算我拒绝了这个权限，但我应该依然可以使用这个应
          用的其他功能，而不是像之前那样直接无法安装它。
        * 运行时权限的核心：说白了就是在程序运行过程中由用户授权我们去执行某些危
          险操作，程序是不可以擅自做主去执行这些危险操作的。注意:我们在进行运行
          时权限处理时使用的是权限名，但是一旦用户同意授权了，那么该权限所对应的
          权限组的其他权限也同时被授权。
        * 基础篇：[《Android 第一行代码》](http://www.ituring.com.cn/book/1841)
          里面第7章的开头，其对应的源码：[RuntimePermissionTest](https://github.com/guolindev/booksource/tree/master/chapter7/RuntimePermissionTest)
          或者 [Android 6.0权限管理及最佳实践 - 简书](https://www.jianshu.com/p/cdcbd3038902)
        * 全面篇：[Android 6.0 运行时权限处理完全解析 - CSDN博客 ](http://blog.csdn.net/lmj623565791/article/details/50709663)
        * 实战篇：[Android权限管理详解 - CSDN博客](http://blog.csdn.net/shangmingchao/article/details/70312824)
        * 补充篇：[关于Android 6.0 运行时权限 | Android Notes ](https://bxbxbai.github.io/2016/05/27/android-runtime-permission/)
                 以及 [targetSdkVersion 23以下6.0中调用checkSelfPermission的问题 - 海阔天空玩世不恭](https://my.oschina.net/u/990728/blog/549914)
* Glide
  * [Android图片加载框架最全解析（一），Glide的基本用法 - 郭霖的专栏](http://blog.csdn.net/guolin_blog/article/details/53759439)
    郭霖写的东西都很赞,值得推荐阅读
  * [Google推荐的图片加载库Glide介绍 - 泡在网上的日子](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0327/2650.html)
  * [Glide 一个专注于平滑滚动的图片加载和缓存库 - 简书](https://www.jianshu.com/p/4a3177b57949)
  * [Glide V4 框架新特性（Migrating from v3 to v4） - HeXinGen的博客 - CSDN博客](http://blog.csdn.net/hexingen/article/details/72578066)
* uCrop:[uCrop使用及源码浅析 | 吴小龙同學](http://wuxiaolong.me/2016/06/20/uCrop/)
* 拍照以及选择图库:[Android 7.0调用相机拍照，返回后显示拍照照片 - CSDN博客](http://blog.csdn.net/ww897532167/article/details/71525514)
   以及 [android打开系统图库终极适配 - CSDN博客](http://blog.csdn.net/nbalichaoq/article/details/51992151)


### Android 开发Tips && 性能优化
* Android开发Tips ：
  * [Android开发一点小技巧和建议献上 - 掘金](https://juejin.im/post/5a66bea86fb9a01caf378d33)
  * [Android 开发注意事项 - 简书](https://www.jianshu.com/p/0b40c02b6119)
* Android 性能优化：
  * [Android内存优化（使用SparseArray和ArrayMap代替HashMap） - CSDN博客 ](http://blog.csdn.net/u010687392/article/details/47809295)
  * [Android 基础1：SparseArray 和 ArrayMap （HashMap替代） - 简书 ](https://www.jianshu.com/p/38b3e72d6fea)



### 后续计划
* 将数据库改为LitePal 进行存储
* 重构MainActivity代码,现在写的很想狗屎一样
* 去掉换肤功能