'use strict';

/**
 * @author regaliastar
 * 用于服务器的上传操作
 * tips: 未测试
 * @param {function} Resume.prototype.setResume -- 用于得到整张表单数据
 */

(function () {
    class Resume {
        /**
         * @constructor
         * @param {string[]} edus
         * @param {string[]} works
         * 使用单例模式，防止实例化多个类
         */
        constructor() {
            if (Resume.instance) {
                throw new Error('不能重复实例化类！');
            }
            this.name = null,
                this.address = null,
                this.phone = null,
                this.mail = null,
                this.edus = [],
                this.works = [],
                this.leader = {},
                this.leader.organization = [],
                this.leader.club = [],
                this.skill = {}
        }

        /**
         * @return {Resume} Resume.instance
         * 单例模式
         */
        static getInstance() {
            if (!Resume.instance) {
                Resume.instance = new Resume();
            }
            return Resume.instance;
        }

        /**
         * @param 打印信息，用于调试
         */
        print() {
            console.log(JSON.stringify(this));
        }

        /**
         * @return {String} this
         * 得到数据
         */
        getResume() {
            return JSON.stringify(this);
        }

        /**
         * 重置
         */
        reset() {
            this.name = null,
                this.address = null,
                this.phone = null,
                this.mail = null,
                this.edus = [],
                this.works = [],
                this.leader = {},
                this.leader.organization = [],
                this.leader.club = [],
                this.skill = {}
        }
    }


    /**
     * 初始化操作，包括按钮的绑定事件等
     * @see check.js 表单验证
     * @see function checkRes can be found in check.js
     */
    var formsubmit = function (r) {
        resume.saveResume(r);
        Avatar.saveImage();
    }

    Resume.prototype.init = function () {
        var _self = this;
        $('#saveResume').on('click', function () {
            _self.setResume();
            //String 转 JSON
            var r = JSON.parse(_self.getResume());
            //alert(r.name);
            formsubmit = formsubmit.before(checkRes);
            formsubmit(r);
        });

        //@author HuangZhenyang
        // 绑定下载按钮事件
        $('#html2canvas').on('click', function () {
            _self.setResume();
            var r = JSON.parse(_self.getResume());
            download(r);
        });

        /**
         *  @author  hzy
         *  pdfmake 导出pdf
         */

        function download(r) {
            html2canvas(document.getElementById("preview180"), {
                // 渲染完成时调用，获得 canvas
                onrendered: function (canvas) {
                    // 从 canvas 提取图片数据
                    //var imgData = canvas.toDataURL('image/jpeg');   失败
                    //var imgData = cropper.getCroppedImageData(180, 180);  成功
                    // {image: imgData, width: 85, height: 105, style: 'text-right'},


                    var dd = {
                        content: [
                            {
                                text: r.name ||'黄振洋',
                                style: 'per_info_header'
                            }, {text: [r.address||'四川省成都市双流县666号' + "   ", r.phone||'17760471603' + "   ", r.mail||'745125931@qq.com' + "   "], style: 'text_center'},
                            /*{text: '教育背景'},
                             {canvas: [{type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1}]},

                             {text:r.edus[0].}*/
                        ],
                        defaultStyle: {
                            font: 'simsun'
                        },
                        styles: {
                            per_info_header: {
                                fontSize: 24,
                                alignment: 'center'
                            },
                            text_center: {
                                alignment: 'center'
                            },
                            text_right: {
                                alignment: 'right'
                            },
                            text_top: {
                                alignment: 'top'
                            }
                        }
                    };

                    dd.content.push({text: '教育背景'});
                    dd.content.push({canvas: [{type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1}]});
                    r.edus.forEach(function (i) {
                        dd.content.push({
                            columns: [
                                {
                                    // star-sized columns fill the remaining space
                                    // if there's more than one star-column, available width is divided equally
                                    width: '*',
                                    text: [i.school || '四川大学']
                                },
                                {
                                    // percentage width
                                    width: 'auto',
                                    text:[i.city || '成都'+",   ", i.province || '四川'],style:'text_right'
                                }
                            ],
                            // optional space between columns
                            columnGap: 10
                        });
                        dd.content.push({
                            columns: [
                                {
                                    // star-sized columns fill the remaining space
                                    // if there's more than one star-column, available width is divided equally
                                    width: '*',
                                    text: [i.college || '软件工程']
                                },
                                {
                                    // percentage width
                                    width: 'auto',
                                    text:[i.end_time || '2019.6'],style:'text_right'
                                }
                            ],
                            // optional space between columns
                            columnGap: 10
                        });
                        dd.content.push({text: ['成绩： ', i.grade || '4']});
                        dd.content.push({text: ['honors： ', i.honors || '西南地区第一adc,川大最后一张卡牌']});
                        dd.content.push({text: ['相关课程： ', i.related_course || '操作系统，系统级编程']});
                    });

                    //工作经历
                    dd.content.push({text: '工作经历'});
                    dd.content.push({canvas: [{type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1}]});
                    r.works.forEach(function (i) {
                        dd.content.push({
                            columns: [
                                {
                                    // star-sized columns fill the remaining space
                                    // if there's more than one star-column, available width is divided equally
                                    width: '*',
                                    text: [i.company || '阿里巴巴']
                                },
                                {
                                    // percentage width
                                    width: 'auto',
                                    text:[i.city || '成都'+",   ", i.province || '四川'],style:'text_right'
                                }
                            ],
                            // optional space between columns
                            columnGap: 10
                        });

                        dd.content.push({
                            columns: [
                                {
                                    // star-sized columns fill the remaining space
                                    // if there's more than one star-column, available width is divided equally
                                    width: '*',
                                    text: [i.position || '大前端' + " ", i.project || '淘宝']
                                },
                                {
                                    // percentage width
                                    width: 'auto',
                                    text:[i.start_time || '2015.6'+" - ", " - ",i.end_time || '2015.8'],style:'text_right'
                                }
                            ],
                            // optional space between columns
                            columnGap: 10
                        });
                        dd.content.push({ul: [i.sentence_1 || '负责UI设计以及前后端通信', i.sentence_2 || '负责系统架构以及任务分配', i.sentence_3 || '带领EF团队做完淘宝项目，实现每个月50%的增值', i.sentence_4 || '获得阿里妈妈最佳新人奖']});
                        dd.content.push({text:'   '});

                    });

                    //领导经验
                    dd.content.push({text: '领导经验'});
                    dd.content.push({canvas: [{type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1}]});
                        //学生社团
                    r.leader.organization.forEach(function (i) {
                        dd.content.push({
                            columns: [
                                {
                                    // star-sized columns fill the remaining space
                                    // if there's more than one star-column, available width is divided equally
                                    width: '*',
                                    text: [i.name || '三次元社',i.position||'会长']
                                },
                                {
                                    // percentage width
                                    width: 'auto',
                                    text:[i.start_time || '2015.2'+" - ", " - ", i.end_time || '2016.2'],style:'text_right'
                                }
                            ],
                            // optional space between columns
                            columnGap: 10
                        });
                        dd.content.push({ul: [i.sentence_1 || '在三次元驰骋', i.sentence_2 || '在三次元浪的飞起', i.sentence_3 || '三次元异世界']});
                        dd.content.push({text:'   '});
                    });
                        //学生俱乐部
                    r.leader.club.forEach(function (i) {
                        dd.content.push({
                            columns: [
                                {
                                    // star-sized columns fill the remaining space
                                    // if there's more than one star-column, available width is divided equally
                                    width: '*',
                                    text: [i.name || '乒乓球俱乐部',i.position||'部长']
                                },
                                {
                                    // percentage width
                                    width: 'auto',
                                    text:[i.start_time || '2015.2'+" - "," - ", i.end_time || '2016.2'],style:'text_right'
                                }
                            ],
                            // optional space between columns
                            columnGap: 10
                        });
                        dd.content.push({ul: [i.sentence_1 || '带领部员打进全国总决赛', i.sentence_2 || '获得第一名，与马琳握手']});
                        dd.content.push({text:'   '});
                    });

                    //技能&兴趣
                    dd.content.push({text: '技能&兴趣'});
                    dd.content.push({canvas: [{type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1}]});
                    dd.content.push({text:['语言',r.skill.language||'德语,法语']});
                    dd.content.push({text:['计算机',r.skill.computer||'熟悉java']});
                    dd.content.push({text:['兴趣爱好',r.skill.hobby||'游泳']});




                    //设置字体
                    pdfMake.fonts = {
                        Roboto: {
                            normal: 'Roboto-Regular.ttf',
                            bold: 'Roboto-Medium.ttf',
                            italics: 'Roboto-Italic.ttf',
                            bolditalics: 'Roboto-Italic.ttf'
                        },
                        simsun: {
                            normal: 'STSONG.ttf',
                            bold: 'STSONG.ttf',
                            italics: 'STSONG.ttf',
                            bolditalics: 'STSONG.ttf',
                        }
                    };
                    pdfMake.createPdf(dd).download("test.pdf");
                }
            });
        }

    }


    /**
     * 得到用户填表的信息
     */
    Resume.prototype.setResume = function () {
        this.reset();

        var _self = this;

        this.name = $('div.name').html() || '何干事'; //pass
        //alert('name: '+this.name);
        this.address = $('div.address').html()|| '西南地区'; //pass

        this.phone = $('div.phone').html()|| '8008208820'; //pass

        this.mail = $('div.mail').html()|| '555555@qq.com'; //pass

        this.skill.language = $('div.language').html()|| '汉语'; //pass

        this.skill.computer = $('div.computer').html()|| '熟悉java'; //pass

        this.skill.hobby = $('div.hobby').html()|| '看直播'; //pass

        //pass
        $('.parent-edu').children('.add-blank').each(function (edu) {
            var _edu = {};
            _edu.school = $(this).find('div.school').html()|| '四川大学'; //pass
            _edu.city = $(this).find('div.city').html()|| '曲靖'; //pass
            _edu.province = $(this).find('div.province').html()|| '云南'; //pass
            _edu.college = $(this).find('div.college').html()|| '软件工程'; //pass
            _edu.end_time = $(this).find('div.end-time').html()|| '2017-06-09'; //pass
            _edu.grade = $(this).find('div.grade').html()|| '4'; //pass
            _edu.honors = $(this).find('div.honors').html()|| '西南地区第一ADC'; //字数大于3会出现br标签
            _edu.related_course = $(this).find('div.related_course').html()|| '如何走A'; //字数大于3会出现br标签
            _self.edus.push(_edu);
        })
        //alert(JSON.stringify(this.edus));
        //var spe = '#|#';
        //pass
        $('.parent-works').children('.add-blank').each(function (work) {
            if ($(this).parent().hasClass('deleted')) {
                return;
            }
            var _work = {};
            _work.company = $(this).find('div.company').html()|| '阿里妈妈';
            _work.city = $(this).find('div.city').html()|| '杭州';
            _work.province = $(this).find('div.province').html()|| '浙江';
            _work.position = $(this).find('div.position').html()|| '总监';
            _work.project = $(this).find('div.project').html()|| '淘宝';
            _work.start_time = $(this).find('div.start_time').html()|| '2015-06-06';
            _work.end_time = $(this).find('div.end_time').html()|| '2016-06-06';
            _work.sentence_1 = $(this).find('div.sentence_1').html()|| '负责运营';
            _work.sentence_2 = $(this).find('div.sentence_2').html()|| '负责设计';
            _work.sentence_3 = $(this).find('div.sentence_3').html()|| '带领团队实现胜利';
            _work.sentence_4 = $(this).find('div.sentence_4').html()|| '每个月50%产值增加';
            _self.works.push(_work);
        });
        //alert(JSON.stringify(_self.works));
        //pass
        $('.leader').children('div.organization').each(function (org) {
            var _organization = {};
            _organization.name = $(this).find('div.name').html()|| '戏剧社';
            _organization.position = $(this).find('div.position').html()|| '社长';
            _organization.start_time = $(this).find('div.start_time').html()|| '2015-03-02';
            _organization.end_time = $(this).find('div.end_time').html()|| '2015-09-09';
            _organization.sentence_1 = $(this).find('div.sentence_1').html()|| '表演话剧';
            _organization.sentence_2 = $(this).find('div.sentence_2').html()|| '带领大家走向全国大赛';
            _organization.sentence_3 = $(this).find('div.sentence_3').html()|| '第一名';
            _self.leader.organization.push(_organization);
        })
        //alert(JSON.stringify(this.leader.organization));
        //pass
        $('.leader').children('div.club').each(function (clu) {
            var _club = {};
            _club.name = $(this).find('div.name').html()|| '嗨爆俱乐部';
            _club.position = $(this).find('div.position').html()|| '部长';
            _club.start_time = $(this).find('div.start_time').html()|| '2015-03-03';
            _club.end_time = $(this).find('div.end_time').html()|| '2015-09-09';
            _club.sentence_1 = $(this).find('div.sentence_1').html()|| '俱乐部';
            _club.sentence_2 = $(this).find('div.sentence_2').html()|| '很好';
            _self.leader.club.push(_club);
        })
        //alert(JSON.stringify(this.leader.club));
    }

    /**
     * 保存信息，发送到服务器
     */
    //TODO:发送数据到服务器
    Resume.prototype.saveResume = function (INSTANCE) {
        var data = INSTANCE;
        data = JSON.stringify(data);
        $.ajax({
            url: '/student/resume',
            type: 'post',
            data: {
                "content":data
            },
            success: function () {

            },
            error: function (err) {
                console.log(err);
            }
        })
    }


    var cs = function () {
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        window.ActiveXObject ? Sys.ie = ua.match(/msie ([\d.]+)/)[1] :
            document.getBoxObjectFor ? Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1] :
                window.MessageEvent && !document.getBoxObjectFor ? Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1] :
                    window.opera ? Sys.opera = ua.match(/opera.([\d.]+)/)[1] :
                        window.openDatabase ? Sys.safari = ua.match(/version\/([\d.]+)/)[1] : 0;

        //浏览器不支持
        if (Sys.ie) {
            $('#waring').show();
        }
        if (Sys.firefox) {
            $('#waring').show();
        }
        if (Sys.safari) {
            $('#waring').show();
        }
    }

    //若浏览器不支持，则给出提示
    //cs();
    $('#waring_close').on("click", function () {
        $('#waring').hide();

    })


    /**
     * @param {function} init
     * @param {function} rotateImage
     * @param {function} saveImage
     * @param {function} window.selectImage
     *
     * @see imagecropper.js
     */
    var cropper;

    class Avatar {
        /**
         * @constructor
         */
        constructor() {
            throw new Error('Avatar 不能使用构造函数');
        }
    }
    Avatar.init = function () {
        //绑定
        cropper = new ImageCropper(600, 600, 340, 420);
        cropper.setCanvas("cropper");
        cropper.addPreview("preview180");

        //检测用户浏览器是否支持imagecropper插件
        if (!cropper.isAvaiable()) {
            alert("Sorry, your browser doesn't support FileReader, please use Firefox3.6+ or Chrome10+ to run it.");
        }
    }

    Avatar.rotateImage = function (e) {
        switch (e.target.id) {
            case "rotateLeftBtn":
                cropper.rotate(-90);
                break;
            case "rotateRightBtn":
                cropper.rotate(90);
                break;
        }
    }

    Avatar.saveImage = function () {
        //选个需要的大小
        var imgData = cropper.getCroppedImageData(180, 180);
        console.log("上传了：" + imgData);
        //alert("上传了：" + imgData);
        //上传代码
        $.ajax({
            type: 'post',
            url: '/site/avatar',
            data: {
                imgData: imgData
            },
            success: function (data) {

            },
            error: function (err) {
                console.log("error..." + err);

            }
        })
    }

    Avatar.selectImage = function (fileList) {
        $('#oldAvatar').addClass('hide');
        $('#big-avatar').addClass('hide');
        var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

        if (!rFilter.test(fileList[0].type)) {
            alert("文件格式不正确");
            return;
        } else {
            //隐藏预览图片，显示裁剪图片
            document.getElementById('uploadCoverImg').style.display = 'none';
            document.getElementById('preview180').style.display = 'block';
            document.getElementById('showModal').click();
            cropper.loadImage(fileList[0]);
        }
    }

    //打开本地图片
    window.selectImage = Avatar.selectImage;

    var resume = Resume.getInstance();
    resume.init();
    Avatar.init();

})();


