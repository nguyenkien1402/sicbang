
$(document).ready(function(){
    //초기 변수 설정
    var overlay = $(".overlay");
    var loginModal = $(".loginModal");
    var joinModal = $(".joinModal");
    var popup = $(".popup");

    //시, 도 Select
    var city = ["서울","경기","인천","강원도","충남","대전","충북","세종","부산","울산","대구","경북","경남","전남","광주","전북","제주"];
    city.forEach(function(item,index){
        var $option = $("<option></option>");
        $option.data("index",index);
        $option.text(item);
        $option.appendTo($(".citySelect"));
    });



    var district = [
        ["강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구","동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구"],
        ["가평군","고양시","과천시","광명시","광주시","구리시","군포시","김포시","남양주시","동두천시","부천시","성남시","수원시","시흥시","안산시","안성시","안양시","양주시","양평군","여주시","연천군","오산시","용인시","의왕시","의정부시","이천시","파주시","평택시","포천시","하남시","화성시"],
        ["강화군","계양구","남구","남동구","동구","부평구","서구","연수구","옹진군","중구"],
        ["강릉시","고성군","동해시","삼척시","속초시","양구군","양양군","영월군","원주시","인제군","정선군","철원군","춘천시","태백시","평창군","홍천군","화천군","횡성군"],
        ["계룡시","공주시","금산군","논산시","당진시","보령시","부여군","서산시","서천군","아산시","예산군","천안시","청양군","태안군","홍성군"],
        ["대덕구","동구","서구","유성구","중구"],
        ["괴산군","단양군","보은군","영동군","옥천군","음성군","제천시","증편군","진천군","청주시","충주시"],
        ["세종시"],
        ["강서구","금정구","남구","동구","동래구","부산진구","북구","사상구","사하구","서구","수영구","연제구","영도구","중구","해운대구"],
        ["남구","동구","북구","울주군","중구"],
        ["남구","달서구","달성군","동구","북구","서구","수성구","중구"],
        ["경산시","경주시","고령군","구미시","군위군","김천시","문경시","봉화군","상주시","성주군","안동시","영덕군","영양군","영주시","영천시","예천군","울릉군","울진군","의성군","청도군","청송군","칠곡군","포항시"],
        ["거제시","거창군","고성군","김해시","남해군","밀양시","사천시","산청군","양산시","의령군","진주시","창녕군","창원시","통영시","하동군","함안군","함양군","합천군"],
        ["강진군","고흥군","곡성군","광양시","구례군","나주시","담양군","목포시","무안군","보성군","순천시","신안군","여수시","영광군","영암군","완도군","장성군","장흥군","진도군","함평군","해남군","화순군"],
        ["광산구","남구","동구","북구","서구"],
        ["고창군","군산시","김제시","남원시","무주군","부안군","순창군","완주군","익산시","임실군","장수군","전주시","정읍시","진안군"],
        ["제주시","서귀포시"]
    ];

    $(".citySelect").change(function(e){
        var $parent = $(this).parent();
        var index = parseInt($(this).find("option:selected").data("index"));

        $parent.find(".districtSelect").text("");

        var $selectOption = $("<option disabled selected></option>");
        $selectOption.text("선택");
        $selectOption.appendTo($parent.find(".districtSelect"));

        district[index].forEach(function(item,index){
            var $option = $("<option></option>");
            $option.data("index",index);
            $option.text(item);
            $option.appendTo($parent.find(".districtSelect"));
        });
    });

    var subway = {
        url: "include/js/subway.js",
        list: {
            maxNumberOfElements: 10,
            match:{
                enabled: true
            }
        }
    };

    $(".subwayInput").easyAutocomplete(subway);

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

    //매물 찾기 방법 선택
    $("#findMethod").change(function(e){
        var method = $(this).find("option:selected").val();
        console.log(method);
        switch(method){
            case "1":         //상권으로 찾기
                $(".searchMethodArea").removeClass("active");
                $(".storeSearch").addClass("active");
                break;
            case "2":         //지하철역으로 찾기
                $(".searchMethodArea").removeClass("active");
                $(".subwaySearch").addClass("active");
                break;
            case "3":         //등록번호로 찾기
                $(".searchMethodArea").removeClass("active");
                $(".numberSearch").addClass("active");
                break;
        }
    });

    //모달 창 닫기
    $(".modal .exitIcon").click(function(e){
        $(this).parents(".modal").removeClass("active");
        $("#username").val("");
        $("#password").val("");
        $(".error-message").html("");
        overlay.removeClass("active");
    });

    $("#loginConfirmButton").click(function(e){
        $.ajax({
            url:"/login",
            type:"POST",
            data: {
                "username": $("#username").val(),
                "password": $("#password").val(),
            },
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

});