function loginFunc() {
    if (checkLoginInput()) {
        $.ajax({
            type: 'post',
            url: '/login',
            dataType: 'json',
            data: {
                userName: $('#userName').val(),
                passWord: $('#password').val()
            }
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            var result = data;
            if (result.ok === "true") {
                if(result.identity === "s"){
                    window.location.href = "/recruitinfo";
                }else if(result.identity === "a"){
                    window.location.href = "/adminPage/addEnterprise";
                }else if(result.identity === "e"){
                    window.location.href = "/enterprise/info";
                }

            } else {
                $('#loginTip').text(result.reason);
                console.log("登录失败");
            }
        }).fail(function (xhr, status) {
            console.log('失败: ' + xhr.status + ', 原因: ' + status);
        });
    }
}

/*
 *     检查用户输入规范
 */
function checkLoginInput() {
    if ($('#userName').val() === null || $('#userName').val() === '') {
        $('#loginTip').text("请输入用户名");
        return false;
    } else if ($('#password').val() === null || $('#password').val() === '') {
        $('#loginTip').text("请输入密码");
        return false;
    } else if(!($('#password').val().length>=6 && $('#password').val().length<=16)){
        $('#loginTip').text("密码不能少于6个字符或多于16个字符");
        return false;
    }
    return true;
}
