# 知识分享小程序开发

## 一. 技术栈

- 后端（微服务）
  - Nacos 服务发现与注册
  - ribbon 负载均衡
  - feign 声明式服务调用
  - RocketMQ 消息推送
  - zipkin 数据追踪
  - gateway 网关
  - JWT token 设计
  - tk-mybatis 数据持久化
- 前端（uni-app）
  - uni 组件库
  - 网络请求的封装

## 二. 功能和效果图

- 普通用户

  - 微信授权登录

  ![授权登录](https://uploader.shimo.im/f/ppYvQK7u42DpDEvn.png!thumbnail)

  - 签到加积分

  ![签到加积分](https://uploader.shimo.im/f/sSKaALZAsl9LRYat.png!thumbnail)

  - 投稿

  ![投稿](https://uploader.shimo.im/f/Y0y7SQUR2GDdMtmu.png!thumbnail)

  - 查看分享

  ![分享列表](https://uploader.shimo.im/f/aSvMuTmEYYdaVnyD.png!thumbnail)

  - 兑换

  ![兑换](https://uploader.shimo.im/f/j1cSraxmze4hFoz6.png!thumbnail)

  - 下载

  ![下载](https://uploader.shimo.im/f/BnKi5EABTyfevMNo.png!thumbnail)

  - 下拉刷新

  ![下拉刷新](https://uploader.shimo.im/f/duYFJ15OHXhZPVtt.png!thumbnail)

  - 触底加载

  ![触底加载](https://uploader.shimo.im/f/utCPPqOTppeLgLaM.png!thumbnail)

  - 兑换详情

  ![兑换详情](https://uploader.shimo.im/f/tUAZAAlDNhhQVw3N.png!thumbnail)

  - 积分详情

  ![积分详情](https://uploader.shimo.im/f/nuBAAfDLAvq1X7OU.png!thumbnail)

  - 我的投稿

  ![我的投稿](https://uploader.shimo.im/f/y3RZFpIu3knjMAhc.png!thumbnail)

- 管理员用户

  - 审核投稿

  ![审核投稿](https://uploader.shimo.im/f/8LoZ11aKILOYuaEg.png!thumbnail)
  ![审核投稿](https://uploader.shimo.im/f/8LoZ11aKILOYuaEg.png!thumbnail)

## 三. 体会

- 使用微服务，将功能模块的分工体现的更加详细
- 最大的体会就是微服务之间的调用好神奇
- 对于这些新技术的理解和认知
  - Nacos 的出现，使得服务之间有了归属
  - Feign 的出现，成为服务之间通信的桥梁
  - Ribbon 负责服务间的任务分配
  - RocketMQ 做消息的推送
  - zipkin 将数据进行追踪研究
  - 网关进行过滤操作
- 重新学习了 uni-app 的 vue 般操作
- Hutool 工具的再次使用
- 微服务的调用和设计真的很棒，就是我的 ECS 远程环境有些限制了网速，影响了一点体验感
- 最近本地启动项目有些多，电脑有些吃不消的样子
- 不管在接口的设计还是统一处理的操作方面都收获满满，尤其在前端看到各种的统一处理操作就很爽，后端封装自己的方法也很满足

