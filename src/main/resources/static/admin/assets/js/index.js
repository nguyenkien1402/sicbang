/**
 * Created by DinhTruong on 10/24/2016.
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
    $(".subwayInput").easyAutocomplete(subway);

    //시, 도 Select
    var city = [];
    var districts = [];

    // call ajax to get all city and district.
    $.ajax({
        url : "/estate/getAllCity",
        type: "GET",
        dataType:"json",
        success:function(data){
            $.each(data,function (key,val) {
                city.push(val.name);
                var $option = $("<option value='"+val.id+"'></option>");
                $option.data("index",key);
                $option.text(val.name);
                $option.appendTo($(".citySelect"));
                var district = [];
                $.each(val.districts,function(key1,vale){
                    var districtObject = {name:"John", id:1};;
                    districtObject.name = vale.name;
                    districtObject.id = vale.id;
                    district.push(districtObject);
                });
                districts.push(district);
            });
        }
    });



    $(".citySelect").change(function(e){
        var $parent = $(this).parent();
        var index = parseInt($(this).find("option:selected").data("index"));

        $parent.find(".districtSelect").text("");

        var $selectOption = $("<option disabled selected></option>");
        $selectOption.text("선택");
        $selectOption.appendTo($parent.find(".districtSelect"));
		$parent.find(".townSelect").text("");

        var $townOption = $("<option disabled selected></option>");
        $townOption.text("선택");
        $townOption.appendTo($parent.find(".townSelect"));
		
        districts[index].forEach(function(item,index){
            var $option = $("<option value='"+item.id+"' ></option>");
            $option.data("index",index);
            $option.text(item.name);
            $option.appendTo($parent.find(".districtSelect"));
        });
    });

    $(".districtSelect").change(function(e){
        var $parent = $(this).parent();
        var index = parseInt($(this).find("option:selected").data("index"));

        $parent.find(".townSelect").text("");

        var $selectOption = $("<option disabled selected></option>");
        $selectOption.text("선택");
        $selectOption.appendTo($parent.find(".townSelect"));
        var districtId = $(this).val();
        $.ajax({
            url: "/estate/getAllTown/"+districtId,
            dataType:"json",
            type:"GET",
            success:function(data){
                $.each(data,function(key,val){
                    var $option = $("<option value='"+val.id+"' ></option>");
                    $option.text(val.name);
                    $option.appendTo($parent.find(".townSelect"));
                });
            }
        });
    });
    //시, 도 Select

    //처음에 팝업창 띄우기

    $(".popup .exit").click(function(e){
        $(this).parents(".popup").removeClass("active");
        overlay.removeClass("active");
    });


    //로그인 버튼


    var findType = 'businessZone';

    //매물 찾기 방법 선택
    $("#findMethod").change(function(e){
        var method = $(this).find("option:selected").val();
        console.log(method);
        switch(method){
            case "1":         //상권으로 찾기
                $(".searchMethodArea").removeClass("active");
                $(".storeSearch").addClass("active");
                $(".subwayInput").val("");
                $("#registryNo").val("");
                break;
            case "2":         //지하철역으로 찾기
                $(".searchMethodArea").removeClass("active");
                $(".subwaySearch").addClass("active");
                $("#registryNo").val("");
                $(".citySelect").val("선택");
                $(".districtSelect").val("선택");
                break;
            case "3":         //등록번호로 찾기
                $(".searchMethodArea").removeClass("active");
                $(".numberSearch").addClass("active");
                $(".citySelect").val("선택");
                $(".districtSelect").val("선택");
                $(".subwayInput").val("");
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



});