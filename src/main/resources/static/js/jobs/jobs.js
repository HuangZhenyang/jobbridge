/**
 * Created by huangzhenyang on 2017/7/25.
 */
var pageNumber = 1;


$(document).ready(function () {
    getHeaderUserName();
   getData();
   setOptionEvent();

});


/*
* @author: HuangZhenyang
* 请求数据
* */
function getData() {
    $.ajax({
        url:'/recruit/showinfo',
        type:'get',
        dataType:'json'
    }).done(function (data) {
        setData(data);
        setPagePrtition(data);
    }).fail(function (xhr,status) {

    });
}


/*
 * @author: HuangZhenyang
 * 操作Dom,插入数据
 * */
function setData(data) {
    //删除原先  #job-list 里的东西，如果有的话 <(￣ˇ￣)/
    $('#job-list').children().remove();

    var eachDom = "";
    var dom = "";
    var data = data.data;
    var jobTitle = "";
    var jobId = "";
    var companyName = "";
    var location = "";
    var time = "";
    var companyDesc = "";
    var iconAddress = "";

    for(let i=0;i<data.length;i++){
        jobTitle = data[i].jobtitle;
        jobId = data[i].jobid;
        companyName = data[i].companyname;
        location = data[i].location;
        time = data[i].time;
        companyDesc = data[i].companydesc;
        iconAddress = data[i].iconaddress;

        eachDom =   "<div class='ant-col-12 gutter-row'>" +
                        "<div class='ant-card media ant-card-bordered'>"+
                            "<div class='ant-card-body'>" +
                                "<a class='media-left' style='height:80px;' href='/studentcenter/recruitinfo?id=" + jobId + "' target='_blank' data-jsx='1812380509'><img style='width: 80px;height:80px;' src='http://localhost:8080"+ iconAddress +"' data-jsx='1812380509'/></a>" +
                                "<div class='media-body' data-jsx='1812380509' style='width: 301px;'>" +
                                "<a href='/studentcenter/recruitinfo?id="+jobId +"' target='_blank' data-jsx='1812380509'>" +
                                    "<dl data-jsx='1812380509'>" +
                                        "<dt class='line-clamp' data-jsx='1812380509'>" + jobTitle +"</dt>" +
                                        "<dd data-jsx='1812380509'>"+companyName + "</dd>" +
                                        "<dd data-jsx='1812380509'><i class='anticon anticon-environment-o'></i>" + location + "</dd>"+
                                        "<dd data-jsx='1812380509'><i class='anticon anticon-clock-circle-o'></i>" + time + "</dd>" +
                                    "</dl>"+
                                "</a>" +
                                "</div>" +
                            "<div class='comment' data-jsx='1812380509'><label class='my-pull-left lab' data-jsx='1812380509'>JobBridge说：</label><a href='/studentcenter/recruitinfo?id="+jobId +"' target='_blank' data-jsx='1812380509'>" +
                                "<span class='line-clamp2 cont' title='" + companyDesc +"' data-jsx='1812380509'>" + companyDesc + "</span></a>" +
                            "</div>"+
                            "</div>" +
                        "</div>"+
                    "</div>";

        dom += eachDom;
    }

    $('#job-list').append(dom);
    //设置页码
    $('#pages').bootstrapPaginator({
        currentPage:pageNumber
    });

}


/*
 * @author: HuangZhenyang
 * 为每个选项设置事件
 * */
