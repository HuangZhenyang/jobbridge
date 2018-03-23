/**
 * Created by huangzhenyang on 2017/7/27.
 */

$(document).ready(function () {
    console.log("companyReceivedResumeDetail.js");
    getData();
});


/*
 * @author:Huangzhenyang
 * 获取url中的参数
 * */
function getUrlParameter(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}


/*
 * @author:Huang Zhenyang
 * 获取简历信息
 * */
function getData() {
    //let resumeSendId = getUrlParameter('id');
    let resumeSendId = $('#resumeSendId').text();
    console.log(">>> resumeSendId"+resumeSendId);
    $.ajax({
        url: '/company/resume_received/resume',
        type: 'post',
        dataType: 'json',
        data: {
            "resumeSendId": resumeSendId,
            "operation": "requestResume"
        }
    }).done(function (data) {
        console.log(JSON.stringify(data));
        setData(data);
    }).fail(function (xhr, status) {

    });
}


/*
 * @author:Huang Zhenyang
 * 填充简历信息
 * */
function setData(data) {
    //个人信息
    let perInfo_name = data.name;
    let perInfo_address = data.address;
    let perInfo_phone = data.phone;
    let perInfo_mail = data.mail;
    //教育背景
    let edus = data.edus; //数组
    let edusLen = edus.length;
    //工作经历
    let works = data.works;
    let worksLen = works.length;
    //领导经验
    let leader = data.leader;
    let organization = leader.organization;
    let club = leader.club;
    let organizationLen = organization.length;
    let clubLen = club.length;
    //技能&兴趣
    let skill = data.skill;


    //填充数据
    //个人信息
    $('div.name').html(perInfo_name);
    $('div.address').html(perInfo_address);
    $('div.phone').html(perInfo_phone);
    $('div.mail').html(perInfo_mail);

    //教育背景  先插dom,再插数据
    if (edusLen > 1) {
        //插入dom
        insertDomIntoEdus(edusLen);
    }
    insertIntoEdus(edus);

    //工作经历 先插dom,再插数据
    if (worksLen > 1) {
        insertDomIntoWorks(worksLen);
    }
    insertIntoWorks(works);

    //领导经验 先插dom,再插数据 分为organization,club
    if (organizationLen > 1) {
        insertDomIntoOrganization(organizationLen);
    }
    insertIntoOrganization(organization);
    if (clubLen > 1) {
        insertDomIntoClub(clubLen);
    }
    insertIntoClub(club);

    //技能&兴趣
    $('div.language').html(skill.language);
    $('div.computer').html(skill.computer);
    $('div.hobby').html(skill.hobby);

}


/*********************************   教育背景  ****************************************/
/*
 * @author:Huang Zhenyang
 * 插入Dom到教育背景edus  插入eduLen-1个
 * */
function insertDomIntoEdus(eduLen) {
    for (let i = 0; i < eduLen - 1; i++) {
        $('.edus').children('.add-blank').last().after('<div class="row area add-blank first-blank" data-target="edu">' +
            '<div class="col-xs-9">' +
            $('.edus').children('.add-blank').find('.col-xs-9').html() +
            '</div>' +
            '<div class="col-xs-3 text-right">' +
            $('.edus').children('.add-blank').find('.col-xs-3 ').html() +
            '</div>' +
            '<div class="oper"> 								<div class="box">									<a class="minus" href="javascript:;"> 		<img src="/img/resume/minus.png" > 									</a> 								</div> 							</div>' +
            '</div>');

        //绑定减号事件
        $('.edus').find('div.oper').find('.minus').on('click', function () {
            $(this).parent().parent().parent().remove();
        });
    }
}
/*
 * @author:Huang Zhenyang
 * 插入数据到教育背景edus
 * */
function insertIntoEdus(edus) {
    $('.parent-edu').children('.add-blank').each(function (i) {
        $(this).find('div.school').html(edus[i].school); //pass
        $(this).find('div.city').html(edus[i].city); //pass
        $(this).find('div.province').html(edus[i].province); //pass
        $(this).find('div.college').html(edus[i].college); //pass
        $(this).find('div.end-time').html(edus[i].end_time); //pass
        $(this).find('div.grade').html(edus[i].grade); //pass
        $(this).find('div.honors').html(edus[i].honors); //字数大于3会出现br标签
        $(this).find('div.related_course').html(edus[i].related_course); //字数大于3会出现br标签
    });
}


/*********************************   工作经历  ****************************************/
/*
 * @author:Huang Zhenyang
 * 插入Dom到工作经历  插入worksLen-1个
 * */
