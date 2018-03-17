/*
*       检查用户输入的规范
 */
;function checkInputFunc() {
    if ($('#userName').val() === null || $('#userName').val() === '') {
        $('#companySignUpTip').text("请输入账户 ");
        return false;
    } else if ($('#password').val() === null || $('#password').val() === '') {
        $('#companySignUpTip').text("请输入密码");
        return false;
    } else if(!($('#password').val().length>=6 && $('#password').val().length<=16)){
        $('#companySignUpTip').text("密码不能少于6个字符或多于16个字符");
        return false;
    }else if ($('#name').val() === null || $('#name').val() === '') {
        $('#companySignUpTip').text("请输入公司名字");
        return false;
    }else if ($('#mailbox').val() === null || $('#mailbox').val() === '') {
        $('#companySignUpTip').text("请输入公司邮箱 ");
        return false;
    }else if(!checkIsEmail()){
        $('#companySignUpTip').text("请输入正确的邮箱 ");
        return false;
    }else if ($('#phoneNum').val() === null || $('#phoneNum').val() === '') {
        $('#companySignUpTip').text("请输入公司号码 ");
        return false;
    }else if ($('#companyIntroduction').val() === null || $('#companyIntroduction').val() === '') {
        $('#companySignUpTip').text("请输入公司介绍 ");
        return false;
    }
    return true;
}

function addCompanyFunc(){
    console.log("");
    if(checkInputFunc()){
        $.ajax({
            url: '/sign_up',
            type: 'post',
            dataType: 'json',
            data: {
                userName:   $('#userName').val(),
                password:   $('#password').val(),
                name:       $('#companyName').val(),
                mailbox:    $('#mailbox').val(),
                phoneNum:   $('#phoneNum').val(),
                companyIntroduction: $('#companyIntroduction').val(),
                identity:   'e',
            },
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            let result = data;
            if(result.ok === "true"){
                window.location.href = "/sign_in";
            }else{
                $('#companySignUpTip').text(result.reason);
            }
        });
    }
}
function checkIsEmail() {
    let reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
    return reg.test($('#mailbox').val());
}