/**
 *  @author  hzy
 *  简历填写提示 jquery toast.    鼠标点击div的时候出现提示
 */
//个人信息
$('.resume-uname').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(个人信息)',
        text: ['只需要 姓名 住址 电话 邮箱，其他都不需要，除了有的要特殊要求附上照片'],
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

//地址
$('.address').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(个人信息)',
        text: ['地址无明确要求的情况下写现住址到街道级就好，如四川省成都市高新区益州大道'],
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


//电话
$('.phone').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(个人信息)',
        text: ['电话号码格式为 131-xxxx-xxxx'],
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

//邮箱地址
$('.mail').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(个人信息)',
        text: ['邮箱建议不要用 QQ 邮箱，QQ 邮箱给人感觉很娱乐化，最好开通一个专门找工作的号，可以用 Foxmail/Gmail 正式一点的邮箱，邮箱地址推荐使用自己的名称相关的,例如:Xiaoer.Wang@foxmail.com 这样你发送邮件过去 HR 会在几千封邮件里一眼锁定你的邮件'],
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


//                       教育背景
$('#edu-background-title').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(教育背景)',
        text: ['教育经历按照时间倒叙依次填写，时间最近的放在第一栏'],
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

//相关课程
$('.related_course').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(教育背景)',
        text: ['相关课程不要把所学的所有课程写上，写和岗位需求和岗位职能相关的课程，例：应聘设计 就要写 CAD PS 等，做贷款类就写风控相关'],
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


