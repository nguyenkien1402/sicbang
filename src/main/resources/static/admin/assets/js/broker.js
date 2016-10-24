$(document).ready(function() {
    //초기 변수 설정
    var overlay = $(".overlay");
    var estateType = $('#estateType').val();
    if(estateType == 'STARTUP'){
        $("#liStartup").addClass("active");
    }else{
        $("#liVacancy").addClass("active");
    }
    var reportModal = $(".reportModal");
    var longitude = $("#longitude").val();
    var latitude = $("#latitude").val();
    var estateId = $("#estateId").val();
    var userId = $("#userId").val();
    var memberId = $("#memberId").val();
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(latitude,longitude), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

    var map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 마커가 표시될 위치입니다
    var markerPosition = new daum.maps.LatLng(latitude,longitude);

    // 마커를 생성합니다
    var marker = new daum.maps.Marker({
        position: markerPosition
    });

    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);

    //로드뷰
    var roadviewContainer = document.getElementById('roadView'); //로드뷰를 표시할 div
    var roadview = new daum.maps.Roadview(roadviewContainer); //로드뷰 객체
    var roadviewClient = new daum.maps.RoadviewClient(); //좌표로부터 로드뷰 파노ID를 가져올 로드뷰 helper객체

    var position = new daum.maps.LatLng(latitude,longitude);

    // 특정 위치의 좌표와 가까운 로드뷰의 panoId를 추출하여 로드뷰를 띄운다.
    roadviewClient.getNearestPanoId(position, 50, function (panoId) {
        roadview.setPanoId(panoId, position); //panoId와 중심좌표를 통해 로드뷰 실행
    });

    //썸네일을 클릭하면 viewImage에 보여준다.
    $(".thumbImage img").click(function (e) {
        var imgs = $(".thumbImage img");
        imgs.removeClass("active");

        $(this).addClass("active");

        $(".viewImage img").attr("src", $(this).attr("src"));

        $(".thumbNum").text($(this).index() + 1 + "/" + imgs.length);
    });

    //화살표를 클릭하면 보여준다.
    $(".sideBox").click(function (e) {
        var arrow = $(this).find(".arrow");
        var imgs = $(".thumbImage img");
        var currentImg = $(".thumbImage img.active");

        imgs.removeClass("active");

        if (arrow.hasClass("leftArrow")) {
            var prevImg;

            if (currentImg.index() != 0)
                prevImg = currentImg.prev();
            else
                prevImg = imgs.eq(imgs.length - 1);

            prevImg.addClass("active");

            $(".viewImage img").attr("src", prevImg.attr("src"));
            $(".thumbNum").text(prevImg.index() + 1 + "/" + imgs.length);
        } else {
            var nextImg;

            if (currentImg.index() != imgs.length - 1)
                nextImg = currentImg.next();
            else
                nextImg = imgs.eq(0);

            nextImg.addClass("active");

            $(".viewImage img").attr("src", nextImg.attr("src"));
            $(".thumbNum").text(nextImg.index() + 1 + "/" + imgs.length);
        }

    });

    //잘못된 정보 신고 버튼
    $(".reportButton").click(function (e) {
        overlay.addClass("active");
        reportModal.addClass("active");
        $(window).scrollTop(0);
    });

    $("#reportConfirmButton").click(function (e) {
        var name = $("input[name=reportName]").val();
        var phone = $("input[name=reportPhone]").val();
        var contents = $("input[name=reportContents]").val();

        if (name == "" || phone == "" || contents == "") {
            alert("모든 정보를 입력해주세요.");
            return false;
        }
        $.ajax({
            url : '/member/report/create',
            type:"POST",
            dataType:"json",
            data:{
                "userId" : userId,
                "estateId" : estateId,
                "name" : name,
                "cellphone" : phone,
                "content" : contents
            },success:function (data) {
                if(data.errors != null){
                    alert("false");
                    return;
                }
                alert("신고가 접수되었습니다.");
                overlay.removeClass("active");
                reportModal.removeClass("active");
            }
        });

    });

    $("#deleteMarket").click(function(e){
        if(confirm("해당 매물을 삭제하시겠습니까?")){
            $.ajax({
                url: "/member/estate/"+estateId,
                type:"DELETE",
                data:{

                },
                success:function(data){
                    alert(data);
                    top.location.href="/member/estate";
                }

            });
        }
    });

    var isWishList = $("#isWishList").val();
    if(isWishList == 'true'){
        $("#wishList").html("위시리스트에서제거");
    }else{
        $("#wishList").html("위시리스트에추가");
    }

    $("#wishList").click(function(){
        if(isWishList == 'true'){
            $.ajax({
                url: "/member/estate/removeWishList",
                type:"POST",
                data:{
                    "estateId" : estateId,
                    "userId": memberId,
                },
                success:function(data){
                    if(data == "SUCCESS")
                    {
                        $("#wishList").html("위시리스트에추가");
                        isWishList = 'false';
                    }else{
                        alert("FAILED");
                    }
                }
            });
        }else{
            $.ajax({
                url: "/member/estate/addWishList",
                type:"POST",
                data:{
                    "estateId" : estateId,
                    "userId": memberId,
                },
                success:function(data){
                    if(data == "SUCCESS")
                    {
                        $("#wishList").html("위시리스트에서제거");
                        isWishList = 'true';
                    }else{
                        alert("FAILED");
                    }
                }
            });
        }
    });

    $("#alertMessage").click(function(){
        alert("Members only; please login.(로그인해주세요)");
    });

    $("#changeAdvertised").click(function(){
         var status = $("#changeAdvertised").text();
         var isAdvertised = false;
         if(status == "광고등록"){
            isAdvertised = true;
         }
         var estateId = $("#estateId").val();
         $.ajax({
             url:"/member/estate/changeAdv",
             type:"POST",
             data:{
                 isAdvertised: isAdvertised,
                 estateId: estateId,
             },success:function(data){
                     if(data == "SUCCESS"){
                         location.reload();
                     }else{
                         alert("false");
                     }
             }
         });
         return false;
    });
});

