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
        url: '/student/request_star',
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        setData(data);
    }).fail(function (xhr, status) {

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
    for (let i = 0; i < company.length; i++) {
        starCompanyId = company[i].starcomid;
        companyName = company[i].comname;
        email = company[i].email;
        phoneNumber = company[i].phonenum;
        companyDesc = company[i].comdesc;
        iconAddr = company[i].iconaddress;

        eachDom = "<li data-jsx='183165769' id='" + starCompanyId + "'>" +
            "<div class='position' data-jsx='183165769'>" +
            "<div class='logo' data-jsx='183165769'>" +
            "<a href='#' target='_blank' data-jsx='183165769'>" +
            "<img src='" + iconAddr + "' data-jsx='183165769'></a>" +
            "</div>" +
            "<div class='detail' data-jsx='183165769'>" +
            "<div class='companyName' data-jsx='183165769'>" +
            "<a href='#' target='_blank' data-jsx='183165769'>" + companyName + "</a>" +
            "</div>" +
            "<div class='contact' data-jsx='183165769'><span>Email：" + email + "</span>" + "<span>&emsp;Tel：" + phoneNumber + "</span>" +
            "</div>" +
            "<div class='wrapper' data-jsx='183165769' style='margin-top: 10px;padding-left: 0px;'>" + companyDesc +
            "</div>" +
            "</div>" +
            "<div class='action' data-jsx='183165769'>" +
            "<button type='button' class='ant-btn'><a href='#' data-jsx='183165769' onclick='cancel(this)'>取消收藏</a></button>" +
            "</div>" +
            "</div>" +
            "</li>";

        dom += eachDom;
    }
    $('#star').append(dom);

    dom = "";
    //插入收藏的职位（大类）
    for (let i = 0; i < job.length; i++) {
        jobTitle = job[i].jobTitle;
        eachDom = "<div class='col-lg-6'><span>" + jobTitle + "</span></div>";
        dom += eachDom;
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
        url: '/student/star?id=' + $(evt).parent().parent().parent().parent().attr('id'),
        type: 'delete',
        dataType: 'json'
    }).done(function (data) {
        if (data.ok === 'true') {
            del(evt);
        }
    }).fail(function (xhr, status) {

    })

}


/*
 *  @author:HuangZhenyang
 *  前端删除
 * */
function del(evt) {
    $(evt).parent().parent().parent().parent().remove();
}




