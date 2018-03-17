
//检查用户输入
function checkSaveFunc(jobName,industry,location,lowSalary,highSalary,deadline,jobDescribe,jobRequire) {

    if (jobName === null ||jobName.trim() === '' ) {
        $('#saveTip').text("请输入职位名称");
        return false;
    }else if(industry.length === 0){
        $('#saveTip').text("请勾选行业");
        return false;
    } else if (location === null||location.trim() === '') {
        $('#saveTip').text("请输入工作地点");
        return false;
    }else if(lowSalary === null ||lowSalary.trim() === ''||lowSalary<0){
        $('#saveTip').text("请输入有效的薪金");
        return false;
    }else if (highSalary === null ||highSalary.trim() ===''||highSalary<0) {
        $('#saveTip').text("请输入有效薪金");
        return false;
    }else if(deadline ===null ||deadline.trim()==='' ){
        $('#saveTip').text("请输入截止日期");
        return false;
    }else if (jobDescribe === null || jobDescribe.trim() ==='' ) {
        $('#saveTip').text("请输入工作职责");
        return false;
    }else if (jobRequire===null ||jobRequire.trim()==='' ){
        $('#saveTip').text("请输入职位要求");
        return false;
    }
    return true;
}