//                      工作经历
$('#work-experience-title').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(工作经历)',
        text: ['按照时间倒叙的方式来写工作履历，时间最近的放在最开头'],
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

//工作经验描述
$('.parent-experience-wdesc').find('.sentence_1').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(工作经历)①',
        text: ['在写每一个 bullet point 的时候必须借助数据来量化你所做事情的成果，学会用数据化的语言描述内容。必须清楚你想要的每个点表达了什么特质或者素质。不要写了 3 点都是一个意思。列如：做了什么事使得公司销售额达到 100 万等/我服务了 300+客户/我审计了一个$500m 的公司等'],
        icon: 'info',
        showHideTransition: 'plain', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba'
    });
});

$('.parent-experience-wdesc').find('.sentence_2').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(工作经历)②',
        text: ['写每一个 bullet point 的时候最好以动词开头，尽量按照 STAR 原则来描述每一个经历即Situation（情况）、Task（任务）、Action（行动）、Result（结果）比如作为某活动的负责人需要为公司做拉新活动，在一个月内，带领一个 5 个人的团队，通过 xxx 方法，取得了10000+新增的成绩'],
        icon: 'info',
        showHideTransition: 'slide', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba'
    });
});

$('.parent-experience-wdesc').find('.sentence_3').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(工作经历)③',
        text: ['在每一个 bullet point 中要尽量包含或者展现 key words，因为越是针对性强的简历越是容易受到认可，甚至大公司直接采用机器筛选简历，如果你的简历中包含他们设置的关键词才能有机会被 HR 看到，否则直接 PASS，所以我们必须学会提炼出 JD 里面的 Key word。每个岗位都有自己对技能的关键词，如 Consulting 需要 Market research;Financial industry 的需要 Analytical skill'],
        icon: 'info',
        showHideTransition: 'fade', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba'
    });
});

