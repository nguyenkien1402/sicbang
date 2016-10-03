$(document).ready(function(){
    //초기 변수 설정
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
        url: "/static/admin/include/js/subway.js",
        list: {
            maxNumberOfElements: 10,
            match:{
                enabled: true
            }
        }
    };

    $(".subwayInput").easyAutocomplete(subway);

    //검색 버튼
    $(".searchBox .buttonArea button").click(function(){
        $(".searchBox .searchMethodArea").removeClass("active");
        $(".searchBox ."+$(this).data("method")).addClass("active");

        $(".searchBox .buttonArea button").removeClass("active");
        $(this).addClass("active");


    });

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(37.563723, 126.903237), // 지도의 중심좌표
            level: 8 // 지도의 확대 레벨
        };

    var map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

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



    $.ajax({
        url:"/estate/search/startup",
        type:"POST",
        dataType:"json",
        data:{
            city: "Seoul",
            estateType: "STARTUP",
        },success : function(data){
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
                var content = '<a href="estate/detail/'+val.id+'">' +
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
        }
    });

    // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
    var zoomControl = new daum.maps.ZoomControl();
    map.addControl(zoomControl, daum.maps.ControlPosition.RIGHT);

    daum.maps.event.addListener(map, 'click', function(mouseEvent) {

        // 클릭한 위도, 경도 정보를 가져옵니다
        var latlng = mouseEvent.latLng;

        var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
        message += '경도는 ' + latlng.getLng() + ' 입니다';

        console.log(message);

    });


});