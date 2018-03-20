/**
 * Created by huangzhenyang on 2017/7/25.
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
        url:'/student/request_star',
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        setData(data);
    }).fail(function (xhr,status) {

    });
}

/*
 *  @author:HuangZhenyang
 *  操作Dom插入数据：插入收藏的公司，插入收藏的职位（大标签）
 * */
function setData(data) {
    var company = data.company;
    var job = data.job;

    var starCompanyId = "";
    var companyName = "";
    var email = "";
    var phoneNumber = "";
    var companyDesc = "";
    var iconAddr = "";
    var jobTitle = "";

    var eachDom = "";
    var dom = "";

    //插入收藏的公司
    for(let i=0;i<company.length;i++){
        starCompanyId = company[i].starcomid;
        companyName = company[i].comname;
        email = company[i].email;
        phoneNumber = company[i].phonenum;
        companyDesc = company[i].comdesc;
        iconAddr = company[i].iconaddress;

        eachDom = "<li id='"+  starCompanyId   +"'>"+
                      "<div class='position'>"+
                          "<div class='logo'>" +
                              "<a href='#' target='_blank' >"+
                                  "<img src='"+iconAddr+"' ></a>"+
                          "</div>" +
                          "<div class='detail' >"+
                              "<div class='companyName' >" +
                                  "<a href='#' target='_blank' >" + companyName + "</a>" +
                              "</div>" +
                              "<div class='contact' ><span>Email：" + email+ "</span>" + "<span>&emsp;Tel：" + phoneNumber +"</span>" +
                              "</div>" +
                              "<div class='wrapper'  style='margin-top: 10px;padding-left: 0px;'>" + companyDesc +
                              "</div>"+
                          "</div>" +
                          "<div class='action' >" +
                              "<button type='button' class='ant-btn'><a href='#'  onclick='cancel(this)'>取消收藏</a></button>" +
                          "</div>" +
                      "</div>" +
                  "</li>";

        dom += eachDom;
    }
    $('#star').append(dom);

    dom="";
    //插入收藏的职位（大类）
    for(let i=0;i<job.length;i++){
        jobTitle = job[i].jobTitle;
        eachDom = "<div class='col-lg-6'><span>"+jobTitle+"</span></div>";
        dom+=eachDom;
    }
    $('#star-job').append(dom);

}


/*
 *  @author:HuangZhenyang
 *  取消收藏
 * */
function cancel(evt) {
    //alert($(evt).parent().parent().parent().parent().attr('id'));
    $.ajax({
        url:'/student/star?id=' + $(evt).parent().parent().parent().parent().attr('id'),
        type:'delete',
        dataType: 'json'
    }).done(function (data) {
        if(data.ok === 'true'){
            del(evt);
        }
    }).fail(function (xhr,status) {

    })

}


/*
 *  @author:HuangZhenyang
 *  前端删除
 * */
function del(evt) {
    $(evt).parent().parent().parent().parent().remove();
}




