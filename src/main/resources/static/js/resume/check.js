/**
 * @author regaliastar
 * 使用装饰者模式，提高代码可复用性
 * @param {function} Function.before  函数插件
 * @param {function} window.checkRes  表单验证
 */

$(document).ready(function(){

    //若函数返回false，则终止，否则继续执行before前的函数
    //用法：function_1.before(function_2);
    //     先执行function_2,
    //     若function_2返回false，则不执行function_1,
    //     否则，继续执行function_1
    Function.prototype.before = function(beforefn){
        var _self = this;
        return function(){
            if(beforefn.apply(this,arguments) === false){
                //不再执行后面的函数
                return;
            }
            return _self.apply(this,arguments);
        }
    }

    var validata = function(resume){
        if(resume.name === ''){
            alert('用户名未填写');
            return false;
        }
        if(resume.address === ''){
            alert('地址未填写');
            return false;
        }
        if(resume.phone === ''){
            alert('电话未填写');
            return false;
        }
        if(resume.mail === ''){
            alert('邮箱未填写');
            return false;
        }
    }

    window.checkRes = function(resume){
        return validata(resume);
    }
})
