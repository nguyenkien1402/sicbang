$(document).ready(function(){
    //초기 변수 설정


    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(37.563723, 126.903237), // 지도의 중심좌표
            level: 10 // 지도의 확대 레벨
        };

    var map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
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
                console.log(district);
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
            var $option = $("<option value='"+item.id+"' ></option>");
            $option.data("index",index);
            $option.text(item.name);
            $option.appendTo($parent.find(".districtSelect"));
        });
    });

    var subway = {
        url: "/static/admin/include/js/subway.js",
        list: {
            maxNumberOfElements: 10,
            match:{
                enabled: true
            }
        }
    };
    //init map
    search("서울",null,null,"STARTUP");
    //
    $(".subwayInput").easyAutocomplete(subway);

    //검색 버튼
    $(".searchBox .buttonArea button").click(function(){
        $(".searchBox .searchMethodArea").removeClass("active");
        $(".searchBox ."+$(this).data("method")).addClass("active");

        $(".searchBox .buttonArea button").removeClass("active");
        $(this).addClass("active");


    });
    // function rend top 10 estate when load page.
    function rendEstate(param,div){
        $.each(param,function(key,val){
            var src = '';
            var button = '';
            if(val.attachments[0] != null)
                src = val.attachments[0].origin;
            else
                src = '/static/admin/assets/images/no_image.jpg';
            if(val.name == "" || val.name == null)
                button = '<button>'+val.user.companyName+'</button>';

            $(div).append("<a target='_blank' href='/estate/detail/"+val.id+"'><li>"+
                "<div class='imgArea'>"
                +"<img src='"+src+"' class='basicImg'/>"
                +"</div><div class='textArea'>"
                + button
                +"<h5 class='primary'>"+val.name+"</h5>"
                +"<div class='price'>"
                +"<span>보증금 "+val.depositeCost+"</span>"
                +"<span>월세 "+val.rentCost+"</span>"
                +"<span>권리금 "+val.premiumCost+"</span>"
                +"</div><div class='location'>"
                +"<span>"+val.businessZone+"</span>"
                +"<span>"+val.area+"m²(8평)</span>"
                +"</div><span class='explain'>"+val.onelineComment+"</span>"
                +"</div></li></a>");

        });

    }
    /* 지하철 역 마커 */
    {
        var imageSrc = '/static/admin/include/images/map/subway.png', // 마커이미지의 주소입니다
            imageSize = new daum.maps.Size(57, 68), // 마커이미지의 크기입니다
            imageOption = {offset: new daum.maps.Point(28.5, 68)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

        // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
        var markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, imageOption),
            markerPosition = new daum.maps.LatLng(37.563723, 126.903237); // 마커가 표시될 위치입니다

        // 마커를 생성합니다
        var marker = new daum.maps.Marker({
            position: markerPosition,
            image: markerImage // 마커이미지 설정
        });

        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map);

        // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
        var content = '<div class="customOverlay">' +
            '    <span class="title">마포구청역</span>' +
            '</div>';

        // 커스텀 오버레이가 표시될 위치입니다
        var position = new daum.maps.LatLng(37.563723 - 0.000350, 126.903237);

        // 커스텀 오버레이를 생성합니다
        var customOverlay = new daum.maps.CustomOverlay({
            map: map,
            position: position,
            content: content,
            yAnchor: 1
        });
    }

    // call api to get estate
    $.ajax({
        url: "/estate/map",
        type:"GET",
        dataType:"json",
        success:function (data) {
            $('#trusted_broker').empty();
            $('#broker').empty();
            $('#member').empty();
            var trusted = data.trusted;
            var broker = data.broker;
            var member = data.member;
            rendEstate(trusted,'#trusted_broker');
            rendEstate(broker,'#broker');
            rendEstate(member,'#member');

        }
    });

    // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다

    daum.maps.event.addListener(map, 'click', function(mouseEvent) {

        // 클릭한 위도, 경도 정보를 가져옵니다
        var latlng = mouseEvent.latLng;

        var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
        message += '경도는 ' + latlng.getLng() + ' 입니다';

        console.log(message);

    });

    var places = new daum.maps.services.Places();
    var callback = function(status, result) {
        if (status === daum.maps.services.Status.OK) {
            console.log(result);
            var coords = new daum.maps.LatLng(result.places[0].latitude, result.places[0].longitude);
            // 결과값으로 받은 위치를 마커로 표시합니다
            map.setCenter(coords);
            var imageSrc = '/static/admin/include/images/map/subway.png', // 마커이미지의 주소입니다
                imageSize = new daum.maps.Size(57, 68), // 마커이미지의 크기입니다
                imageOption = {offset: new daum.maps.Point(28.5, 68)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
            // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
            var markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, imageOption),
                markerPosition = coords;
            // 마커를 생성합니다
            marker = new daum.maps.Marker({
                position: markerPosition,
                image: markerImage // 마커이미지 설정
            });
            map.setLevel(5);
            var circle = new daum.maps.Circle({
                map: map,
                center: coords,
                radius: 1000,
                strokeWeight: 2,
                strokeColor: '#FF00FF',
                strokeOpacity: 0.8,
                strokeStyle: 'dashed',
                fillColor: 'blue',
                fillOpacity: 0.1
            });
            // 마커가 지도 위에 표시되도록 설정합니다
            marker.setMap(map);
        }
    };

    // function search map.
    function search(city,district,subway,estateType){
        $.ajax({
            url:"/estate/search",
            type:"POST",
            dataType:"json",
            data:{
                city: city,
                district: district,
                subwayStation:subway,
                estateType: estateType,
            },success : function(data){
                map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
                var zoomControl = new daum.maps.ZoomControl();
                map.addControl(zoomControl, daum.maps.ControlPosition.RIGHT);

                $.each(data,function(key,val){
                    var imageSrc = "/static/admin/include/images/map/";
                    var category = parseInt(val.category);
                    switch(category){
                        case 1:  {imageSrc = imageSrc+'food.png'; break;}
                        case 2:  {imageSrc = imageSrc+'restaurant.png'; break;}
                        case 3:  {imageSrc = imageSrc+'liquor.png'; break;}
                        default: {imageSrc = imageSrc+'shop.png'; break;}
                    }
                    imageSize = new daum.maps.Size(72, 72), // 마커이미지의 크기입니다
                        imageOption = {offset: new daum.maps.Point(36.5, 36.5)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                    // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
                    var markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, imageOption),
                        markerPosition = new daum.maps.LatLng(val.latitude, val.longitude); // 마커가 표시될 위치입니다

                    // 마커를 생성합니다
                    var marker = new daum.maps.Marker({
                        position: markerPosition,
                        image: markerImage // 마커이미지 설정
                    });

                    // 마커가 지도 위에 표시되도록 설정합니다
                    marker.setMap(map);
                    // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
                    var content = '<a target="_blank" href="estate/detail/'+val.id+'">' +
                        '<div class="detailOverlay">' +
                        '    <span class="title">'+val.name+'</span>' +
                        '</div>' +
                        '</a>';

                    // 커스텀 오버레이가 표시될 위치입니다
                    var position = new daum.maps.LatLng(parseFloat(val.latitude) + parseFloat(0.000150),val.longitude);

                    // 커스텀 오버레이를 생성합니다
                    var customOverlay = new daum.maps.CustomOverlay({
                        map: map,
                        position: position,
                        content: content,
                        yAnchor: 1
                    });
                });
                var str = (city == null ? "" : city) + " " + (district == null ? "" : district);
                map.setLevel(7);
                if(subway != null) {
                    str = subway + "역";
                    places.keywordSearch(str, callback);
                }else{
                // 주소-좌표 변환 객체를 생성합니다
                var geocoder = new daum.maps.services.Geocoder();
                // 주소로 좌표를 검색합니다
                geocoder.addr2coord(str, function(status, result) {
                    // 정상적으로 검색이 완료됐으면
                    console.log(result);
                    if (status === daum.maps.services.Status.OK) {
                        var coords = new daum.maps.LatLng(result.addr[0].lat, result.addr[0].lng);
                        // 결과값으로 받은 위치를 마커로 표시합니다
                        var marker = new daum.maps.Marker({
                            map: map,
                            position: coords
                        });
                        map.setCenter(coords);
                    }else{
                        alert("검색에 실패하였습니다. 주소를 다시 확인하여주세요.\n검색 주소 : "+str);
                    }
                });
                }
            }
        });

    }
    var remember = 'businessZone';
    $("#searchByStore").click(function(){
        var district = $(".districtSelect").val();
        var city = $(".citySelect").val();
        var attr = '';
        for(var i = 0; i<=30;i++){
            if($('#c'+i).prop('checked')){
                attr = attr+$('#c'+i).val() + ',';
            }
        }
        searchByBusinessType(city,district,null,attr,"");

        remember = 'businessZone';
    });
    $("#searchBySubway").click(function(){
        var subway = $('.subwayInput').val();
        var attr = '';

        for(var i = 0; i<=30;i++){
            if($('#c'+i).prop('checked')){
                attr = attr+$('#c'+i).val() + ',';
            }
        }
        searchByBusinessType(null,null,subway,attr,"");
        remember = 'subway';
    });

    $('.littleWide').click(function () {
        var attr = '';

        for(var i = 0; i<=30;i++){
            if($('#c'+i).prop('checked')){
                attr = attr+$('#c'+i).val() + ',';
            }
        }
        if(remember == 'subway'){
            console.log(attr);
            var subway = $('.subwayInput').val();
            searchByBusinessType(null,null,subway,attr,"");
        }else{
            var district = $(".districtSelect").val();
            var city = $(".citySelect").val();
            searchByBusinessType(city,district,null,attr,"");
        }

    });

    function searchByBusinessType(city,district,subway,businessType,estateType){
        $.ajax({
            url:"/estate/search/business",
            type:"POST",
            dataType:"json",
            data:{
                city: city,
                district: district,
                subwayStation:subway,
                estateType: estateType,
                businessType: businessType},
            success:function(data){
                map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
                var zoomControl = new daum.maps.ZoomControl();
                map.addControl(zoomControl, daum.maps.ControlPosition.RIGHT);

                $.each(data,function(key,val){
                    var imageSrc = "/static/admin/include/images/map/";
                    var category = parseInt(val.category);
                    switch(category){
                        case 1:  {imageSrc = imageSrc+'food.png'; break;}
                        case 2:  {imageSrc = imageSrc+'restaurant.png'; break;}
                        case 3:  {imageSrc = imageSrc+'liquor.png'; break;}
                        default: {imageSrc = imageSrc+'shop.png'; break;}
                    }
                    imageSize = new daum.maps.Size(72, 72), // 마커이미지의 크기입니다
                        imageOption = {offset: new daum.maps.Point(36.5, 36.5)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                    // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
                    var markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, imageOption),
                        markerPosition = new daum.maps.LatLng(val.latitude, val.longitude); // 마커가 표시될 위치입니다

                    // 마커를 생성합니다
                    var marker = new daum.maps.Marker({
                        position: markerPosition,
                        image: markerImage // 마커이미지 설정
                    });

                    // 마커가 지도 위에 표시되도록 설정합니다
                    marker.setMap(map);
                    // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
                    var content = '<a target="_blank" href="estate/detail/'+val.id+'">' +
                        '<div class="detailOverlay">' +
                        '    <span class="title">'+val.name+'</span>' +
                        '</div>' +
                        '</a>';

                    // 커스텀 오버레이가 표시될 위치입니다
                    var position = new daum.maps.LatLng(parseFloat(val.latitude) + parseFloat(0.000150),val.longitude);

                    // 커스텀 오버레이를 생성합니다
                    var customOverlay = new daum.maps.CustomOverlay({
                        map: map,
                        position: position,
                        content: content,
                        yAnchor: 1
                    });
                });
                var str = (city == null ? "" : $(".citySelect :selected").text()) + " " + (district == null ? "" : $(".districtSelect :selected").text());
                map.setLevel(7);
                if(subway != null) {
                    str = subway + "역";
                    places.keywordSearch(str, callback);
                }else{
                    // 주소-좌표 변환 객체를 생성합니다
                    var geocoder = new daum.maps.services.Geocoder();
                    // 주소로 좌표를 검색합니다
                    geocoder.addr2coord(str, function(status, result) {
                        // 정상적으로 검색이 완료됐으면
                        console.log(result);
                        if (status === daum.maps.services.Status.OK) {
                            var coords = new daum.maps.LatLng(result.addr[0].lat, result.addr[0].lng);
                            // 결과값으로 받은 위치를 마커로 표시합니다
                            var marker = new daum.maps.Marker({
                                map: map,
                                position: coords
                            });
                            map.setCenter(coords);
                        }else{
                            alert("검색에 실패하였습니다. 주소를 다시 확인하여주세요.\n검색 주소 : "+str);
                        }
                    });
                }
            }
        });
    }
});