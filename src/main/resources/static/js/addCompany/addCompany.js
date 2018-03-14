/*
*       检查用户输入的规范
 */
;function checkInputFunc() {
    if ($('#userName').val() === null || $('#userName').val() === '') {
        $('#comregisterTip').text("请输入账户 ");
        return false;
    } else if ($('#password').val() === null || $('#password').val() === '') {
        $('#comregisterTip').text("请输入密码");
        return false;
    } else if(!($('#password').val().length>=6 && $('#password').val().length<=16)){
        $('#comregisterTip').text("密码不能少于6个字符或多于16个字符");
        return false;
    }else if ($('#name').val() === null || $('#name').val() === '') {
        $('#comregisterTip').text("请输入公司名字");
        return false;
    }else if ($('#email').val() === null || $('#email').val() === '') {
        $('#comregisterTip').text("请输入公司邮箱 ");
        return false;
    }else if(!checkIsEmail()){
        $('#comregisterTip').text("请输入正确的邮箱 ");
        return false;
    }else if ($('#phoneNum').val() === null || $('#phoneNum').val() === '') {
        $('#comregisterTip').text("请输入公司号码 ");
        return false;
    }else if ($('#enterpriseIntroduction').val() === null || $('#enterpriseIntroduction').val() === '') {
        $('#comregisterTip').text("请输入公司介绍 ");
        return false;
    }
    return true;
}

function addCompanyFunc(){
    if(checkInputFunc()){
        $.ajax({
            url: '/register',
            type: 'post',
            dataType: 'json',
            data: {
                userName:   $('#userName').val(),
                password:   $('#password').val(),
                name:       $('#name').val(),
                email:      $('#email').val(),
                phoneNum:   $('#phoneNum').val(),
                enterpriseIntroduction: $('#enterpriseIntroduction').val(),
                identity:   'e',
            },
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            var result = data;
            if(result.ok === "true"){
                window.location.href = "/adminPage/addEnterprise";
            }else{
                $('#comregisterTip').text(result.reason);
            }
        });
    }
}
function checkIsEmail() {
    var reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
    return reg.test($('#email').val());
}