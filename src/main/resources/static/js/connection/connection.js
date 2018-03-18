function setData(data) {
    $('#connection-list').children().remove();
    let userName ="test";
    $('#userName').append(userName);
    $('#number').text(10);
    let name ="廖文斌";
    let beizhu ="四川大学";
    let dom= "";
    let eachDom ="";

    eachDom ="<div class=\"mn-pymk-list__card display-flex flex-column\">"+ "<div class=\"pymk-card ember-view\">" + "<div class=\"pt3 ember-view\">" +
        "<img class=\"lazy-image EntityPhoto-circle-7 loaded\"  src=\"assets/img/index/bg1.jpg\">" +" </div>" +
        "<div class=\"pymk-card__details pv0 ph2 text-align-center mt1\">" + "<span class=\"pymk-card__name pymk-card__name--card-layout block m0 Sans-15px-black-85%-semibold\">" + name
        + "</span>" + "<span class=\"pymk-card__occupation pymk-card__occupation--card-layout block m0 Sans-13px-black-55%\">" + beizhu +"</span>" +"</div>"
        +"<div class=\"pymk-card__action-container pt1 pb3 ph0\">" +"<button  class=\"button-secondary-small\">"
        + "<span aria-hidden=\"true\" >" +"加为好友"+"</span>" +"</button>" +"</div>"+"</div>" +"</div>";
    dom += eachDom;
    $('#connection-list').append(dom);
}