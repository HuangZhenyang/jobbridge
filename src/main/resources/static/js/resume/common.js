var M={};

M.invoke=function(type,url,data,sf,errf,timeout){
    url=location.protocol+'//'+location.host+url;
	if($.isArray(sf)){
		var oldSf = sf;
		sf = function(data){
			for(var i = 0; i<oldSf.length;i++){
				oldSf[i](data);
			}
		};
	}
	if(typeof sf != 'function'){
		sf = function(data){
			$.toast(data.msg, function() {});
		};
	}
	if(!timeout){
		timeout=60000;
	}
	$.ajax({
		url : url,
		dataType:'json',
		type: type,
		timeout : timeout,
		data : data,
		error : function(xhr,status){
			if(xhr.status===401){
				var pathname = location.pathname;
				if(pathname == '/resume' || pathname == '/resume/cn' || pathname == '/resume/en') {
					M.alert('鎻愮ず', '<p class="text-center">璇蜂笉瑕佸叧闂椤甸潰锛屽湪鏂扮獥鍙ｇ櫥褰曞悗锛屽洖鍒版湰鐣岄潰鐐瑰嚮淇濆瓨锛屽幓<a href="/user/loginCode" target="_blank">鏂扮獥鍙ｇ櫥褰�</a></p>');
				} else {
					M.alert('鎻愮ず', '<p class="text-center">璇�<a href="/user/loginCode" target="_blank">鐧诲綍</a></p>');
				}
            }else if(xhr.status===403){
                M.alert('鎻愮ず', '<p class="text-center">璇�<a href="/user/loginCode">鍗囩骇璐︽埛</a></p>');
            }else if(errf) {
            	errf(xhr,status);
            }else{
                M.alert('鎻愮ず', '鎿嶄綔澶辫触');
			}
		},
		success : sf
	});
};
M.ajax = function (param) {
    param = $.extend({
        timeout: 60000,
        dataType: 'json'
    }, param);
    param.url = location.protocol + '//' + location.host + param.url;
    if (param.dto) {
        param.data = {
            info: JSON.stringify(param.data)
        };
    }
    var error = param.error;
    param.error = function (xhr, status) {
        if (xhr.status === 403) {
            M.alert('鎻愮ず', '<p class="text-center">璇�<a href="/user/loginCode">鍗囩骇璐︽埛</a></p>');
        } else if (error) {
            error(xhr, status);
        } else {
            M.alert('鎻愮ず', '鎿嶄綔澶辫触');
        }
    };
    $.ajax(param);
};

M.alert = function(title, content) {
	$.alert({
		title: title || '鎻愮ず', 
		content: content,
		backgroundDismiss: true,
		buttons: {
			ok: {
				text: '纭'
			}
		}
	});
};

M.confirm = function(content, confirmAction, cancelAction) {
	$.confirm({
	    title: '鎻愮ず',
	    content: content || '鏄惁鎵ц鎿嶄綔',
	    backgroundDismiss: true,
	    buttons: {
	        confirm: {
	        	text: '纭',
	        	btnClass: 'btn-primary',
	            keys: ['enter'],
	        	action: confirmAction != null ? confirmAction : function () {
	        		
	        	}
	        },
	        cancel: {
	        	text: '鍙栨秷',
	        	btnClass: 'btn-default',
	            keys: ['esc'],
	        	action: cancelAction != null ? cancelAction :  function () {
	        		
	        	}
	        }
	    }
	});
};