function insertDomIntoWorks(worksLen) {

    for (let i = 0; i < worksLen - 2; i++) {
        $('.works').children('.add-blank').last().after('<div class="row area add-blank first-blank" data-target="work">' +
            '<div class="col-xs-9">' +
            $('.works').children('.add-blank').find('.col-xs-9').html() +
            '</div>' +
            '<div class="col-xs-3 text-right">' +
            $('.works').children('.add-blank').find('.col-xs-3').html() +
            '</div>' +
            '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png" > 								   </a> 																</div> 															</div>' +
            '</div>');

        //绑定减号事件
        $('.works').find('div.oper').find('.minus').on('click', function () {
            $(this).parent().parent().parent().remove();
        });
    }
}
/*
 * @author:Huang Zhenyang
 * 插入数据到工作经历 
 * */
function insertIntoWorks(works) {
    $('.parent-works').children('.add-blank').each(function (i) {
        if ($(this).parent().hasClass('deleted')) {
            return;
        }
        $(this).find('div.company').html(works[i].company);
        $(this).find('div.city').html(works[i].city);
        $(this).find('div.province').html(works[i].province);
        $(this).find('div.position').html(works[i].position);
        $(this).find('div.project').html(works[i].project);
        $(this).find('div.start_time').html(works[i].start_time);
        $(this).find('div.end_time').html(works[i].end_time);
        $(this).find('div.sentence_1').html(works[i].sentence_1);
        $(this).find('div.sentence_2').html(works[i].sentence_2);
        $(this).find('div.sentence_3').html(works[i].sentence_3);
        $(this).find('div.sentence_4').html(works[i].sentence_4);
    });
}


/*********************************   领导经验  ****************************************/
/*
 * @author:Huang Zhenyang
 * 插入Dom到组织
 * */
function insertDomIntoOrganization(organizationLen) {
    for (let i = 0; i < organizationLen - 1; i++) {
        $('.leader').children('.organization').last().after('<div class="row area add-blank first-blank organization" data-target="practice">' +
            '<div class="col-xs-9">' +
            $('.leader').children('.organization').find('.col-xs-9').html() +
            '</div>' +
            '<div class="col-xs-3 text-right">' +
            $('.leader').children('.organization').find('.col-xs-3').html() +
            '</div>' +
            '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png"> 								   </a> 																</div> 															</div>' +
            '</div>');

        //绑定减号事件
        $('.leader').find('.organization').find('div.oper').find('.minus').on('click', function () {
            $(this).parent().parent().parent().remove();
        });
    }
}
/*
 * @author:Huang Zhenyang
 * 插入数据到组织
 * */
function insertIntoOrganization(organization) {
    $('.leader').children('div.organization').each(function (i) {
        $(this).find('div.name').html(organization[i].name);
        $(this).find('div.position').html(organization[i].position);
        $(this).find('div.start_time').html(organization[i].start_time);
        $(this).find('div.end_time').html(organization[i].end_time);
        $(this).find('div.sentence_1').html(organization[i].sentence_1);
        $(this).find('div.sentence_2').html(organization[i].sentence_2);
        $(this).find('div.sentence_3').html(organization[i].sentence_3);
    })
}
/*
 * @author:Huang Zhenyang
 * 插入Dom到俱乐部
 * */
function insertDomIntoClub(clubLen) {
    for (let i = 0; i < clubLen; i++) {
        $('.leader').children('.club').last().after('<div class="row area add-blank first-blank club" data-target="practice">' +
            '<div class="col-xs-9">' +
            $('.leader').children('.club').find('.col-xs-9').html() +
            '</div>' +
            '<div class="col-xs-3 text-right">' +
            $('.leader').children('.club').find('.col-xs-3').html() +
            '</div>' +
            '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png"> 								   </a> 																</div> 															</div>' +
            '</div>');

        //绑定减号事件
        $('.leader').find('.club').find('div.oper').find('.minus').on('click', function () {
            $(this).parent().parent().parent().remove();
        });
    }
}
/*
 * @author:Huang Zhenyang
 * 插入数据到俱乐部
 * */
function insertIntoClub(club) {
    $('.leader').children('div.club').each(function (i) {
        $(this).find('div.name').html(club[i].name);
        $(this).find('div.position').html(club[i].position);
        $(this).find('div.start_time').html(club[i].start_time);
        $(this).find('div.end_time').html(club[i].end_time);
        $(this).find('div.sentence_1').html(club[i].sentence_1);
        $(this).find('div.sentence_2').html(club[i].sentence_2);
    })
}


/*
 * @author:Kelv1nYu
 * 告诉后台hr通过了该简历
 * */
function approveResume() {
    var resumeSendId = $("#resumeSendId").text();
    //alert(resumeSendId);
    $.ajax({
        url:'/company/resume_received/resume',
        type:'POST',
        dataType:'json',
        data:{
            "resumeSendId": resumeSendId,
            "operation": "sendEmail"
        }
    });
}


