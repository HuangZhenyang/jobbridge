# Job Bridge 大学生求职服务平台

<p align="left">
	<img alt="MIT" src="https://img.shields.io/npm/l/express.svg" />
</p>

***

## 项目结构
* 采用前后端分离开发
* 前端：html + css + js, bootstrap + jquery ；插件：pdfmake.js
* 后端: spring boot
* 模板引擎: Thymeleaf
* 数据库：MySQL
* 测试: 后端采用Junit进行单元测试; 前端采用Mock.js对请求进行拦截;


## 功能特点
* 学生：
	1. 用学生邮箱进行学生身份认证; 
	2. 在线制作/修改/导出简历;
	3. 查看职海中的招聘信息,可根据条件筛选;
	4. 进入到心仪的具体招聘信息页面进行简历投递;
 	5. 查看已经投递过的职位(时间轴的方式);
	6. 可收藏职位定期获得相关招聘信息的推送;
	7. HR审阅如果批准, 会收到平台的一封提示邮件;
	8. 人脉中查看与自己可能认识的人;

* 企业：
	1. 注册,需要上传公司信息, 相关的资质证明以及公司logo等内容;
	2. 发布/修改/删除招聘信息;
	3. 查看已经发布的招聘信息列表;
	4. 查看所有的投递信息列表;
	5. 在线审阅学生投递过来的简历;
	6. 如果通过,点击"批准",平台会向学生邮箱发一封"HR已经批准,会在近期通知您"的邮件;



## 运行
1. **创建数据库：** 打开`mysql`命令行, `mysql> create database jobbridge2;`, `mysql> use jobbridge2;`, `mysql> source [path to JobBridge.sql];`
2. **修改配置文件中的数据库配置：** 打开项目中的`application.yml`文件，然后修改对应的数据库配置；
3. **运行：** 在`idea`中`open`项目文件夹， 运行即可，`localhost:8080/`

---