function setOptionEvent() {
    var optionList = getOptionList();
    //绑定事件
    for(let i=0;i<optionList.length;i++){
        //闭包
        (function(j)
        {
            optionList[j].on('click',function()
            {
                if(j !==0 && j !== 14){

                    if(j<=13){ //city
                        optionList[0].removeClass('ant-tag-checkable-checked'); //删除 不限 的样式
                       /* for(let k=0;k<14;k++){
                            if(optionList[k].hasClass('ant-tag-checkable-checked')){
                                cityList.push(optionList[k].text());
                            }
                        }
                        //发送ajax请求
                        getDataByCondition("city",cityList);*/
                    }else if(j>=15){    //industry
                        optionList[14].removeClass('ant-tag-checkable-checked'); //删除 不限 的样式
                        /*for(let k=15;k<38;k++){
                            if(optionList[k].hasClass('ant-tag-checkable-checked')){
                                industryList.push(optionList[k].text());
                            }
                        }
                        getDataByCondition("industry",industryList);*/
                    }

                    //Toggle点击的Tag的样式
                    if(optionList[j].hasClass('ant-tag-checkable-checked')){
                        optionList[j].removeClass('ant-tag-checkable-checked');
                        //如果取消了，1-13都没有checked,那么就给[0]加上checked
                        let flag = true;
                        for(let i=1;i<=13;i++){
                            if(optionList[i].hasClass('ant-tag-checkable-checked')){
                                flag = false;
                            }
                        }
                        if(flag){
                            optionList[0].addClass('ant-tag-checkable-checked');
                        }
                        //如果取消了，15-137都没有checked,那么就给[14]加上checked
                        flag=true;
                        for(let i=15;i<=37;i++){
                            if(optionList[i].hasClass('ant-tag-checkable-checked')){
                                flag = false;
                            }
                        }
                        if(flag){
                            optionList[14].addClass('ant-tag-checkable-checked');
                        }
                    }else{
                        optionList[j].addClass('ant-tag-checkable-checked');
                    }

                    getDataByCondition(optionList); //ajax请求数据
                }else if(j === 0){  //点的是  不限
                    for(let k=j+1;k<14;k++){//删除城市的选择
                        optionList[k].removeClass('ant-tag-checkable-checked');
                    }
                    optionList[j].addClass('ant-tag-checkable-checked');
                    getDataByCondition(optionList);
                }else if(j === 14){ //点的是不限
                    for(let k=j+1;k<38;k++){ //删除industry的其他的选择
                        optionList[k].removeClass('ant-tag-checkable-checked');
                    }
                    optionList[j].addClass('ant-tag-checkable-checked');
                    getDataByCondition(optionList);
                }
            });
        })(i);
    }
}


/*
 * @author: HuangZhenyang
 * 根据条件请求事件
 * */
function getDataByCondition(optionList) {
    var cityList = new Array();
    var industryList = new Array();
    for(let i=0;i<=13;i++){
        if(optionList[i].hasClass('ant-tag-checkable-checked')){
            cityList.push(optionList[i].text().trim());
        }
    }

    for(let i=14;i<38;i++){
        if(optionList[i].hasClass('ant-tag-checkable-checked')){
            industryList.push(optionList[i].text().trim());
        }
    }

    var sendData = JSON.stringify({
        "numberofpage":pageNumber*10,
        "optionlist":{
            "citylist":cityList,
            "industrylist":industryList
        }
    });

    console.log(sendData);

    $.ajax({
        url:'/recruit/showinfobycondition',
        type:'post',
        dataType:'json',
        data:{
            "content":sendData
        }
    }).done(function (data) {
        //刷新总页数
        setPagePrtition(data);
        setData(data);
    }).fail(function (xhr,status) {

    });
}


/*
* @author:HuangZhenyang
* 分页
* */
function setPagePrtition(data) {
    let numberOfPage = 1;
    if(parseInt(data.numberofpage) === 0){
        numberOfPage = 1;
    }else{
        numberOfPage = data.numberofpage;
    }

    var options = {
        currentPage: 1,
        totalPages:  numberOfPage,
        alignment:'center',
        onPageClicked: function (e,originalEvent,type,page) {
            pageNumber = page;
            var optionList = getOptionList();
            getDataByCondition(optionList);
        }
    };
    $('#pages').bootstrapPaginator(options);
}


/*
* @author:HuangZhenyang
* 获取用户选择的optionList
* */
function getOptionList() {
    var optionList = $('div.ant-tag'); //获取的是dom对象的数组
    //转成jquery对象
    for(let i=0;i<optionList.length;i++){
        optionList[i] = $(optionList[i]);
    }
    return optionList;
}