//                          领导经验
$('.leader').find('h4').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(领导经验)',
        text: ['挑选能够体现个人能力的实践经历，可以是学校社团工作、校内比赛和活动、校外兼职经历，也是按照 STAR+量化成果的方式去写',
            '如果有正式工作经验和实习那么可以将实习放在社会实践栏和社团放在一起，如果没有正式工作，则将实习经历放在工作经验栏',
            '实践的内容最好也是和求职岗位需求相关的'],
        icon: 'info',
        showHideTransition: 'slide', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba',
        //bgColor: '#13f6ff',
    });
});


//                            技能&兴趣
//语言
$('.skill').find('.language').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(技能&兴趣)',
        text: ['语言栏建议填写掌握的语言和熟练程度（以证书或分数证明），如 英语（熟练，CET-6 640 分）；法语（熟练）'],
        icon: 'info',
        showHideTransition: 'fade', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba'
    });
});

//技能
$('.skill').find('.computer').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(技能&兴趣)',
        text: ['技能栏建议填写与所申请岗位相关的技能，有的时候 JD 里面会明确要求。列如互联网运营类工作可能涉及 PS 熟练、Xmind 精通'],
        icon: 'info',
        showHideTransition: 'fade', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba'
    });
});

//兴趣
$('.skill').find('.hobby').on('click', function () {
    $.toast({
        heading: '<strong>jobbridge小贴士</strong>(技能&兴趣)',
        text: ['兴趣栏也是写和职位技能素养需求相关的，并尽量量化。比如你应聘体育类工作，你可以写你喜欢运动，跑过几个城市的马拉松'],
        icon: 'info',
        showHideTransition: 'fade', //fade,slide,plain
        hideAfter: 60000, //false
        stack: 1, //the max number of toast, or false == 1
        position: 'bottom-right',
        loader: true,
        loaderBg: '#09b3ba'
    });
});


