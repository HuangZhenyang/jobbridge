/**
 * 全局变量
 * */
let bg = ["bg-violet", "bg-green", "bg-pink", "bg-blue"]; //设置背景色
let icon = ["fa-group", "fa-paper-plane", "fa-coffee"]; //设置图标
let bgThemes = ["bg-theme","bg-warning","bg-success","bg-info","bg-important"];

$(document).ready(function () {
    getHeaderUserName();
    getData();
});

//获取用户投递职位的信息
function getData() {
    $.ajax({
        url: '/student/request_resume_send',
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        setsendData(data);
        console.log("成功收到数据：" + JSON.stringify(data));
    }).fail(function (xhr, status) {
        console.log("失败:" + xhr + " , ", status);
    });
}

//操作DOM,插入数据
function setsendData(data) {
    let sendDataDom = "";
    let eachsendDataDom = "";
    let contentDom = "";
    let data = data.resumeSendData;
    let position = "";
    let haveDelete = "";
    let bg = ["bg-violet", "bg-green", "bg-pink", "bg-blue"]; //设置背景色
    let icon = ["fa-group", "fa-paper-plane", "fa-coffee"]; //设置图标
    let bgThemes = ["bg-theme","bg-warning","bg-success","bg-info","bg-important"];

    if(data.length === 0){
        sendDataDom = "<h4> Oops! 你似乎还没投递任何简历哦</h4> ";
    }else{
        for (let i = 0; i < data.length; i++) {
            //职位是否已被删除
            if(data[i].haveDelete === "true"){
                haveDelete = "职位已删除";
            }else{
                haveDelete = "";
            }
            //设置时间轴卡片的位置
            if (i % 2 === 0) {
                position = "timeline-entry";
            } else {
                position = "timeline-entry left-aligned";
            }

            contentDom = "<div class='timeline-entry-inner'>" +
                "<time class='timeline-time'><span>" + data[i].time + "</span>" + "</time>" +
                "<div class='timeline-icon " + bg[i % bg.length] + "'>" + "<i class='fa " + icon[i % icon.length] + "'></i></div>" +
                "<div class='timeline-label " + bg[i % bg.length] + "'>" + "<h4 class='timeline-title'>" +"<a href='"+data[i].jobHref+"'>"+ "<span style='color: #ffffff'>"+data[i].jobTitle + "  " + data[i].companyName + "</span></a>"+"<span style='float: right' class='badge " + bgThemes[i%bgThemes.length] + "'>" + haveDelete + "</span>"  +"</h4><hr>" +
                "<p>" + data[i].jobDescribe + ".</p></div>" +
                "</div>";

            //如果是最后一个，加上  加号
            if (i === data.length - 1) {
                eachsendDataDom = "<article class='" + position + "'>" +
                    contentDom +
                    "<div class='timeline-entry-inner'>" +
                    "<div style='-webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);' class='timeline-icon'><a href='/recruit/info'><i class='fa fa-plus'></i></a></div>" +
                    "</div>" +
                    "</article>";
            } else {
                eachsendDataDom = "<article class='" + position + "'>" +
                    contentDom +
                    "</article>";
            }

            sendDataDom += eachsendDataDom;
            position = "";
            eachsendDataDom = "";
        }
    }

    $('#timeline').append(sendDataDom);
}

