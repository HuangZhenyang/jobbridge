/**
 * Created by huangzhenyang on 2017/7/24.
 */


/*
*  @author: HuangZhenyang
*   学生退出函数
*   封装在此，可复用
*   2017/7/24
* */
function stuLogout() {
    $.ajax({
        url: '/exit',
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        //跳到学生的主页
        if(data.ok === true){
            window.location.href = "/";
        }
        console.log("成功收到数据：" + JSON.stringify(data));
    }).fail(function (xhr,status) {
        console.log(xhr+" "+status);
    });
}

/*
 *  @author: HuangZhenyang
 *   企业退出函数
 *   封装在此，可复用
 *   2017/7/24
 * */
function comLogout() {
    $.ajax({
        url: '/company/exit',
        type: 'get',
        dataType: 'json'
    }).done(function (data) {
        //跳到企业主页
        console.log("成功收到数据：" + JSON.stringify(data));
    }).fail(function (xhr,status) {
        console.log(xhr+" "+status);
    });
}