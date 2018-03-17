drop table if exists resume_send;
drop table if exists star_tag;
drop table if exists star_company;
drop table if exists review;
drop table if exists inform_interview;
drop table if exists recruit_tag;
drop table if exists recruit;
drop table if exists tag;
drop table if exists company;
drop table if exists student_detail;
drop table if exists resume;
drop table if exists student;

create table student(
	student_id  				BIGINT(20) AUTO_INCREMENT,
	user_name  					VARCHAR(20),
	mailbox  					VARCHAR(30),
	password  					VARCHAR(200),
	identity					VARCHAR(2),
	primary key(student_id)				
);
create table resume(
	resume_id 					BIGINT(20) AUTO_INCREMENT,
	student_id  				BIGINT(20),
	resume_content				VARCHAR(5000),
	primary key(resume_id),
	foreign key(student_id) references student(student_id)
);

create table student_detail(
	student_id  				BIGINT(20),
	student_mailbox  			VARCHAR(30),
	phone_num  					CHAR(11),
	university_name  			VARCHAR(30),
	major  						VARCHAR(25),
	grade  						VARCHAR(10),
	intention_city  			VARCHAR(300),
	intention_industry  		VARCHAR(300),
	intention_function  		VARCHAR(300),
	authentication 				BOOLEAN,
	primary key(student_id),
	foreign key(student_id) references student(student_id)
);
create table company(
	company_id  				BIGINT(20) AUTO_INCREMENT,
	user_name  					VARCHAR(20),
	name  						VARCHAR(30),
	mailbox  					VARCHAR(30),
	phone_num  					VARCHAR(30),
	password  					VARCHAR(200),
	company_introduction  		VARCHAR(500),
	icon_address  				VARCHAR(150),
	identity					VARCHAR(2),
	primary key(company_id)
);
create table tag(
	tag_id  					int AUTO_INCREMENT,
	name  						VARCHAR(20),
	primary key(tag_id)
);
create table recruit(
	recruit_id  				BIGINT(20) AUTO_INCREMENT,
	company_id  				BIGINT(20),
	job_name  					VARCHAR(20),
	job_describe  				VARCHAR(500),
	job_require  				VARCHAR(500),
	location  					VARCHAR(50),
	low_salary  				int,
	high_salary   				int,
	date_time  					timestamp,
	deadline  					VARCHAR(30),
	have_delete  				BOOLEAN,
	primary key(recruit_id),
	foreign key(company_id) references company(company_id)
);
create table recruit_tag(
	recruit_id  				BIGINT(20),
	tag_id  					int,
	primary key(recruit_id,tag_id),
	foreign key(recruit_id) references recruit(recruit_id),
	foreign key(tag_id) references tag(tag_id)
);


create table inform_interview(
	inform_interview_id  		BIGINT(20) AUTO_INCREMENT,
	company_id  				BIGINT(20),
	student_id  				BIGINT(20),
	content  					VARCHAR(500),
	date_time  					timestamp,
	primary key(inform_interview_id),
	foreign key(company_id) references company(company_id),
	foreign key(student_id) references student(student_id)
);
create table review(
	review_id					BIGINT(20),
	company_id  				BIGINT(20),
	student_id  				BIGINT(20),
	job_name  					VARCHAR(20),
	content  					VARCHAR(500),
	date_time  					timestamp,
	primary key(review_id),
	foreign key(company_id) references company(company_id),
	foreign key(student_id) references student(student_id)
);
create table star_company(
	student_id  				BIGINT(20),
	company_id  				BIGINT(20),
	primary key(company_id, student_id),
	foreign key(student_id) references student(student_id),
	foreign key(company_id) references company(company_id)
);
create table star_tag(
	student_id  				BIGINT(20),
	tag_id  					int,
	primary key(tag_id, student_id),
	foreign key(student_id) references student(student_id),
	foreign key(tag_id) references tag(tag_id)
);
create table resume_send(
	resume_send_id  			BIGINT(20) AUTO_INCREMENT,
	resume_id  					BIGINT(20),
	company_id  				BIGINT(20),
	recruit_id  				BIGINT(20),
	date_time  					timestamp,
	have_read  					BOOLEAN,
	have_delete  				BOOLEAN,
	primary key(resume_send_id),
	foreign key(resume_id) references resume(resume_id),
	foreign key(company_id) references company(company_id),
	foreign key(recruit_id) references recruit(recruit_id)
);


