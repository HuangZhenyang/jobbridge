/*
 *       检查用户输入的规范
 */
function checkSignUpFunc() {
    if ($('#userName').val() === null || $('#userName').val() === '') {
        alert("请输入用户名");
        return false;
    }else if ($('#mailbox').val() === null || $('#mailbox').val() === '') {
        alert("请输入邮箱");
        return false;
    }else if (!checkIsEmail()) {
        alert("请输入正确的邮箱");
        return false;
    } else if ($('#password').val() === null || $('#password').val() === '') {
        alert("请输入密码");
        return false;
    } else if(!($('#password').val().length>=6 && $('#password').val().length<=16)){
        alert("密码不能少于6个字符或多于16个字符");
        return false;
    }else if ($('#password1').val() === null || $('#password1').val() === '') {
        alert("请再次输入密码");
        return false;
    } else if (!($('#password').val() === $('#password1').val())) {
        alert("两次输入的密码不一致");
        return false;
    }
    return true;
}

function signUpFunc(){
    if(checkSignUpFunc()){
        let formdata = new FormData();
        formdata.append('userName',$('#userName').val());
        formdata.append('mailbox',$('#mailbox').val());
        formdata.append('password',$('#password').val());
        formdata.append('identity','s');
        $.ajax({
            url: '/sign_up',
            type: 'post',
            data: formdata,
            async: false,
            processData : false,
            contentType : false,
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            let result = data;
            if(result.ok === "true"){
                window.location.href = "/sign_in";
            }else{
                alert(result.reason);
            }
        });
        // $.ajax({
        //     url: '/sign_up',
        //     type: 'post',
        //     dataType: 'json',
        //     data: {
        //         userName:   $('#userName').val(),
        //         password:   $('#password').val(),
        //         mailbox:    $('#mailbox').val(),
        //         identity:   's'
        //     },
        // }).done(function (data) {
        //     console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
        //     let result = data;
        //     if(result.ok === "true"){
        //         window.location.href = "/sign_in";
        //     }else{
        //         alert(result.reason);
        //     }
        // });
    }
}
function checkSignUpCompanyFunc() {
    if ($('#userNameCompany').val() === null || $('#userNameCompany').val() === '') {
        alert("请输入用户名");
        return false;
    }else if ($('#mailboxCompany').val() === null || $('#mailboxCompany').val() === '') {
        alert("请输入邮箱");
        return false;
    }else if (!checkIsEmailCompany()) {
        alert("请输入正确的邮箱");
        return false;
    } else if ($('#passwordCompany').val() === null || $('#passwordCompany').val() === '') {
        alert("请输入密码");
        return false;
    } else if(!($('#passwordCompany').val().length>=6 && $('#passwordCompany').val().length<=16)){
        alert("密码不能少于6个字符或多于16个字符");
        return false;
    }else if ($('#password1Company').val() === null || $('#password1Company').val() === '') {
        alert("请再次输入密码");
        return false;
    } else if (!($('#passwordCompany').val() === $('#password1Company').val())) {
        alert("两次输入的密码不一致");
        return false;
    } else if($('#companyName').val() === null || $('#companyName').val() === ''){
        alert("请输入企业名称");
        return false;
    } else if($('#tel').val() === null || $('#tel').val() === ''){
        alert("请输入联系方式");
        return false;
    } else if($('#industry').val() === null || $('#industry').val() === ''){
        alert("请选择企业类型");
        return false;
    } else if($('#company_introduction').val() === null || $('#company_introduction').val() === ''){
        alert("请输入企业描述信息");
        return false;
    } else if($('#icon_address').val() === null || $('#icon_address').val() === ''){
        alert("请添加企业logo图片");
        return false;
    }
    return true;
}
function signUpCompanyFunc(){
    if(checkSignUpCompanyFunc()){
        let formdata = new FormData();
        let img_file = $('#icon_address')[0].files[0];
        formdata.append('userName',$('#userNameCompany').val());
        formdata.append('name',$('#companyName').val());
        formdata.append('mailbox',$('#mailboxCompany').val());
        formdata.append('phoneNum',$('#tel').val());
        formdata.append('password',$('#passwordCompany').val());
        formdata.append('companyIntroduction',$('#company_introduction').val());
        formdata.append('img_file',img_file);
        formdata.append('industryId',$('#industry').val());
        formdata.append('identity','e');
        $.ajax({
            url: '/sign_up',
            type: 'post',
            data: formdata,
            async: false,
            processData : false,
            contentType : false,
        }).done(function (data) {
            console.log('成功, 收到的数据: ' + JSON.stringify(data, null, '  '));
            let result = data;
            if(result.ok === "true"){
                window.location.href = "/sign_in";
            }else{
                alert(result.reason);
            }
        });
    }
}
function checkIsEmail() {
    let reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
    return reg.test($('#mailbox').val());
}
function checkIsEmailCompany() {
    let reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
    return reg.test($('#mailboxCompany').val());
}


/*个人注册和企业注册点击切换    author:帅比紫韦浪浪*/
$("#panel ul li a").click(function(){
    var id=this.name;
    $(id).show();
    if(id==="#tab-2"){
        $("#tab-1").hide();
    } else if(id==="#tab-1"){
        $("#tab-2").hide();
    }
})