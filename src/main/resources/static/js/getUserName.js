/**
 * Created by huangzhenyang on 2017/7/27.
 */

/*
* @author:HuangZhenyang
* 获取用户名
* */
function getHeaderUserName() {
    $.ajax({
        url:'/username',
        type:'get',
        dataType:'json'
    }).done(function (data) {
        setHeaderUserName(data);
    }).fail(function (xhr,status) {

    });
}


/*
 * @author:HuangZhenyang
 * 设置用户名
 * */
function setHeaderUserName(data) {
    $('#header-userName').text(data.username);
}
