$(document).ready(function () {
    getHeaderUserName();
    getData();
});


/*
* @author:Huangzhenyang
* 获取url中的参数
* */
/*function getUrlParameter(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}*/

/**
 * @author:Huangzhenyang
 * 获取recruit的id
 * */
function getRecruitId() {
    return $('#recruitId').text();
}

function  getData() {
    //var jobId = getUrlParameter('id');
    let jobId = getRecruitId();
    $.ajax({
        url: '/student/request_recruit?id='+jobId,
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        setData(data);
    }).fail(function (xhr,status) {

    });
}


function setData(data) {
    let haveStar = data.haveStar;
    let companyId = data.companyId;
    let jobId = data.jobId;
    let content = data.content;

    if(haveStar === 'true'){
        $('button.my-star-button').text('已收藏');
    }else{
        $('button.my-star-button').text('收藏');
    }

    $('button.my-star-button').attr('id',companyId);
    $('button.my-send-button').attr('id',jobId);

    $("#jobName").text(content.jobName);
    $("#name").text(content.name);
    $("#location").text(content.location);
    $("#lowSalary").text(content.lowSalary);
    $("#highSalary").text(content.highSalary);
    $("#companyIntroduction").text(content.companyIntroduction);
    $("#deadline").text(content.deadline);
    $("#jobDescribe").text(content.jobDescribe);
    $("#jobRequire").text(content.jobRequire);
}


/*
* @author: HuangZhenyang
* 收藏公司
* */
function starButtonFunc(evt) {
    if($(evt).text().trim() === "收藏"){
        $.ajax({
            url:'/student/star',
            type:'post',
            dataType:'json',
            data:{
                "companyid":$(evt).attr('id')
            }
        }).done(function (data) {
            if(data.ok==='true'){
                $('button.my-star-button').text('已收藏');
            }else{
                $('button.my-star-button').text('收藏');
            }
        }).fail(function (xhr,status) {

        });
    }else if($(evt).text().trim() === "已收藏"){ //取消收藏
        $.ajax({
            url:'/student/star?id='+$(evt).attr('id'),
            type:'delete',
            dataType:'json',
        }).done(function (data) {
            if(data.ok==='true'){
                $('button.my-star-button').text('收藏');
            }else{
                $('button.my-star-button').text('已收藏');
            }
        }).fail(function (xhr,status) {

        });
    }

}



/*
 * @author: HuangZhenyang
 * 投递简历
 * */
function send(evt) {
    $.ajax({
        url:'/student/resume_send',
        type:'post',
        dataType:'json',
        data:{
            "jobid":$(evt).attr('id')
        }
    }).done(function (data) {
        if(data.ok==='true'){
            $('#sendTip').text('投递成功');
        }else{
            $('#sendTip').text(data.reason);
        }
    }).fail(function (xhr,status) {

    });
}


