# Todolist
这是一个只专注于帮助你记录、管理日常计划任务的界面简洁清爽工具类APP。

APP功能专一、操作简单、界面优美，记录只保存在本地，不上传到服务端，不需要担心隐私泄露问题。

## 功能简介
* 帮助你记录、管理日常计划任务
* 天气预报
* 其他功能如快递查询、基站查询、身份证查询、周公解梦、手机号码归属地、邮编查询等


### 项目截图


App体验：[Todolist - fir.im ](https://fir.im/tolist)


### 项目中用到的知识
* 命名规范：
  * 主要参考Blankj：[Android 开发规范（完结版） - 简书](https://www.jianshu.com/p/45c1675bec69)
  * [《阿里巴巴Java开发手册》](https://yq.aliyun.com/articles/69327)
    以及解读版：[阿里巴巴java开发手册解读1-命名规约|香辣猪蹄儿](http://yvshuo.me/blog/docs/alibaba/index.html)
  * [《阿里巴巴Android开发手册》](https://yq.aliyun.com/articles/499254)
* Java基础:
  * Date跟Calendar:[Date类学习总结(Calendar Date 字符串 相互转换 格式化)](http://blog.csdn.net/footballclub/article/details/45191061)
    以及 [关于时间，日期，星期，月份的算法（Java中Calendar的使用方法）](http://blog.csdn.net/haima573979352/article/details/14448797)
* Android基础:
  * Android基础知识复习:
     * [尚硅谷15天Android基础(复习笔记) - CSDN博客](http://blog.csdn.net/simplebam/article/details/70213167)
     * [《Android 第一行代码》](http://blog.csdn.net/guolin_blog/article/details/52032038)
  * 四大组件:
     * Activity:
        * 启动模式:[Activity的四种启动模式-图文并茂 – Android开发中文站](http://www.androidchina.net/3173.html)
        * 状态保存与恢复:[Android Activity 和 Fragment 状态保存与恢复的最佳实践](https://www.jianshu.com/p/45cc7775a44b)
        * 动画切换:[酷炫的Activity切换动画，打造更好的用户体验 - 简书](https://www.jianshu.com/p/37e94f8b6f59)
        * 标签属性:[Android Activity标签属性 - 简书](https://www.jianshu.com/p/8598825222cc)
     * PreferenceActivity:
        * [Android开发之PreferenceActivity的使用 - 简书](https://www.jianshu.com/p/4a65f4a912c6)
        * [Preference 三种监听事件说明 - wangjicong_215的博客 - CSDN博客](http://blog.csdn.net/wangjicong_215/article/details/52209380)
     * Fragment
        * [实现Activity和Fragment之前通信 - CSDN博客](http://blog.csdn.net/xuxian361/article/details/75529810)
        * [死磕 Fragment 的生命周期 - MeloDev的博客 - CSDN博客](http://blog.csdn.net/MeloDev/article/details/53406019)
        * [android fragment onHiddenChanged的使用 - CSDN博客](http://blog.csdn.net/bzlj2912009596/article/details/62851537)
           ,这里是为了解释第二篇博文准备的
        * [Fragment的setUserVisibleHint方法实现懒加载，但
          setUserVisibleHint 不起作用？ - Leevey·L - 博客园](http://www.cnblogs.com/leevey/p/5678037.html)
          ,这里是为了解释第二篇博文准备的
        * [TabLayout使用详解 - 简书](https://www.jianshu.com/p/7f79b08f5afa)
          ,这里是为了解释第二篇博文准备的
        * [套在ViewPagerz中的Fragment在各种状态下的生命周期 - CSDN博客](http://blog.csdn.net/jemenchen/article/details/52645380)
        * [Android -- Fragment 基本用法、生命周期与细节注意 - 简书](https://www.jianshu.com/p/1ff18ec1fb6b)
        * [Fragment全解析系列（一）：那些年踩过的坑 - 简书](https://www.jianshu.com/p/d9143a92ad94)
          ,这系列的四篇都很经典,建议你可以看看
        * 还不知道怎么入门解析Fragment的可以看我的面经,里面涉及了(卖个广告),
          [Android面经-基础篇(持续更新...) - CSDN博客](http://blog.csdn.net/simplebam/article/details/77989675)
        * 关于保存和恢复Fragment目前最正确的状态:[The Real Best Practices to Save/Restore Activity's and Fragment's state. (StatedFragment is now deprecated)](https://inthecheesefactory.com/blog/fragment-state-saving-best-practices/en)
  * Material Design:
    * [Android Theme.AppCompat 中，你应该熟悉的颜色属性 - 简书 ](https://www.jianshu.com/p/15c6397685a0)
      这家伙的关于MD文章也是值得一看的,简短but精辟
    * Toolbar:
        * [Android ActionBar完全解析，使用官方推荐的最佳导航栏(上)](http://blog.csdn.net/guolin_blog/article/details/18234477)
        * [ToolBar使用心得(如何改变item的位置) - 泡在网上的日子](http://www.jcodecraeer.com/plus/view.php?aid=7667)
        * [Toolbar+DrawerLayout+NavigationView使用心得](http://www.jcodecraeer.com/a/anzhuokaifa/2017/0317/7694.html)
        * [Android ToolBar 使用完全解析 - 简书]( https://www.jianshu.com/p/ae0013a4f71a)
    * CoordinatorLayout(本身就是一个加强版的FrameLayout)可以监听其所有子控件
      的各种事件,然后自动帮助我们做出最为最为合理的响应 <--(寄生) AppBarLayout
      (垂直的LinearLayout加强版),它在内部做了很多滚动事件的封装
      <--(寄生) CollapsingToolBarLayout(可折叠式标题栏)
        * CoordinatorLayout:[CoordinatorLayout与滚动的处理-泡在网上的日子](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0717/3196.html)
        * DrawLayout:
          * [android官方侧滑菜单DrawerLayout详解 - 泡在网上的日子](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/0925/1713.html)
          * [用Android自带的DrawerLayout和ActionBarDrawerToggle实现侧滑效
            果 - CSDN博客](http://blog.csdn.net/miyuexingchen/article/details/52232751)
          * [Drawer 详解 ·Material Design Part 3 - Android - 掘金](https://juejin.im/entry/5825c76d67f3560058d23657)
    * RecyclerView:
        * [RecyclerView简单使用总结 - 简书](https://www.jianshu.com/p/9b3949f7cb0f)
        * [RecyclerView使用完全指南，是时候体验新控件了（一） - 简书](https://www.jianshu.com/p/4fc6164e4709)
    * SwipeRefreshLayout:
        * [SwipeRefreshLayout详解和自定义上拉加载更多 - 简书 ](https://www.jianshu.com/p/d23b42b6360b)
        * [SwipeRefreshLayout+RecyclerView冲突的几种解决方案 - 简书](https://www.jianshu.com/p/34cbaddb668b)
    * 看不懂物料设计的话建议买郭霖先生的《第二行代码》好一点，这本书内容对于初级
      开发者来说还是蛮不错的
  * 异步消息机制:[Android异步消息处理机制完全解析，带你从源码的角度彻底理解](http://blog.csdn.net/guolin_blog/article/details/9991569)
  * Android换肤:
    * [Android 探究 LayoutInflater setFactory - CSDN博客](http://blog.csdn.net/lmj623565791/article/details/51503977)
    * [Android主题换肤 无缝切换 - 简书](https://www.jianshu.com/p/af7c0585dd5b)
    * [fengjundev/Android-Skin-Loader: 一个通过动态加载本地皮肤包进行换肤的皮肤框架](https://github.com/fengjundev/Android-Skin-Loader)
  * Android混淆:[Android混淆备忘录 - 简书 ](https://www.jianshu.com/p/a48b49e9e2a8)
   * 拍照以及选择图库:[Android 7.0调用相机拍照，返回后显示拍照照片 - CSDN博客](http://blog.csdn.net/ww897532167/article/details/71525514)
     以及 [android打开系统图库终极适配 - CSDN博客](http://blog.csdn.net/nbalichaoq/article/details/51992151)
   * 矢量图:[SVG 的 PathData 在 Android 中的使用 - CSDN博客 ](http://blog.csdn.net/zwlove5280/article/details/73196543)
     以及 [Android：获取并制作矢量图动画 - Android - 掘金](https://juejin.im/entry/5948c1ea8d6d81cc72fd1bbe)
   * 文本你还可以这样玩:[Android TextView中文字通过SpannableString来设置超链接
     、颜色、字体等属性- CSDN博客 ](http://blog.csdn.net/snowdream86/article/details/6776629)
   * 文本合成图片:[在代码中合成图片然后分享 - Android开发过程中的一些个人总结](https://segmentfault.com/a/1190000004554721)


### 项目中的用到的开源框架
* bugtags-移动时代首选 Bug 管理系统:[Bugtags 使用说明 - CSDN博客](http://blog.csdn.net/ObjectivePLA/article/details/51037804)
* Blankj/AndroidUtilCode：[终于等到你--权限工具类 - 简书](https://www.jianshu.com/p/333b09b7e000)
* ButterKnife：[[Android开发] ButterKnife8.5.1 使用方法教程总结 - CSDN博客](http://blog.csdn.net/niubitianping/article/details/54893571)
* LitePal:
   * [Android数据库高手秘籍(一)——SQLite命令 - 郭霖的专栏 - CSDN博客](http://blog.csdn.net/guolin_blog/article/details/38461239)
     以及 [LitePal 1.6.0版本来袭，数据加解密功能保障你的应用数据安全](http://mp.weixin.qq.com/s/TSp36cnKLxUmAHjT86UCrQ)
   * LitePal使用心得(这里指的是 1.6.1 版本):
     * 实体private 、public、 public final 类型,LitePal都会存储,无需生成
       getter/setter 方法(其实就是默认存储实体中所有的字段/属性,如果不想存储,
       则设置使用@Column()中的ignore 属性)
     * 字段/属性 在数据库中一律使用小写存储,所以我们在 crud 时候可以忽略字段大小
       写操作,不过在查询时候如果where(" name=? ", name),如果 name (这个字段为
       String) 为 null 就会报错 "org.litepal.exceptions.DataSupportException: the
       bind value at index 1 is null"
     * Litepal 的注解 @Column(),里面有 nullable,unique,defaultValue,ignore
       四种方法
       * unique: 比如taskId设置了unique,当存储(save()方法)时候,数据库如果已经
        有该taskId,那就不存储(save() 方法返回 false)
* Gson-解析Json数据:[你真的会用Gson吗?Gson使用指南（一） - 简书 ](https://www.jianshu.com/p/e740196225a4)
  ,这个作者写的Gson系列教程很好,值得拜读
* Rx系列
  * 实战:[rengwuxian/RxJavaSamples: RxJava 2 和 Retrofit 实战](https://github.com/rengwuxian/RxJavaSamples)
  * RxJava:
     * 目前最好的RxJava文章,没有之一:[给初学者的RxJava2.0教程(一) - 简书 ](https://www.jianshu.com/p/464fa025229e)
     * 其他RxJava文章推荐:[RxJava2 学习资料推荐](http://mp.weixin.qq.com/s/UAEgdC2EtqSpEqvog0aoZQ)
     * [RxJava之过滤操作符 - 行云间 - CSDN博客](http://blog.csdn.net/io_field/article/details/51378909)
     * [RxJava2.0你不知道的事 - 简书](https://www.jianshu.com/p/785d9dfb0a5b)
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
* Retrofit
   * [你真的会用Retrofit2吗?Retrofit2完全教程 - 简书](https://www.jianshu.com/p/308f3c54abdd)
   * [Android Retrofit 2.0使用 | 吴小龙同學](http://wuxiaolong.me/2016/01/15/retrofit/)
   * [Android Retrofit 2.0 使用-补充篇 - 简书](https://www.jianshu.com/p/93153b34310e)
* Glide
  * [Android图片加载框架最全解析（一），Glide的基本用法 - 郭霖的专栏](http://blog.csdn.net/guolin_blog/article/details/53759439)
    郭霖写的东西都很赞,值得推荐阅读
  * [Google推荐的图片加载库Glide介绍 - 泡在网上的日子](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0327/2650.html)
  * [Glide 一个专注于平滑滚动的图片加载和缓存库 - 简书](https://www.jianshu.com/p/4a3177b57949)
  * [Glide V4 框架新特性（Migrating from v3 to v4） - HeXinGen的博客 - CSDN博客](http://blog.csdn.net/hexingen/article/details/72578066)
* uCrop:[uCrop使用及源码浅析 | 吴小龙同學](http://wuxiaolong.me/2016/06/20/uCrop/)
* NewbieGuide:[推荐一个好用小巧的Android引导蒙版（浮层）库 - 简书 ](https://www.jianshu.com/p/5e80c7aee1fc)
* BaseRecyclerViewAdapterHelper:[BRVAH官方使用指南（持续更新） - 简书](https://www.jianshu.com/p/b343fcff51b0)
* FloatingActionButton:[trity1993/FloatingActionButton](https://github.com/trity1993/FloatingActionButton)
  * 其实就是悬浮button的多种选择


### Android 开发Tips && 性能优化
* Android开发Tips ：
  * [Android开发一点小技巧和建议献上 - 掘金](https://juejin.im/post/5a66bea86fb9a01caf378d33)
  * [Android 开发注意事项 - 简书](https://www.jianshu.com/p/0b40c02b6119)
* Android 性能优化：
  * [Android内存优化（使用SparseArray和ArrayMap代替HashMap） - CSDN博客 ](http://blog.csdn.net/u010687392/article/details/47809295)
  * [Android 基础1：SparseArray 和 ArrayMap （HashMap替代） - 简书 ](https://www.jianshu.com/p/38b3e72d6fea)
  * [Android布局优化之ViewStub、include、merge使用与源码分析 - CSDN博客 ](http://blog.csdn.net/bboyfeiyu/article/details/45869393)


### 开发中遇到的问题
* 下面这bug解决办法,传送门;[[译文]Android Studio 3.0 发行说明 - CSDN博客](http://blog.csdn.net/guiying712/article/details/78352062)
  ```
  Error:(42, 5) error: style attribute '@android:attr/windowEnterAnimation' not found.
  Error:(42, 5) error: style attribute '@android:attr/windowExitAnimation' not found.
  ```
  其实就是@android 前面的"@"去掉就好
* 本来这里打算使用第三方开源库作为CityPicker依赖库,但由于都不太满足需求(可能是
  数据也可能因为UI不好看等愿意),所以唯有自己找了一个网上的修改源码才得,参考的源
  代码:[yanzhenjie/AddressChecker: MD风格的地址选择器，MD风格的城市选择器，
  Android就不要按照iOS风格来设计啦！](https://github.com/yanzhenjie/AddressChecker)


### 项目中的缺点
* 本来我准备完全借鉴[WoodsHo/AbsolutePlan: AbsolutePlan ](https://github.com/WoodsHo/AbsolutePlan)
  ,但是由于这个项目实在太坑,决定放弃

* 由于本人理解换肤方面的知识没有透彻,去掉换肤功能
* 由于天气方面用了两个数据库,准备全线转LitePal
* 到时需要逐步删除readme.text多余的东西
* 后续考虑支持md语法,参考:[zeleven/mua](https://github.com/zeleven/mua)
* 做个懒加载
* 后期考虑替换Mua软件