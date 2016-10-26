/**
 * Created by DinhTruong on 9/24/2016.
 */

$(document).ready(function(){
    //초기 변수 설정
    var overlay = $(".overlay");
    var loginModal = $(".loginModal");
    var joinModal = $(".joinModal");
    var popup = $(".popup");

    var subway = {
        url: "/static/admin/include/js/subway.js",
        list: {
            maxNumberOfElements: 10,
            match:{
                enabled: true
            }
        }
    };
    //시, 도 Select

    //처음에 팝업창 띄우기

    $(".popup .exit").click(function(e){
        $(this).parents(".popup").removeClass("active");
        overlay.removeClass("active");
    });

    //로그인 창 띄우는 버튼
    $(".loginButton").click(function(e){
        overlay.addClass("active");
        joinModal.removeClass("active");
        $(window).scrollTop(0);
        loginModal.addClass("active");
    });

    //회원가입 창 띄우는 버튼
    $("#joinButton").click(function(e){
        overlay.addClass("active");
        joinModal.addClass("active");
    });

    //로그인 버튼



    //회원가입 버튼
    $("#joinConfirmButton").click(function(e){
        var email = $("input[name=joinEmail]").val();
        var password = $("input[name=joinPassword]").val();
        var passConf = $("input[name=joinPassConf]").val();
        var policy = $("#c1").prop("checked");
        var privacy = $("#c2").prop("checked");
        var place = $("#c3").prop("checked");

        if(email == ""){
            alert("이메일을 입력해주세요.");
            return false;
        }

        if(password == ""){
            alert("비빌번호를 입력해주세요.");
            return false;
        }
		
		
        if(passConf == ""){
            alert("비밀번호확인을 입력해주세요.");
            return false;
        }

		
		
        if(policy == false){
            alert("이용약관에 동의해주세요.");
            return false;
        }
        if(privacy == false){
            alert("개인정보 취급방침에 동의해주세요.");
            return false;
        }

        if(place == false){
            alert("위치기반서비스 이용약관에 동의해주세요.");
            return false;
        }
		if(password != passConf){
			$("#passwordMessage").html("Password do not match");
			return false;
		}
        //TODO : Ajax 통신
        $.ajax({
            url : "/member-join",
            type: "POST",
            data:{
                "email" : email,
                "password": password,
                "passwordConfirm": passConf
            },
            success: function(data){
                if(data == "SUCCESS"){
                    alert("Success!");
                    $.ajax({
                        url:"/login",
                        type:"POST",
                        data: {
                            "username": email,
                            "password": password,

                        },
                        success: function(data){
                            if(data == 'login_success'){
                                top.location.href="/";
                            }else{
                                alert("FAIL!");
                            }
                        }
                    });
                }else{
                    $("#helpBlockMessage").html(data);
                    $("input[name=joinPassword]").val("");
                    $("input[name=joinPassConf]").val("");
                    $("#passBlockMessage").html("");
                    $("input[name=joinEmail]").focus();
                }
            }

        });
    });



    $("#loginConfirmButton").click(function(e){
        var data = null;
        if($("#rememberme").prop('checked')){
           data = {
               "username": $("#username").val(),
               "password": $("#password").val(),
               "rememberme":$("#rememberme").val()};
        }else{
           data={
               "username": $("#username").val(),
               "password": $("#password").val()};
        }
        $.ajax({
            url:"/login",
            type:"POST",
            data: data,
            success: function(data){
                if(data == 'login_success'){
                    top.location.href="/";
                }else{
                    $("#username").val("");
                    $("#password").val("");
                    $(".error-message").html("Invalid user name or password");
                }
            }
        });
    });


    //모달 창 닫기
    $(".modal .exitIcon").click(function(e){
        $(this).parents(".modal").removeClass("active");
        $("#username").val("");
        $("#password").val("");
        $(".error-message").html("");
        overlay.removeClass("active");
    });

});