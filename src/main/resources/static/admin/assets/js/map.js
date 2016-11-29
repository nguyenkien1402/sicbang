/**
 * Created by DinhTruong on 10/3/2016.
 */

$(document).ready(function(){
    //초기 변수 설정
    var estateType = $("#estateType").val();
    if(estateType == 'STARTUP'){
        $("#liStartup").addClass("active");
    }else if(estateType == 'VACANT'){
        $("#liVacancy").addClass("active");
    }
    else{
        $("#liEstate").addClass("active");
    }
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(37.563723, 126.903237), // 지도의 중심좌표
            level: 10 // 지도의 확대 레벨
        };

    var remember = 'businessZone';
    var depositeCostFrom = 'all';
    var depositeCostTo = 'all';
    var rentCostFrom = 'all';
    var rentCostTo = 'all';
    var premiumCostFrom = 'all';
    var premiumCostTo = 'all';
    var businessChecked = '';

    // calling tab estate
    initTabState();

    // init map
    var map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
    var currentZoom = 0;
    var customOverlayCurrent = new daum.maps.CustomOverlay({});
    var zoomControl = new daum.maps.ZoomControl();
    map.addControl(zoomControl, daum.maps.ControlPosition.RIGHT);
    var markers = [];
    var clusterer = new daum.maps.MarkerClusterer({
        map: map,
        markers: markers,
        minClusterSize:10
    });

    daum.maps.event.addListener(map, 'dragend', function() {
        var latlng = map.getCenter();
        getDataDragEndOrZoomChange(latlng.getLat(),latlng.getLng());

    });

    daum.maps.event.addListener(map, 'zoom_changed', function() {
        var latlng = map.getCenter();
        currentZoom = map.getLevel();
        getDataDragEndOrZoomChange(latlng.getLat(),latlng.getLng());
    });
    //시, 도 Select
    var citys = [];
    var districts = [];
    var towns = [];
    $.ajax({
        url:getAllCity(),
        success:function(){
            initPage();
        }
    })


    // get All City when load page.
    function getAllCity(){
        $.ajax({
            url: "/estate/getAllCity",
            type: "GET",
            dataType: "json",
            success: function (data) {
                $.each(data, function (key, val) {

                    var cityObject = {name: "", id: 1};
                    cityObject.name=val.name;
                    cityObject.id=val.id;
                    citys.push(cityObject);
                    var $option = $("<option value='" + val.id + "'></option>");
                    $option.data("index", key);
                    $option.text(val.name);
                    $option.appendTo($(".citySelect"));
                    var district = [];
                    $.each(val.districts, function (key1, vale) {
                        var districtObject = {name: "John", id: 1};
                        districtObject.name = vale.name;
                        districtObject.id = vale.id;
                        district.push(districtObject);

                    });
                    districts.push(district);
                });
            }
        });
      
    }

    // city selectBox change event handler
    $(".citySelect").change(function(e){
        var $parent = $(this).parent();
        var index = parseInt($(this).find("option:selected").data("index"));

        $parent.find(".districtSelect").text("");

        var $selectOption = $("<option disabled selected></option>");
        $selectOption.text("선택");
        $selectOption.appendTo($parent.find(".districtSelect"));
		
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


    // district selectBox change event handler
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
                    towns.push(val);
                    var $option = $("<option value='"+val.id+"' ></option>");
                    $option.text(val.name);
                    $option.appendTo($parent.find(".townSelect"));
                });
            }
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
        $(div).empty();
        $.each(param,function(key,val){
            var src = '';
            var button = '';
            if(val.attachments[0] != null)
                src = val.attachments[0].origin;
            else
                src = '/static/admin/assets/images/no_image.jpg';
            if(val.name == "" || val.name == null)
                button = '<button>'+val.user.companyName+'</button>';

            var area = 0;
            if(val.estateType == "STARTUP")
            area = parseFloat(val.area);
            else
            area = parseFloat(val.contractArea);

            var areaKorean = Math.round(area/3.30875);
            var onelineComment = TextAbstract(val.onelineComment,15);
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
                +"<span>"+area+"m²("+areaKorean+"평)</span>"
                +"</div><span class='explain'>"+onelineComment+"</span>"
                +"</div></li></a>");

        });

    }

    function getDataDragEndOrZoomChange(lat,lng){
        $.ajax({
            url:"/estate/search/getDataDragEndOrZoomChange",
            type:"POST",
            dataType:"json",
            data:{
                depositeCostFrom : depositeCostFrom,
                depositeCostTo : depositeCostTo,
                rentFrom : rentCostFrom,
                rentTo : rentCostTo,
                premiumCostFrom : premiumCostFrom,
                premiumCostTo : premiumCostTo,
                estateType: estateType,
                businessType: businessChecked,
                zoomLevel: currentZoom,
                latitude: lat,
                longitude:lng
            },beforeSend: function(){
                $('img.loading').show();
            },success:function(data) {
                $('img.loading').hide();
                drawingMarker(data,0,0);
            }
        });

    }

    // 탭 열고 닫기
    function initTabState() {
        $(".searchbox-btn button").click(function () {
            $(".searchbox-btn").toggleClass("close");
            $(".searchBox").toggleClass("close");
        });
    }

    function TextAbstract(text, length) {
        if (text == null) {
            return "";
        }
        if (text.length <= length) {
            return text;
        }
        text = text.substring(0, length);
        return text + "...";
    }

    // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다

    var places = new daum.maps.services.Places();
    var callback = function(status, result) {
        if (status === daum.maps.services.Status.OK) {
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
            clusterer.addMarker(marker);
        }
    };

    // get All Parameter

    function getAllParameter(){
        getCheckedBusiness();
        getDepositeCost();
        getRentCost();
        getPremiumCost();
    }

    // get DepositeCost value ( depositeCostFrom - depositeCostTo)
    function getDepositeCost(){
        var strDepositeCost = $("#depositeCost").val();
        if(strDepositeCost != 'all'){
        var arrDepositeCost = strDepositeCost.split("-");
            depositeCostFrom = arrDepositeCost[0];
            depositeCostTo = arrDepositeCost[1];
        }else{
            depositeCostFrom = 'all';
            depositeCostTo = 'all';
        }
    }
    // get RentCost value ( rentCostFrom - rentCostTo)
    function getRentCost() {
        var strRentCost = $("#rentCost").val();
        if(strRentCost != 'all'){
            var arrRentCost = strRentCost.split("-");
            rentCostFrom = arrRentCost[0];
            rentCostTo = arrRentCost[1];
        }else{
            rentCostFrom = 'all';
            rentCostTo = 'all';
        }
    }

    function getCheckedBusiness(){
        businessChecked = '';
        for (var i = 0; i <= 30; i++) {
            if ($('#c' + i).prop('checked')) {
                businessChecked = businessChecked + $('#c' + i).val() + ',';
            }
        }
    }


    // get PremiumCost value ( premiumCostFrom - premiumCostTo)
    function getPremiumCost() {
        var strPremiumCost = $("#premiumCost").val();
        if(strPremiumCost != 'all'){
            if(strPremiumCost != 'none'){
                var arrPremiumCost = strPremiumCost.split("-");
                premiumCostFrom = arrPremiumCost[0];
                premiumCostTo = arrPremiumCost[1];
            }else{
                premiumCostFrom = 0;
                premiumCostTo = 0;
            }
        }else{
            premiumCostFrom = 'all';
            premiumCostTo = 'all';
        }
    }
    //Search by city,district,town click event
    $("#searchByStore").click(function(){
        var district = $(".districtSelect").val();
        var city = $(".citySelect").val();
        var town = $(".townSelect").val();
        currentZoom = 5;
        getAllParameter();
        searchByBusinessType(city,district,town,null,businessChecked,estateType,null,currentZoom);

        remember = 'businessZone';
    });

    //Search by subway click event
    $("#searchBySubway").click(function(){
        var subway = $('.subwayInput').val();
        currentZoom = 5;
        getAllParameter();

        searchByBusinessType(null,null,null,subway,businessChecked,estateType,null,currentZoom);
        remember = 'subway';
    });

    // Button Search by Business Click event
    $('.littleWide').click(function () {

        getAllParameter();

        if(remember == 'subway'){
            var subway = $('.subwayInput').val();
            searchByBusinessType(null,null,null,subway,businessChecked,estateType,null,currentZoom);
        }else{
            var district = $(".districtSelect").val();
            var city = $(".citySelect").val();
            var town = $(".townSelect").val();
            searchByBusinessType(city,district,town,null,businessChecked,estateType,null,currentZoom);
        }
    });

    function drawingMarker(data,latitude,longitude){

        var trusted = [];
        var broker = [];
        var member = [];
        customOverlayCurrent.setMap(null);
        clusterer.removeMarkers(markers);
        clusterer.clear();
        $.each(data,function(key,val){
            switch (val.typeTrust){
                case "TRUSTED_STARTUP": {
                    if( val.advertised == true)
                        trusted.push(val);
                    else
                        broker.push(val);
                    break;
                }
                case "REALTOR":  broker.push(val); break;
                case "DIRECT_DEAL" :  member.push(val); break;
            }
            var imageSrc = "/static/admin/include/images/map/";
            var category = parseInt(val.category);
            if(val.estateType == 'VACANT'){
                imageSrc = imageSrc+'shop.png';
            }else{
                switch(category){
                    case 1:  {imageSrc = imageSrc+'food.png'; break;}
                    case 2:  {imageSrc = imageSrc+'restaurant.png'; break;}
                    case 3:  {imageSrc = imageSrc+'liquor.png'; break;}
                }
            }
            imageSize = new daum.maps.Size(72, 72), // 마커이미지의 크기입니다
                imageOption = {offset: new daum.maps.Point(36.5, 36.5)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
            // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
            var markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, imageOption),
                markerPosition = new daum.maps.LatLng(val.latitude, val.longitude); // 마커가 표시될 위치입니다
            longitude = val.longitude;
            latitude = val.latitude;
            // 마커를 생성합니다
            var marker = new daum.maps.Marker({
                position: markerPosition,
                image: markerImage // 마커이미지 설정
            });

            // 마커가 지도 위에 표시되도록 설정합니다
            clusterer.addMarker(marker);
            // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
            var content = '<a target="_blank" href="/estate/detail/'+val.id+'">' +
                '<div class="detailOverlay">' +
                '    <span class="title">'+val.name+'</span>' +
                '</div>' +
                '</a>';

            // 커스텀 오버레이가 표시될 위치입니다
            var position = new daum.maps.LatLng(parseFloat(val.latitude) + parseFloat(0.000150),val.longitude);

            // 커스텀 오버레이를 생성합니다
            var customOverlay = new daum.maps.CustomOverlay({
                position: position,
                content: content,
                yAnchor: 1
            });
            daum.maps.event.addListener(marker, 'mouseover', function() {
                customOverlayCurrent.setMap(null);
                customOverlay.setMap(map);
                customOverlayCurrent = customOverlay;
            });
        });

        rendEstate(trusted,'#trusted_broker');
        rendEstate(broker,'#broker');
        rendEstate(member,'#member');
    }

    function searchByBusinessType(city,district,town,subway,businessType,estateType,registryNo,zoomLevel){

        $.ajax({
            url:"/estate/search/business",
            type:"POST",
            dataType:"json",
            data:{
                city: city,
                district: district,
                town:town,
                depositeCostFrom : depositeCostFrom,
                depositeCostTo : depositeCostTo,
                rentFrom : rentCostFrom,
                rentTo : rentCostTo,
                premiumCostFrom : premiumCostFrom,
                premiumCostTo : premiumCostTo,
                estateCode: registryNo,
                subwayStation:subway,
                estateType: estateType,
                businessType: businessType},
            beforeSend: function(){
                $('img.loading').show();
            },
            success:function(data){

                var latitude = 37.50819396972656;
                var longitude = 126.741943359375 ;

                drawingMarker(data,latitude,longitude);

                var cityName = "서울";
                var districtName = "";
                var townName = "";

                for(var i = 0 ; i< citys.length;i++){
                    if(city == citys[i].id){
                        cityName = citys[i].name;
                    }
                }
                for(var i=0; i<districts.length;i++){
                    for(var j=0;j<districts[i].length;j++){
                        if(district == districts[i][j].id){
                            districtName = districts[i][j].name;
                        }
                    }
                }
                for(var i = 0 ; i< towns.length;i++){
                    if(town == towns[i].id){
                        townName = towns[i].name;
                    }
                }

                var str = (city == null ? "" : cityName) + " " + (district == null ? "" : districtName) + (town == null ? "" : townName);

                map.setLevel(zoomLevel);
                if(subway != null) {
                    str = subway + "역";
                    places.keywordSearch(str, callback);
                }else if(registryNo!= null){
                    var coords = new daum.maps.LatLng(latitude, longitude);
                    map.setCenter(coords);
                }
                else{
                    // 주소-좌표 변환 객체를 생성합니다
                    var geocoder = new daum.maps.services.Geocoder();
                    // 주소로 좌표를 검색합니다
                    geocoder.addr2coord(str, function(status, result) {
                        // 정상적으로 검색이 완료됐으면
                        if (status === daum.maps.services.Status.OK) {
                            var coords = new daum.maps.LatLng(result.addr[0].lat, result.addr[0].lng);
                            // 결과값으로 받은 위치를 마커로 표시합니다
                            var marker = new daum.maps.Marker({
                                position: coords
                            });
                            clusterer.addMarker(marker);
                            map.setCenter(coords);
                        }else{
                            if(city != null || district != null)
                            alert("검색에 실패하였습니다. 주소를 다시 확인하여주세요.\n검색 주소 : "+str);
                        }
                    });
                }
            },complete: function(){
                $('img.loading').hide();
            }
        });
    }




    function initPage() {
        var redirect = $("#redirect").val();
        //init map
        if (redirect != 'true') {
            searchByBusinessType(9, null,null, null, null, estateType,null,7);
        } else {
            var cityValue = $("#cityValue").val();
            var districtValue = $("#districtValue").val();
            var townValue = $("#townValue").val();
            var subwayValue = $("#subwayStationValue").val();
            var registryNo = $("#registryNoValue").val();

            getAllParameter();

            if (subwayValue == '' && registryNo == '') {
                searchByBusinessType(cityValue, districtValue,townValue, null, businessChecked, estateType,null,5);
            }else if(registryNo != ''){
                searchByBusinessType(null, null,null, null, businessChecked, estateType,registryNo,5);
            }else {
                $(".searchBox .searchMethodArea").removeClass("active");
                $(".searchBox .buttonArea button").removeClass("active");
                $(".searchBox .storeSearch").removeClass("active");
                $(".searchBox .subwaySearch").addClass("active");
                $("#subwaySearchButton").addClass("active");
                $(".subwayInput").val(subwayValue);
                searchByBusinessType(null, null,null, subwayValue, businessChecked, estateType,null,5);
            }

        }
    }
    
});