/*
 *  加号减号的代码
 *  @author: hzy
 */
//                  ***  教育背景  ***
//绑定加号事件
$('.edus').find('div.oper').find('a.add').on('click', function () {
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
});


//                  ***  工作经历  ***
//绑定加号事件
$('.works').find('div.oper').find('a.add').on('click', function () {
    $('.works').children('.add-blank').last().after('<div class="row area add-blank first-blank" data-target="work">' +
        '<div class="col-xs-9">' +
        $('.works').children('.add-blank').find('.col-xs-9').html() +
        '</div>' +
        '<div class="col-xs-3 text-right">' +
        $('.works').children('.add-blank').find('.col-xs-3').html() +
        '</div>' +
        '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png" th:src="@{/img/resume/minus.png}"> 								   </a> 																</div> 															</div>' +
        '</div>');

    //绑定减号事件
    $('.works').find('div.oper').find('.minus').on('click', function () {
        $(this).parent().parent().parent().remove();
    });
});


//                  ***  领导经验  ***
//绑定加号事件   学生社团
$('.leader').find('.organization').find('div.oper').find('a.add').on('click', function () {
    $('.leader').children('.organization').last().after('<div class="row area add-blank first-blank organization" data-target="practice">' +
        '<div class="col-xs-9">' +
        $('.leader').children('.organization').find('.col-xs-9').html() +
        '</div>' +
        '<div class="col-xs-3 text-right">' +
        $('.leader').children('.organization').find('.col-xs-3').html() +
        '</div>' +
        '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png" th:src="@{/img/resume/minus.png}"> 								   </a> 																</div> 															</div>' +
        '</div>');

    //绑定减号事件
    $('.leader').find('.organization').find('div.oper').find('.minus').on('click', function () {
        $(this).parent().parent().parent().remove();
    });
});

