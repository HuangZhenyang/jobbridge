/*
 *       检查用户输入的规范
 */
function checkRegisterFunc() {
    if ($('#userName').val() === null || $('#userName').val() === '') {
        $('#registerTip').text("请输入用户名");
        return false;
    }else if ($('#email').val() === null || $('#email').val() === '') {
        $('#registerTip').text("请输入邮箱");
        return false;
    }else if (!checkIsEmail()) {
        $('#registerTip').text("请输入正确的邮箱");
        return false;
    } else if ($('#password').val() === null || $('#password').val() === '') {
        $('#registerTip').text("请输入密码");
        return false;
    } else if(!($('#password').val().length>=6 && $('#password').val().length<=16)){
        $('#registerTip').text("密码不能少于6个字符或多于16个字符");
        return false;
    }else if ($('#password1').val() === null || $('#password1').val() === '') {
        $('#registerTip').text("请再次输入密码");
        return false;
    } else if (!($('#password').val() === $('#password1').val())) {
        $('#registerTip').text("两次输入的密码不一致");
        return false;
    }

    return true;
}

function registerFunc(){
    if(checkRegisterFunc()){
        $.ajax({
            url: '/register',
            type: 'post',
            dataType: 'json',
            data: {
                userName:   $('#userName').val(),
                password:   $('#password').val(),
                email:      $('#email').val(),
                identity:   's'
            },
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            var result = data;
            if(result.ok === "true"){
                window.location.href = "/forLogin";
            }else{
                $('#registerTip').text(result.reason);
            }
        });
    }
}
function checkIsEmail() {
    var reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
    return reg.test($('#email').val());
}