insert into student
	values(1,'syunk','408271272@qq.com','3277E8DD42812340F56B2997A9C6E801F9AE2C5DAA954DED676C6F1B8297A879B75185943D90B146C07401A0EBF57543533C646C0AB40D9C3035454757B92FA5CF278EE04F8D634D5E984371425E9179','s'),
	(2,'admin','','E228C3B57EA3A4DD9FBB68D9ACEDA997F8BD1C4E7230B1016E3341B39D2FE6EF3A1178F0482C8FE2F9F9C5B4B2395B19E56D6BA8617828E79FC1773FC5DF40DC30DBEDCA387C431929C646D69B2F9F7A','a');

insert into company
	values(1,'baidu','百度','mbaidu@baidu.com','86-10-5992 8888','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'百度是一家持续创新的,以"用科技让复杂世界更简单"为使命的高科技公司','/img/comlogo/baidu.png','e'),
	(2,'aiqiyi','爱奇艺','maiqiyi@aiqiyi.com','86-10-8000 6459','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'爱奇艺是一家为了视频事业而献身的伟大公司','/img/comlogo/aiqiyi.png','e'),
	(3,'4399','4399','m4399@4399.com','86-10-4566 8885','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'4399是中国最早的和领先的在线休闲小游戏平台，从建立至今，4399坚持的是用户第一，以"用户体验"为核心的建站模式，免费为用户提供各种绿色、安全、健康的游戏，不断完善服务策略，赢得了众多忠实的用户',
		'/img/comlogo/4399.png','e'),
	(4,'ebay','易贝','mebay@ebay.com','86-10-5864 6946','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'eaby是一个管理可让全球民众上网买卖物品的线上拍卖及购物网站。ebay于1995年9月4日由Pierre Omidyar以Auctionweb的名称创立于加利福尼亚州圣荷西。人们可以在ebay上通过网络出售商品','/img/comlogo/ebay.png','e'),
	(5,'IBM','IBM','mibm@ibm.com','86-10-8889 6465','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'IBM（国际商业机器公司）或万国商业机器公司，简称IBM（International Business Machines Corporation）。总公司在纽约州阿蒙克市','/img/comlogo/IBM.png','e'),
	(6,'TCL','TCL','mtcl@tcl.com','86-10-6494 4646','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'TCL集团股份有限公司创立于1981年，是全球化的智能产品制造及互联网应用服务企业集团。集团现有8万多名员工，23个研发机构，21个制造基地，在80多个国家和地区设有销售机构，业务遍及全球160多个国家和地区','/img/comlogo/TCL.pgn','e'),
	(7,'alibaba','阿里巴巴','malibaba@alibaba.com','86-10-4564 7789','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'阿里巴巴网络技术有限公司（简称：阿里巴巴集团）是以曾担任英语教师的马云为首的18人于1999年在杭州创立，他们相信互联网能够创造公平的竞争环境，让小企业通过创新与科技扩展业务，并在参与国内或全球市场竞争时处于更有利的位置。[1] 
阿里巴巴集团经营多项业务，另外也从关联公司的业务和服务中取得经营商业生态系统上的支援。业务和关联公司的业务包括：淘宝网、天猫、聚划算、全球速卖通、阿里巴巴国际交易市场、1688、阿里妈妈、阿里云、蚂蚁金服、菜鸟网络等
','/img/comlogo/alibaba.png','e'),
	(8,'baidumap','百度地图','mbaidumap@baidumap.com','86-10-9999 7746','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'百度地图是百度提供的一项网络地图搜索服务，覆盖国内近400个城市、数千个区县。2014年12月15日，百度与诺基亚达成协议，未来诺基亚地图及导航业务Here将向百度提供中国内地以外的地图数据服务','/img/comlogo/baidumap.png','e'),
	(9,'baoma','宝马','mbaoma@baoma.com','86-10-9859 7646','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'宝马公司创建于1916年，总部设在德国慕尼黑。BMW的蓝白标志宝马总部所在地巴伐利亚州州旗的颜色。百年来，宝马汽车由最初的一家飞机引擎生产厂发展成为以高级轿车为主导，并生产享誉全球的飞机引擎、越野车和摩托车的企业集团，名列世界汽车公司前列。宝马也被译为“巴依尔”','/img/comlogo/baoma.png','e'),
	(10,'danone','达能','mdanone@danone.com','86-10-9799 7346','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'达能集团总部设于法国巴黎的是一个业务极为多元化的跨国食品公司，集团的业务遍布六大洲、产品行销100多个国家。1996年集团的总营业额达到839亿法郎。 在法国、意大利及西班牙，达能集团都是最大的食品集团，达能亦是当今欧洲第三大食品集团，并列全球同类行业前六名','/img/comlogo/danone.png','e'),
	(11,'dazhong','大众','mdazhong@dazhong.com','86-10-4444 7254','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'大众汽车（德语：Volkswagen）是一家总部位于德国沃尔夫斯堡的汽车制造公司，也是世界四大汽车生产商之一的大众集团的核心企业','/img/comlogo/dazhong.png','e'),
	(12,'didi','滴滴出行','mdidi@didi.com','86-10-9999 7746','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'滴滴出行是涵盖出租车、[1]  专车、[2]  快车、[3]  顺风车、[4]  代驾及[5]  大巴等多项业务在内的一站式出行平台','/img/comlogo/didi.png','e'),
	(13,'Neusoft','东软','mneusoft@neusoft.com','86-10-7786 7566','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'东软集团是中国领先的IT解决方案与服务供应商，是上市企业，股票代码600718。公司成立于1991年，前身为东北大学下属的沈阳东大开发软件系统股份有限公司和沈阳东大阿尔派软件有限公司','/img/comlogo/Neusoft.png','e'),
	(14,'hongbanbao','红板报','mhongbanbao@hongbanbao.com','86-10-9779 7556','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'','/img/comlogo/hongbanbao.png','e'),
	(15,'hunanweishi','湖南卫视','mhunanweishi@hunamweishi.com','86-10-9999 7746','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'湖南卫视的全称是湖南广播电视台卫星频道，昵称芒果台，是湖南广播电视台和芒果传媒有限公司旗下的卫星电视频道','/img/comlogo/hunanweishi.png','e'),
	(16,'jiritoutiao','今日头条','mjinritoutiao@jiritoutiao.com','86-10-9999 7746','DC7BE88AB13D57CB571C0449D2DC75F9DFA8066B820A451139712FFF99880144A11901878A4CA6F50A319CF4704963920ED97D4B765A776D7010D637728056744061575798B6686695464C3DDB165CDB',
		'今日头条是一款基于数据挖掘的推荐引擎产品，它为用户推荐有价值的、个性化的信息，提供连接人与信息的新型服务，是国内移动互联网领域成长最快的产品服务之一','/img/comlogo/jinritoutiao.png','e');

insert into student_detail
	values(1,'2015141463140@stu.scu.edu.cn','17761279281','四川大学','软件工程',2015,'[上海,成都]','[互联网]','[项目管理,技术]',0);

insert into resume
	values(1,1,null);

insert into tag
	values(1,'互联网'),(2,'医疗/医药'),(3,'媒体/文化/休闲'),(4,'多元化'),(5,'通信/计算设备/软件'),(6,'生产/制造/纺织'),(7,'消费品'),(8,'能源/资源'),(9,'地产/建筑/施工材料'),(10,'奢侈品'),(11,'金融'),(12,'电子/电器/家电');

insert into star_company
	values(1,1);

insert into star_tag
	values(1,1);

insert into recruit
	values(1,1,'项目经理','指导项目进行','这是要求：我没写','南京',120000,240000,'2017-06-12 09:52:08','2017-06-11',0),(2,1,'产品经理','负责产品运营','熟悉java,熟悉管理团队','北京',30000,100000,'2017-07-28 02:37:36','2017-07-28',0),(3,1,'自然语言处理实习生','负责算法设计等','熟悉python','上海',10000,30000,'2017-07-28 02:38:31','2017-07-06',0),(4,2,'项目实习生','1. 参与京东app内容类频道的整体规划与设计，提升频道的用户粘性及商业价值产出；\n2. 负责协调各部门资源，把控产品迭代进度，确保项目顺利推进执行；\n3. 调研分析产品现状、市场环境、竞品情况，并据此制定阶段性的产品发展战略。','1. 对产品经理的职责有基本了解，清楚产品功能上线的基本流程；\n2. 对互联网有较浓厚的兴趣，对电商、直播或视频等领域有一定了解；\n3. 沟通能力强，善于协调资源驱动项目顺利进行；\n4. 具备较强的逻辑思维能力，能够主动发现并解决问题；\n5. 有互联网行业相关实习经历者优先。',' 上海',10000,20000,'2017-07-28 02:40:22','2017-07-27',0),(5,2,'人事助理','1.员工工服发放，人力资源库房、更衣柜管理\n2.员工入、离职流程和手续办理，员工假条管理，员工信息录入和维护\n3.主管日常工作协调和支持','1.本科及以上学历，专业不限，对人事工作有浓厚兴趣者优先考虑\n2.能保证每周5天实习时间者优先\n3.灵活聪明，执行力强，善于学习，主动性强，认真负责，沟通表达良好\n4. 能承受一定的工作压力\n5.熟悉办公软件的操作，如excel，outlook等','厦门',2000,3000,'2017-07-28 02:42:42','2017-07-30',0),(6,3,'管理咨询—运营管控实习生','1.员工工服发放，人力资源库房、更衣柜管理\n2.员工入、离职流程和手续办理，员工假条管理，员工信息录入和维护\n3.主管日常工作协调和支持','\n协助项目组与客户对接并深入了解地产行业现状、运作模式、客户业务及信息化体系 \n协助项目组成员进行系统蓝图制定与功能设计，落实建设规范与标准 \n协助项目组成员进行交付方案研讨与撰写 \n协助项目组成员组织客户培训 ','洛杉矶',100000,200000,'2017-07-28 02:52:13','2017-07-29',0),(7,3,'行政实习生','1、根据宜家采购政策负责文具管理和购物工具采购\n2、根据部门指示协助提供信息分析和采购报告 \n3、协助人事计划和排版专员优化排班，管理协调多技能员工的排办和跟进，最大化商场各部门资源\n4、负责办公室和会议室的设备维护\n5、协助开展商场的其他管理任务 \n6、负责商场相关文件的收集和管理 ','1、良好的沟通能力（须与商场很多同事接触）\n2、注重细节，富有责任心\n3、Microsoft Office等办公软件运用熟练\n4、能自信而清晰地用英语进行书面交流，至少四级以上。\n5、对宜家理念，包括我们的愿景和业务理念，宜家文化和价值观，以及如何创造业务优势有基本了解，宜粉快来！！\n6、对人们的日常家居生活以及家居装饰感兴趣\n7、充满好奇，渴望找到更高效的工作方式，期盼利用我的知识促进业务，从而提高绩效。','洛杉矶',1000,3000,'2017-07-28 02:53:02','2017-07-15',0),(8,3,'金融风险管理部门实习生','基于Basel和银监会相关规定，协助银行完成监管合规要求工作，内容涉及金融风险管理，金融工具建模；','本科生（大三及其以上）以及研究生，专业限于金融、数学、经济学等相关专业；\n精通使用MS Office软件；\n熟练使用WIND或彭博；\n需要量化背景，要求熟练操作matlab或SAS或R，优先考虑matlab以及SAS；\n每周至少四天到岗，实习期三个月；','厦门',2220,3338,'2017-07-28 02:54:21','2017-07-29',0),(9,4,'数据工程小组实习生','1. 负责大数据处理工程平台及策略算法的测试设计、工具开发、技术改进、效果评估以及数据挖掘分析、BI 等相关工作；\n2. 参与程序架构和代码的评审工作，并提出改进意见；\n3. 负责测试方案制定、测试工具开发、跟进产品质量。','1. 本科（含）以上，计算机及相关专业毕业，热爱技术；\n2. 对互联网有热情；\n3. 熟悉 Java、C++、Python 至少一门语言；\n4. 了解 Linux，有数据挖掘 Hadoop 大数据处理相关了解；\n5. 2018 或 2019 年毕业均可。','旧金山',2000,6000,'2017-07-28 02:57:06','2017-07-16',0),(10,7,'产品实习生','1. 协助参与产品素材创意搜集，关注图片视频的新潮玩法；\n\n2. 协助产品素材制作上线，包括沟通推进等工作；\n\n3. 协助其他运营团队相关工作；','4. 深度自拍用户！喜欢使用各种P图软件；\n\n5. 紧跟潮流，追踪各种热点，点子星人~抗压力好；\n\n6.一周四天及以上，能至少实习4个月。','北京',30000,49998,'2017-07-28 02:58:14','2017-07-29',0),(11,9,'金融风险管理部门实习生','基于Basel和银监会相关规定，协助银行完成监管合规要求工作','本科生（大三及其以上）以及研究生，专业限于金融、数学、经济学等相关专业；\n精通使用MS Office软件；\n熟练使用WIND或彭博；\n需要量化背景，要求熟练操作matlab或SAS或R，优先考虑matlab以及SAS；\n每周至少四天到岗，实习期三个月；','杭州',5000,6000,'2017-07-28 02:59:13','2017-07-02',0),(12,12,'HR实习生','1、协助培训组进行日常的人力资源培训工作、相关课件内容的制作与调整、培训安排等工作；\n2、协助完成上级领导交代的其他任务。','1、全日制本科或以上学历（211、985院校），人力资源相关专业；\n2、2018届、2019届在校学生；\n3、沟通表达能力优秀，逻辑思维清晰，熟练操作office办公软件，擅长PPT及H5制作，如熟练PS等图片设计或视频制作软件使用更佳；\n4、暑假需全勤，开学后每周需到岗3天或以上（不含周末），可长期实习者优先考虑；\n5、实习表现优秀者，有提前赢取校招录用的机会。','西安',9000,12000,'2017-07-28 03:00:13','2017-08-25',0),(13,11,'iOS开发实习生','1、开发安排的工作计划；\n2、参与产品开发小组、依据产品开发计划实施产品详细开发工作；\n3、技术文档编写；\n4、完成上级领导安排的其他相关工作。','1、重点院校计算机相关专业本科及本科以上学历；\n2、C语言基础扎实、数据库基础扎实、数据结构基础扎实；\n3、深入理解面向对象编程，具备良好的面向对象思想，熟悉常用的设计模式；\n4、严谨的逻辑思维，严谨的编码风格；\n5、具备良好的沟通协调能力、责任心强，具备优秀团队合作精神；\n6、在校参与过项目实操经验者优先；\n7、目前在校大学生。','北京',9000,50000,'2017-07-28 03:02:22','2017-07-09',0),(14,15,'债券融资部实习生','1、参与债券融资项目的现场工作，协助项目负责人开展尽职调查、底稿收集、申报材料起草和制作等工作；\n2、协助项目负责人撰写项目尽职调查报告、投标文件、项目建议书等；\n3、参与项目后续管理、信息披露等工作；\n4、协助完成领导及同事交办的其他工作。','1、国内重点高校（本硕必须均为985、211高校）2018年及2018年以后毕业的硕士研究生，会计、财管、金融等相关专业；\n2、实习期不少于3个月；每周实习时间不少于4天，可接受一定的出差安排；\n3、具有较好的逻辑思考能力和文字写作能力，熟练使用office；具有较强的学习能力、抗压能力、沟通能力、团队合作精神，责任心强；\n4、有相关实习经历优先考虑；通过CPA部分科目考试者优先考虑；通过国家司法考试者优先。\n5、本次为正式实习生招聘，表现优秀者可推荐留用。','成都',20000,50000,'2017-07-28 03:03:52','2017-09-23',0),(15,8,'研究部实习生','1、 全日制研究生在读，财经类专业优先 \n2、 主要职责：更新行业和宏观经济数据库、估值信息，协助进行课题类研究任务，报告翻译，撰写行业报告等 ','3、 熟悉国内股权市场，掌握行业研究基本方法，能够独立搭建估值模型，熟练操作wind，bloomberg等数据终端，英文读写水平优秀 \n4、 CPA、CFA优先，有券商实习经验优先 \n5、 能保证每周四天工作时间，公司为实习生提供有竞争力的实习津贴 ','北京',9000,12000,'2017-07-28 03:05:13','2017-09-22',0),(16,16,'HR 实习生','1. 维护、拓展公司招聘渠道，更新招聘信息； \n2. 根据 JD 筛选、推荐简历，协调面试官、候选人沟通； \n3. 负责跟进面试安排、面试反馈等招聘整体流程； \n4. 参与负责人力资源相关工作（内部推荐、员工关系、培训、绩效、校园招聘等）的相关工作。 ','1. 本科或研究生在读，大三优先 ； \n2. 熟练使用 office 等办公软件；  \n3. 每周保证实习 4 个工作日以上，实习至少三个月；  \n4. 积极、主动、耐心、踏实、高效、善于沟通。 ','上海',9000,50000,'2017-07-28 03:06:21','2017-11-03',0);

insert into recruit_tag
	values(1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(15,1),(16,1),(2,2),(4,3),(10,3),(11,3),(12,3),(16,3),(4,4),(6,4),(13,4),(16,4),(4,5),(5,6),(6,7),(11,7),(14,7),(16,7),(7,8),(7,9),(7,10),(8,11),(9,11),(10,11),(12,11),(14,11),(16,11),(16,12);