//绑定加号事件   俱乐部
$('.leader').find('.club').find('div.oper').find('a.add').on('click', function () {
    $('.leader').children('.club').last().after('<div class="row area add-blank first-blank club" data-target="practice">' +
        '<div class="col-xs-9">' +
        $('.leader').children('.club').find('.col-xs-9').html() +
        '</div>' +
        '<div class="col-xs-3 text-right">' +
        $('.leader').children('.club').find('.col-xs-3').html() +
        '</div>' +
        '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png" > 								   </a> 																</div> 															</div>' +
        '</div>');

    //绑定减号事件
    $('.leader').find('.club').find('div.oper').find('.minus').on('click', function () {
        $(this).parent().parent().parent().remove();
    });
});









/**
 * Created by huangzhenyang on 2017/7/27.
 */

$(document).ready(function () {
    getData();
});


/*
 * @author:Huangzhenyang
 * 获取url中的参数
 * */
function getUrlParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}


/*
 * @author:Huang Zhenyang
 * 获取简历信息
 * */
function getData() {
   // let resumeId = getUrlParameter('id') ||;

    $.ajax({

        url: '/student/request_resume',
        type: 'get',
        dataType: 'json',
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
            '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png" > 								   </a> 																</div> 															</div>' +
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
            '<div class="oper">                                                         <div class="box">									                        <a class="minus" href="javascript:;"> 										<img src="/img/resume/minus.png" > 								   </a> 																</div> 															</div>' +
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






