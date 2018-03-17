/**
 * Created by huangzhenyang on 2017/7/22.
 */


/*
*   @author HuangZhenyang
*   检查用户输入规范
* */
function check(phone,school,major,grade,city,industry,func) {
    let re = /^1\d{10}$/;
    if(phone.trim() === ''){
        $('#saveTip').text("请填写手机号");
        return false;
    }
    if(!(re.test(phone))){
        $('#saveTip').text("请填写有效手机号");
        return false;
    }
    if(school.trim() === ''){
        $('#saveTip').text("请填写学校");
        return false;
    }
    if(major.trim()===''){
        $('#saveTip').text("请填写专业");
        return false;
    }
    if(grade.trim()===''){
        $('#saveTip').text("请填写年级");
        return false;
    }
    if(city.length===0){
        $('#saveTip').text("请勾选意向城市");
        return false;
    }
    if(industry.length===0){
        $('#saveTip').text("请勾选意向行业");
        return false;
    }
    if(func.length===0){
        $('#saveTip').text("请勾选意向职能");
        return false;
    }
    $('#saveTip').text("");
    return true;
}
