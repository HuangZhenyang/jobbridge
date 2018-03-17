$(document).ready(function () {
    getHeaderUserName();
    showCompanyInfo();
});


function showCompanyInfo() {
    $.ajax({
        url: '/company/request_info',
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        setData(data);
    }).fail(function (xhr,status) {

    });
}

function setData(data) {
    $("#name").text(data.name);
    $("#userName").text(data.userName);
    $("#mailbox").text(data.mailbox);
    $("#phoneNum").text(data.phoneNum);
    $("#companyIntroduction").text(data.companyIntroduction);
}


