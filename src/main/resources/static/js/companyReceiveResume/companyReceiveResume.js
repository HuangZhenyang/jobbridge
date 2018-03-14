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
        url:'/enterprise/showsendinfo',
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
    let sendId = "";
    let sendTime = "";
    let jobTitle = "";
    let userName = "";
    let status = "";
    let liClass = ["list-primary","list-danger","list-success","list-warning","list-info"];
    let bgThemes = ["bg-theme","bg-warning","bg-success","bg-info","bg-important"];
    let data = data.data;

    for(let i=0;i<data.length;i++){
        sendId = data[i].sendid;
        sendTime = data[i].sendtime;
        jobTitle = data[i].jobtitle;
        userName = data[i].username;
        status = data[i].status;

        eachDataDom = "<li class='"+liClass[i%liClass.length] + "' id='"+ sendId+"'>" +
            "<i class='fa fa-ellipsis-v'></i>"+
            "<div class='job-title'>"+ "<a href='/companyReceivedResumeDetail.html?id="+sendId+"'>" +

            "<span class='task-title-sp'>" + jobTitle + "</span>" + "<span>&nbsp from: " + userName +"&nbsp;&nbsp;</span>" +
            "<span class='badge " + bgThemes[i%bgThemes.length] + "'>" + sendTime + "</span>" +
            "</a>"+
            "<div class='pull-right hidden-phone'>" +
            "<span class='badge'>"+status+"</span>"+"<span>&nbsp;&nbsp;</span>"+
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
        url: '/enterprise/send/delete?id='+ $(evt).parent().parent().parent().attr('id'),
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



