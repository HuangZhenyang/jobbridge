/**
 * 检查用户输入的规范
 * */
function checkInputFunc() {
    if ($('#userName').val() === null || $('#userName').val() === '') {
        $('#addAdminTip').text("请输入账户 ");
        return false;
    } else if ($('#password').val() === null || $('#password').val() === '') {
        $('#addAdminTip').text("请输入密码");
        return false;
    } else if(!($('#password').val().length>=6 && $('#password').val().length<=16)){
        $('#addAdminTip').text("密码不能少于6个字符或多于16个字符");
        return false;
    }
    return true;
}

/**
 * @author: HuangZhenyang
 * 添加管理员的函数
 * */
function addAdminFunc(){
    if(checkInputFunc()){
        $.ajax({
            url: '/signUp',
            type: 'post',
            dataType: 'json',
            data: {
                userName: $('#userName').val(),
                password: $('#password').val(),
                identity: 'a'
            }
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            let result = data;
            if(result.ok === "true"){
                //window.location.href = "/adminPage/addCompany";
            }else{
                $('#addAdminTip').text(result.reason);
            }
        });
    }
}