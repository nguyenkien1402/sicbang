function DaumMap() {

    var _this = this;
    var _map = {};
    var _markers = [];
    var circle = {};
    var API_KEY = 'ea91b3914bab953385dbc9a97cf6b1c5';

    this.init = function(mapContainer, mapCenterLat, mapCenterLng, mapLevel) {
        var mapContainer = document.getElementById(mapContainer),
            mapOption = {
                center: new daum.maps.LatLng(parseFloat(mapCenterLat), parseFloat(mapCenterLng)),
                level: parseInt(mapLevel)
            };

        map = new daum.maps.Map(mapContainer, mapOption);
        map.addOverlayMapTypeId(daum.maps.MapTypeId.ROADVIEW);
    };

    this.update = function(data) {
        for (var i = 0, n = data.length; i < n; i++) {
            _this.addMarker(data[i]);
        }
        _this.setMarkers(map);
    };

    this.move = function(_id) {
        var marker = _this.findMarker(_id);
        if (marker) {
            map.panTo(marker.getPosition());
        }
    };

    this.addMarker = function(data) {
        if (!_this.findMarker(data.id)) {
            var marker = new daum.maps.Marker();
            marker.setPosition(new daum.maps.LatLng(parseFloat(data.latitude), parseFloat(data.longitude)));
            marker.setTitle(data.name);

    //        var markerImage = new daum.maps.MarkerImage(
    //            'http://i1.daumcdn.net/dmaps/apis/nlocalblit04.png',
    //            new daum.maps.Size(31, 35),
    //            new daum.maps.Point(13, 34));
    //        marker.setImage(markerImage);
            switch (data.category) {
                case '':
                    break;
                case '':
                    break;
                default:

            }
            marker._id = data.id;
            marker._estateType = data.estateType;
            marker._category = data.category;
            marker._businessType = data.businessType;

            _markers.push(marker);
        }
    };

    this.setMarkers = function(map) {
        for(var i = 0, n = _markers.length; i < n; i++) {
            _markers[i].setMap(map);
        }
    };

    this.findMarker = function(_id) {
        return _markers.find(function(elem, index, array) {
            return elem._id == _id;
        });
    };

    this.removeMarker = function() {
        _this.setMarkers(null);
        _markers = [];
    };

    this.drawCircle = function(lat, lng) {
        circle = new daum.maps.Circle({
            map: _map,
            center : new daum.maps.LatLng(parseFloat(lat), parseFloat(lng)),
            radius: 50,
            strokeWeight: 2,
            strokeColor: '#FF00FF',
            strokeOpacity: 0.8,
            strokeStyle: 'dashed',
            fillColor: '#00EEEE',
            fillOpacity: 0.5
        });
    };

    this.removeCircle = function() {
        circle.setMap(null);
    };

    this.getLocation = function(address, callback) {
        // TODO call daum api to get lng,lat
        if (callback) {
            callback();
        }
    };

};

var _daum = new DaumMap();