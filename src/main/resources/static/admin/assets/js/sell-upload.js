/**
 * Created by DinhTruong on 10/24/2016.
 */

$(document).ready(function(){

    var constructDate=$('input[name="constructDate"]'); //our date input has the name "date"
    constructDate.datepicker({
        format: "yyyy/mm/dd",
        container: constructDate.parent(),
        todayHighlight: true,
        autoclose: true,
        language: 'kr'
    });
    var availableDate=$('input[name="availableDate"]');//our date input has the name "date"
    availableDate.datepicker({
        format: "yyyy/mm/dd",
        container: availableDate.parent(),
        todayHighlight: true,
        autoclose: true,
        language: 'kr'
    });
    //초기 변수 설정
    var overlay = $(".overlay");
    var loginModal = $(".loginModal");
    var joinModal = $(".joinModal");
    var addressModal = $(".addressModal");
    var longitude = 0;
    var latitude = 0;
    var isAddressChecked = false;
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(37.563723, 126.903237), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

    var map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    addressModal.removeClass("active");

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
                var $option = $("<option value='"+val.name+"' data-info='"+val+"'></option>");
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

        districts[index].forEach(function(item,index){
            var $option = $("<option value='"+item.name+"' data-id='"+item.id+"'></option>");
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
        var districtId = $(this).find("option:selected").data("id");
        $.ajax({
            url: "/estate/getAllTown/"+districtId,
            dataType:"json",
            type:"GET",
            success:function(data){
                $.each(data,function(key,val){
                    var $option = $("<option value='"+val.name+"' data-info='"+val+"' ></option>");
                    $option.text(val.name);
                    $option.appendTo($parent.find(".townSelect"));
                });
            }
        });
    });



    ///
    getAllCategory();
    var categories = [];
    var businessTypes = [];

    //get all category
    function getAllCategory(){
        $.ajax({
            url:"/member/estate/getAllCategory",
            type:"GET",
            dataType:"json",
            success:function(data){
                $.each(data,function(key,val){
                    var categoryObject = {name: "", id: 1};
                    categoryObject.name=val.name;
                    categoryObject.id=val.id;
                    categories.push(categoryObject);
                    var $option = $("<option value='" + val.id + "'></option>");
                    $option.data("index", key);
                    $option.text(val.name);
                    $option.appendTo($(".category"));
                    var businessTypeArr = [];
                    $.each(val.businessTypes, function (key1, value) {
                        var businessTypeObject = {name: "John", id: 1};
                        businessTypeObject.name = value.name;
                        businessTypeObject.id = value.id;
                        businessTypeArr.push(businessTypeObject);
                    });
                    businessTypes.push(businessTypeArr);
                });

                var index = parseInt($(".category").find("option:selected").data("index"));
                $(".businessType").empty();
                businessTypes[index].forEach(function(item,index){
                    var $option = $("<option value='"+item.id+"' ></option>");
                    $option.data("index",index);
                    $option.text(item.name);
                    $option.appendTo($(".businessType"));
                });

            }

        });
    };

    $(".category").change(function(){
        var $parent = $(this).parent();
        var index = parseInt($(this).find("option:selected").data("index"));

        $(".businessType").empty();

        businessTypes[index].forEach(function(item,index){
            var $option = $("<option value='"+item.id+"' ></option>");
            $option.data("index",index);
            $option.text(item.name);
            $option.appendTo($(".businessType"));
        });
    });


    //시, 도 Select
    $(".searchAddress").click(function(e){
        e.preventDefault();
        var $parent = $(this).parent();
        var city = $parent.find(".citySelect").find("option:selected").text();
        var district = $parent.find(".districtSelect").find("option:selected").text();
        var town = $parent.find(".townSelect").find("option:selected").text();
        var detail = $parent.find(".detailAddress").val();
        var str = (city == "선택" ? "" : city) + " " + (district == "선택" ? "" : district) + " " + (town == "선택" ? "" : town) + " " + detail;
        // 주소-좌표 변환 객체를 생성합니다
        var geocoder = new daum.maps.services.Geocoder();
        // 주소로 좌표를 검색합니다
        geocoder.addr2coord(str, function(status, result) {

            // 정상적으로 검색이 완료됐으면
            if (status === daum.maps.services.Status.OK) {
                var coords = new daum.maps.LatLng(result.addr[0].lat, result.addr[0].lng);
                longitude = result.addr[0].lng;
                latitude = result.addr[0].lat;
                // 결과값으로 받은 위치를 마커로 표시합니다
                var marker = new daum.maps.Marker({
                    map: map,
                    position: coords
                });
                // 인포윈도우로 장소에 대한 설명을 표시합니다
                //var infowindow = new daum.maps.InfoWindow({
                //    content: '<div style="width:150px;text-align:center;padding:6px 0;">우리회사</div>'
                //});
                //infowindow.open(map, marker);
                // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
                map.setCenter(coords);
                overlay.addClass("active");
                addressModal.addClass("active");
            }else{
                alert("검색에 실패하였습니다. 주소를 다시 확인하여주세요.\n검색 주소 : "+str);
            }
        });
    });

    var tabType = 'first';

    //탭의 버튼을 눌렀을 때
    $(".tabArea button").click(function(e){
        $(".tabContents").removeClass("active");
        $(".tabArea button").removeClass("active");
        var str = $(this).data("index");
        tabType = $(this).data("index");
        $(".tabContents."+str).addClass("active");
        $(this).addClass("active");
        isAddressChecked = false;
    });

    //사진 첨부 버튼
    $(".imageUploadButton").click(function(e){
        var files = $(this).parent().find("input[type=file]");
        var imgs = $(this).parent().find("img");

        var fileClick = false;
        files.each(function(index){
            //만약 파일이 비어있다면 그 곳에 이미지를 집어넣는다.
            if($(this).val() == "" && !fileClick){
                $(this).click();
                fileClick = true;
            }
        });

        if(!fileClick){
            alert("사진은 최대 5장까지 업로드할 수 있습니다.")
        }
    });
    var fileString = ["","","","",""];
    //파일이 업로드 되면
    $("input[type=file]").change(function(e){
        var img = $(this).parent().find("img").eq(
            $(this).parents(".tabContents").find("input[type=file]").index($(this))
        );

        var ext = $(this).val().split('.').pop().toLowerCase();
        if(ext == ""){
            img.attr('src', "");
            $(this).val("");
            return false;
        }

        if($.inArray(ext, ['gif','png','jpg','jpeg']) == -1) {
            alert('gif,png,jpg,jpeg 파일만 업로드 할수 있습니다.');
            $(this).val("");
            img.attr('src', "");
            return;
        }

        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                img.attr('src', e.target.result);
                for(var i = 0 ; i < 5 ; i++){
                    if(fileString[i] == ''){
                        fileString[i] = e.target.result;
                        break;
                    }
                }
            }
            reader.readAsDataURL(this.files[0]);

        }
    });

    //이미지 제거를 위해 사진을 클릭했을 때
    $(".uploadImg").click(function(e){
        var file = $(this).parent().find("input[type=file]").eq(
            $(this).parents(".tabContents").find("img").index($(this))
        );
        $(this).attr("src","");
        file.val("");

    });
    var _this = new Admin();
    //confirm address button event handler
    $("#confirm").click(function(){
        isAddressChecked = true;

    });
    $("#cancel").click(function(){
        isAddressChecked = false;
    });
    //모달 창 닫기
    $(".modal .exitIcon").click(function(e){
        $(this).parents(".modal").removeClass("active");
        overlay.removeClass("active");
    });


    $("#formStartup").submit(function(e) {
        var self = this;
        e.preventDefault();

        //      if (!_this.validateForm($(self))) {
        //          return false;
        //     }
        if(!isAddressChecked){
            alert("당신은 검색 주소 버튼을 클릭하여 주소를 확인해야");
            return false;
        }
        var inputLongitude = $("<input>").attr("type", "hidden").attr("name", "longitude").val(longitude);
        var inputLatitude = $("<input>").attr("type", "hidden").attr("name", "latitude").val(latitude);

        //     _this.convertForm($(self));

        $('#formStartup').append($(inputLongitude));
        $('#formStartup').append($(inputLatitude));
        //     self.submit();
        var text = $("#detailStartup").val();
        text = text.replace(/\r?\n/g, '<br />');
        console.log(text);
        $("#detailStartup").val(text);
        _this.submitFormEstateWeb($(self));
        return false; //is superfluous, but I put it here as a fallback
    });

    $("#formVacant").submit(function(e) {
        var self = this;
        e.preventDefault();
        //    if (!_this.validateForm($(self))) {
        //        return false;
        //    }
        if(!isAddressChecked){
            alert("당신은 검색 주소 버튼을 클릭하여 주소를 확인해야");
            return false;
        }
        var inputLongitude = $("<input>").attr("type", "hidden").attr("name", "longitude").val(longitude);
        var inputLatitude = $("<input>").attr("type", "hidden").attr("name", "latitude").val(latitude);
        //   _this.convertForm($(self));
        $('#formVacant').append($(inputLongitude));
        $('#formVacant').append($(inputLatitude));
        var text = $("#detailVacant").val();
        text = text.replace(/\r?\n/g, '<br/>');
        $("#detailVacant").val(text);
        console.log(text);
        //   self.submit();
        _this.submitFormEstateWeb($(self));
        return false; //is superfluous, but I put it here as a fallback
    });
});