M.action={};
//鍔犺浇鍒嗛〉
M.action.loadPage=function(param){
	param=$.extend({
		//鍒嗛〉瀹瑰櫒(蹇呰)
		container:null,
		//姣忛〉淇℃伅鏁伴噺(蹇呰)
		pageSize:10,
		//褰撳墠椤电爜(蹇呰)
		pageNumber:1,
		//淇℃伅鎬绘暟(蹇呰)
		totalCount:0,
		//鐐瑰嚮椤电爜
		action:function(pageNumber){}
	},param);
	//鎬婚〉鏁�
	var pageCount=parseInt((param.totalCount-1)/param.pageSize+1);
	if(pageCount<=1){
		return;
	}
	//璧峰椤电爜
	var pageNumberBegin=param.pageNumber-5<1?1:param.pageNumber-5;
	//缁撴潫椤电爜
	var pageNumberEnd=param.pageNumber+5>pageCount?pageCount:param.pageNumber+5;
	var $page=$(param.container);
	//==============dom鐢熸垚
	$page.html('<div class="my-pagination"><ul class="pagination"></ul></div>');
	$page_ul=$page.find('ul');
	//涓婁竴椤�
	$page_ul.append('<li class="first '+(param.pageNumber==pageNumberBegin?'disabled':'')+'"><a href="#">芦</a></li>');
	$page_ul.append('<li class="prev '+(param.pageNumber==pageNumberBegin?'disabled':'')+'"><a href="#">鈥�</a></li>');
	//椤电爜
	for(var i=pageNumberBegin;i<=pageNumberEnd;i++){
		$page_ul.append('<li class="num '+(i==param.pageNumber?'active':'')+'"><a href="#">'+i+'</a></li>');
	}
	//涓嬩竴椤�
	$page_ul.append('<li class="next '+(param.pageNumber==pageNumberEnd?'disabled':'')+'"><a href="#">鈥�</a></li>');
	$page_ul.append('<li class="last '+(param.pageNumber==pageNumberEnd?'disabled':'')+'"><a href="#">禄</a></li>');
	//==============dom浜嬩欢
	$page.off('click');
	$page.on('click','a',function(e){
		e.preventDefault();
		var $ele=$(this);
		var $li=$ele.closest('li');
		if($li.is('.disabled') || $li.is('.active')){
			return;
		}
		if($li.is('.first')){
			param.action(1);
		}else if($li.is('.prev')){
			param.action(param.pageNumber-1);
		}else if($li.is('.next')){
			param.action(param.pageNumber+1);
		}else if($li.is('.last')){
			param.action(pageCount);
		}else if($li.is('.num')){
			param.action(parseInt($ele.html()));
		}
	});
};

M.browser={
    isAndroid:/Android/i.test(navigator.userAgent),
    isPhone:/iPhone/i.test(navigator.userAgent)
};

M.regexp={};
M.regexp.email=/^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$/;

function clone(obj) {
  // Handle the 3 simple types, and null or undefined
  if (null == obj || "object" != typeof obj) return obj;
 
  // Handle Date
  if (obj instanceof Date) {
    var copy = new Date();
    copy.setTime(obj.getTime());
    return copy;
  }
 
  // Handle Array
  if (obj instanceof Array) {
    var copy = [];
    for (var i = 0, len = obj.length; i < len; ++i) {
      copy[i] = clone(obj[i]);
    }
    return copy;
  }
 
  // Handle Object
  if (obj instanceof Object) {
    var copy = {};
    for (var attr in obj) {
      if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
    }
    return copy;
  }
 
  throw new Error("Unable to copy obj! Its type isn't supported.");
}

Date.prototype.format = function(format) {
    var o = {
    	"M+" : this.getMonth()+1, //month
    	"d+" : this.getDate(),    //day
    	"h+" : this.getHours(),   //hour
    	"m+" : this.getMinutes(), //minute
    	"s+" : this.getSeconds(), //second
    	"q+" : Math.floor((this.getMonth()+3)/3),  //quarter
    	"S" : this.getMilliseconds() //millisecond
    };
    
    if(/(y+)/.test(format)) {
    	format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    	
    for(var k in o) {
    	if(new RegExp("("+ k +")").test(format)) {
    		 format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
    	}
    }
   
    return format;
};

//鎴彇瀛楃涓茶嫳鏂囧崰鍗婁釜锛屾眽瀛楀崰涓€涓�
String.prototype.xzSubstring = function(end){
	var intLength = 0;
	for (var i=0;i<this.length;i++){
	   if (this.substring(i, 1).charCodeAt(0)<299){
		   intLength=intLength+0.5;  
	   }else{
		   intLength=intLength+1;
	   }
	   if(intLength > end){
		   return this.substring(0, i);
	   }
	   
	}
	return this;
};
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");  
};
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, "");  
};
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, "");  
};
String.prototype.ismail = function() {
	return (/^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$/).test(this);
};
String.prototype.isphone = function() {
	return (/^1[34578]\d{9}$/).test(this);
};
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
	    return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
	} else {
	    return this.replace(reallyDo, replaceWith);  
	}
};
String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substring(this.length-str.length)==str)
	  return true;
	else
	  return false;
	return true;
};
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
};

var console = window.console;
if (!console || !console.log || !console.error) {
  console = {log: function(){ }, error: function(){ }};
}