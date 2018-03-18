---
---
List<Recruit> recruitList 表示返回的是一个List,每一个对象都是Recruit;
{} 中表示需要从返回的对象取出来的数据;

## 公司接口

---

### companyInfo.html 公司信息
返回 Company company

{
	"name",
	"userName",
	"mailbox",
	"phoneNum",
	"companyIntroduction"
}

### companyPublishedRecruit.html 公司已发布的招聘信息列表
返回 List<Recruit> recruitList

{
	{
		"publishedId",
		"publishedTime",
		"jobTitle"	
	},
	{
		"publishedId",
		"publishedTime",
		"jobTitle"	
	}
}


### companyReceivedResume.html 公司收到的投递信息
返回 List<ResumeSend> resumeSendList
     List<String> jobTitleList
     List<String> studentNameList
     List<String> readStatusList

resumeSendList:
{
	{
		"resumeSendId",
		"resumeSendTime"	
	},
	{
		"resumeSendId",
		"resumeSendTime"	
	}	
}

jobTitleList:
{
	{
		"java实习生"	
	},
	{
		"java实习生"	
	}
}

studentNameList:
{
	{
		"何干是"
	},
	{
		"何干是"
	}
}

readStatusList:
{
	{
		"未读"
	},
	{
		"未读"
	}
}


   
