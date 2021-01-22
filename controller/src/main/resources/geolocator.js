(function () {
    'use strict';
    setGeoLocation();

    function setGeoLocation() {
        if (!navigator.geolocation) {
            let div = document.getElementById("geoLocation");

            div.innerText = "not supported";

        } else {
            navigator.geolocation.getCurrentPosition(onSuccess, onError);
        }
    }


    // handle success case
    function onSuccess(position) {
        let div = document.getElementById("geoLocation");
        let newDiv = document.createElement("p");
        const {
            latitude,
            longitude
        } = position.coords;
        newDiv.setAttribute("geo", `(${latitude},${longitude})`);
        newDiv.innerText = `(${latitude},${longitude})`;
        div.append(newDiv);

    }

    // handle error case
    function onError() {
        div.innerText = "Error GeoLocation not available";
    }


})();