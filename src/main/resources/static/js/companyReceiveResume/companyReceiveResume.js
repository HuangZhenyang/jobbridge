/**
 * Created by huangzhenyang on 2017/7/24.
 */

$(document).ready(function () {
    getHeaderUserName();
   getData();
});

/*
*  @author:HuangZhenyang
*  获取数据
* */
function getData() {

    $.ajax({
        url:'/company/request_resume_received',
        type:'get',
        dataType:'json'
    }).done(function (data) {
        setData(data);
    }).fail(function (xhr, status) {

    });
}


/*
 *  @author:HuangZhenyang
 *  操作Dom插入数据
 * */
function setData(data) {
    let eachDataDom = "";
    let dataDom = "";
    let resumeReceiveId = "";
    let resumeReceiveTime = "";
    let jobTitle = "";
    let studentUserName = "";
    let readStatus = "";
    let liClass = ["list-primary","list-danger","list-success","list-warning","list-info"];
    let bgThemes = ["bg-theme","bg-warning","bg-success","bg-info","bg-important"];
    let resumeReceiveList = data.resumeSendList;

    for(let i=0;i<resumeReceiveList.length;i++){
        resumeReceiveId = resumeReceiveList[i].resumeSendId;
        resumeReceiveTime = resumeReceiveList[i].resumeSendTime;
        jobTitle = resumeReceiveList[i].jobTitle;
        studentUserName = resumeReceiveList[i].studentUserName;
        readStatus = resumeReceiveList[i].readStatus;

        eachDataDom = "<li class='"+liClass[i%liClass.length] + "' id='"+ resumeReceiveId+"'>" +
            "<i class='fa fa-ellipsis-v'></i>"+
            "<div class='job-title'>"+ "<a href='/company/resume_received/resume?id="+resumeReceiveId+"'>" +

            "<span class='task-title-sp'>" + jobTitle + "</span>" + "<span>&nbsp from: " + studentUserName +"&nbsp;&nbsp;</span>" +
            "<span class='badge " + bgThemes[i%bgThemes.length] + "'>" + resumeReceiveTime + "</span>" +
            "</a>"+
            "<div class='pull-right hidden-phone'>" +
            "<span class='badge'>"+readStatus+"</span>"+"<span>&nbsp;&nbsp;</span>"+
            "<button class='btn btn-danger btn-xs fa fa-trash-o' onclick='del(this)'></button>" +
            "</div>"+
            "</div>"+
            "</li>";

        dataDom += eachDataDom;
    }

    console.log(dataDom);
    $('#sortable').append(dataDom);
}

/*
 *  @author:HuangZhenyang
 *  点击按钮的请求
 * */
function del(evt) {
    //console.log($(evt).parent().parent().parent().attr('id'));
    $.ajax({
        url: '/company/resume_received?id='+ $(evt).parent().parent().parent().attr('id'),
        type:'delete',
        dataType:'json'
    }).done(function (data) {
        if(data.ok === 'true'){
            delMessage(evt);
        }else{
            alert(data.reason);
        }
    }).fail(function (xhr,status) {

    })
}


/*
 *  @author:HuangZhenyang
 *  删除前端显示的招聘信息
 * */
function delMessage(evt) {
    $(evt).parent().parent().parent().remove();
}



