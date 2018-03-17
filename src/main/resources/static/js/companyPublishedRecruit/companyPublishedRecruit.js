/**
 * Created by huangzhenyang on 2017/7/24.
 */
let whichButton = ""; //add or modify


$(document).ready(function () {
    getHeaderUserName();
    getPublishedJobsData();
    /*
     * @author:HuangZhenyang
     * 绑定modal清空事件
     * */
    $('#myModal').on('show.bs.modal', function () {
        $(this).removeData("bs.modal");
        clearModal();
    });
});


/*
 *  @author: HuangZhenyang
 *  获取该企业已经发布的招聘信息的 简要信息：时间+职位名称
 * */
function getPublishedJobsData() {
    $.ajax({
        url:'/enterprise/request_recruit',
        type:'get',
        dataType:'json'
    }).done(function (data) {
        setPublishedJobsData(data);
    }).fail(function (xhr,status) {

    });
}


/*
 *  @author: HuangZhenyang
 *  插入Dom
 * */
function setPublishedJobsData(data) {
    let eachDataDom = "";
    let dataDom = "";
    let recruitList = data.recruitList;
    let publishedId = "";
    let publishTime = "";
    let jobTitle = "";
    let liClass = ["list-primary","list-danger","list-success","list-warning","list-info"];
    let bgThemes = ["bg-theme","bg-warning","bg-success","bg-info","bg-important"];


    for(let i=0;i<data.length;i++){
        publishedId = recruitList[i].publishedId;
        publishTime = recruitList[i].publishedTime;
        jobTitle = recruitList[i].jobTitle;
        eachDataDom = "<li class='"+liClass[i%liClass.length] + "' id='"+ publishedId+"'>"  +
            "<i class='fa fa-ellipsis-v'></i>"+
            "<div class='job-title'>"+
            "<span class='task-title-sp'>" + jobTitle + "</span>" + "<span>&nbsp</span>" +
            "<span class='badge " + bgThemes[i%bgThemes.length] + "'>" + publishTime + "</span>" +
            "<div class='pull-right hidden-phone'> " +
            "<button class='btn btn-primary btn-xs fa fa-pencil' onclick='modify(this)' data-toggle='modal' data-target='#myModal'></button>" + "<span>&nbsp</span>"+
            "<button class='btn btn-danger btn-xs fa fa-trash-o' onclick='del(this)'></button>" +
            "</div>"+
            "</div>"+
            "</li>";
        dataDom += eachDataDom;
    }

    $('#sortable').append(dataDom);
}


/*
 *  @author: HuangZhenyang
 *  删除当前行的请求
 * */
function del(evt) {
    $.ajax({
        url: '/company/recruit?id='+$(evt).parent().parent().parent().attr('id'),
        type:'delete',
        dataType:'json'
    }).done(function (data) {

        if(data.ok==='true'){

            delPublishedJob(evt);
        }else{

            alert(data.reason);
        }
    }).fail(function (xhr,status) {

    })
}


/*
 *  @author: HuangZhenyang
 *  删除发布的招聘信息
 * */
function delPublishedJob(evt) {
    $(evt).parent().parent().parent().remove();
}


/*
 *  @author: HuangZhenyang
 *  修改发布的招聘信息
 * */
function modify(evt) {
    whichButton = "modify";
    let publishedId = $(evt).parent().parent().parent().attr('id');
    //设置publishedId在按钮的id
    $("input[value='提交']").attr('id',publishedId);
    showcominfo(publishedId);
}



//提交请求信息
function  showcominfo(publishedId) {

    $.ajax({
        url: '/company/recruit?id='+publishedId,
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        setData(data);
    }).fail(function (xhr,status) {

    });
}

//设置已经发布的信息
function setData(data) {

    clearModal();
    let jobName = data.jobName;
    let industry = data.industry;
    let location = data.location;
    let lowSalary = data.lowSalary;
    let highSalary = data.highSalary;
    let deadline = data.deadline;
    let jobDescribe = data.jobDescribe;
    let jobRequire = data.jobRequire;
    let str = "";


    $("#jobName").attr('value',data.jobName);

    for(let i=0;i<industry.length;i++){
        str = "input:checkbox[value='"+industry[i]+"']";
        $(str).prop('checked',true);
    }

    $("#location").attr('value',data.location);
    $("#lowSalary").attr('value',data.lowSalary);
    $("#highSalary").attr('value',data.highSalary);
    $("#deadline").attr('value',data.deadline);
    $("#jobDescribe").val(data.jobDescribe);
    $("#jobRequire").val(data.jobRequire);

}


/*
 *  @author: HuangZhenyang
 *  发布或者修改
 * */
function save(evt){
    let jobName=$('#jobName').val();
    let industry = new Array();
    let location= $('#location').val();
    let lowSalary= $('#lowSalary').val();
    let highSalary= $('#highSalary').val();
    let deadline= $('#deadline').val();
    let jobDescribe=$('#jobDescribe').val();
    let jobRequire=$('#jobRequire').val();

    let publishedId = "";
    if(whichButton === "add"){
        publishedId = "";
    }else if(whichButton === "modify"){
        publishedId = $(evt).attr('id');
    }

    $('input[name="industry"]:checked').each(function(){
        industry.push($(this).siblings().text());
    });


    if(checkSaveFunc(jobName,industry,location,lowSalary,highSalary,deadline,jobDescribe,jobRequire)){
        let industryStr = "[";
        for(let i=0;i<industry.length;i++){
            industryStr += industry[i];
            if(i<industry.length-1){
                industryStr+=",";
            }
        }
        industryStr += "]";

        let sendData = {
            "jobName": jobName,
            "industry":industryStr,
            "location": location,
            "lowSalary": lowSalary,
            "highSalary": highSalary,
            "deadline": deadline,
            "jobDescribe": jobDescribe,
            "jobRequire": jobRequire
        };

        $.ajax({
            type: 'post',
            url: '/company/recruit?id='+publishedId,
            dataType: 'json',
            data: sendData,
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            let result = data;
            if(result.ok === "true"){
                window.location.href = "";
            }else{
                $('#saveTip').text(result.reason);
            }
        }).fail(function (xhr,status) {

        });
    }

    //如果whichButton为add，则在前端插入一行数据
    if(whichButton === 'add'){
        sendData = {
            "jobName": jobName,
            "industry":industry,
            "location": location,
            "lowSalary": lowSalary,
            "highSalary": highSalary,
            "deadline": deadline,
            "jobDescribe": jobDescribe,
            "jobRequire": jobRequire
        };
        setPublishedJobsData(sendData);
    }

}


/*
 * @author:HuangZhenyang
 * 修改WhichButton为add
 * */
function changeWhichButton() {
    whichButton = "add";
}


/*
 * @author:HuangZhenyang
 * 清空modal
 * */
function clearModal() {
    $('#jobName').attr('value',"");

    $('input[name="industry"]:checked').each(function(){
        $(this).removeAttr('checked');
    });

    $('#location').attr('value',"");
    $('#lowSalary').attr('value',"");
    $('#highSalary').attr('value',"");
    $('#deadline').attr('value',"");
    $('#jobDescribe').val("");
    $('#jobRequire').val("");
}






