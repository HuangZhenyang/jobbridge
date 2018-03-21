/**
 * Created by huangzhenyang on 2017/7/24.
 */

$(document).ready(function () {
    // getHeaderUserName();
   // getValidation();
});


/*
 *  @author: HuangZhenyang
 *  获取学生身份认证状态
 * */
function getValidation() {
    $.ajax({
        url:'/student/request_authentication',
        type:'get',
        dataType:'json'
    }).done(function (data) {
        var stuValidation = "";
        var spanColor = "";
        if(data.authentication === 'true'){ //如果已验证通过，插入Dom
            stuValidation = "&emsp;您已通过学生认证";
            spanColor = '<span style="color: black">';
            $('#mailbox').attr('disabled',true);
            $('#submitButton').attr('disabled',true); //设置按钮不能点
        }else{
            spanColor = '<span style="color: red">';
            stuValidation = "&emsp;您尚未通过学生认证";
        }
        var dom = '<div class="wrapper" data-jsx="3479566426">' +
            '<div class="detail" data-jsx="3479566426" style=" margin-left: 70px; ">' +
            '<div class="phone-num form-group" data-jsx="3479566426">' +
            '<div data-jsx="3479566426">' +
            '<span class="my-label">验证状态：'+ spanColor + stuValidation+'</span></span>'+
            '</div>'+
            '</div>'+
            '</div>'+
            '</div>';
        $('div.my-info').children('div.content').children('div.wrapper').before(dom);

    }).fail(function (xhr,status) {

    });
}



/*
*  @author: HuangZhenyang
*  填写邮箱提示toast
* */
$('#mailbox').on('click', function () {
    $('#submitTip').text('');
    $.toast({
        heading: '<strong>填写小贴士</strong>',
        text: ['学生邮箱用于确认您的学生身份哦','学生邮箱必须是以 <strong>.edu.cn</strong>结尾的哦','提交以后服务端会发送一封确认邮件到您的学生邮箱中哦'],
        icon: 'info',
        showHideTransition: 'plain', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba',
        //bgColor: '#13f6ff',
    });
});


/*
*  @author：HuangZhenyang
*  提交学生邮箱的函数
* */
function submitFunc() {
    var mailbox = $('#mailbox').val();
    if(checkStuEmail(mailbox)){
        $('#submitButton').attr('disabled',true);
        $.ajax({
            url: '/student/authentication',
            type: 'post',
            dataType:'json',
            data:{
                "mailbox":mailbox
            }
        }).done(function (data) {
            if(data.ok === true){
                $('#submitTip').text('提交成功，已发送邮件到您的邮箱');
            }else{
                $('#submitTip').text('提交失败: '+ data.reason);
            }
            $('#submitButton').removeAttr('disabled');
        }).fail(function (xhr,status) {
            $('#submitTip').text('提交失败');
        })
    }else{
        $('#submitTip').text('请填写有效的学生邮箱');
    }
}

/*
 * @author：HuangZhenyang
 *  确认邮箱格式正确函数
 */
function checkStuEmail(stuEmail) {
    return stuEmail.endsWith(".edu.cn");
}


