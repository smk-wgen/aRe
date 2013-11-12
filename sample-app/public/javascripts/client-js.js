var ARatingService = ARatingService || {};

ARatingService.WebServiceConnector = function WebServiceConnector() {
    'use strict';
   var host= "http://localhost:9000/api";
   var buildRatingsRequest = function(productId){
       return host+"/product/"+productId+"/ratings";
   };
   var makeCall = function(url,type,payload){
       var xmlHttp = null;

       xmlHttp = new XMLHttpRequest();
       xmlHttp.open( type, url, false );
       xmlHttp.send( payload );
       return xmlHttp.responseText;
   };
   this.getRatings = function(productId){
        var url = buildRatingsRequest(productId);
        var response = makeCall(url,"GET",null);
        console.log(response);
        //make a call to backend  use XmlHttpRequest
   };
   this.addRating = function(productId,rating,who,review){
        var url = buildRatingsRequest(productId);
        var ratingObj = {
            'sourceId' : who,
            'ratingMap' : rating,
            'review' : review
        };
        var response = makePostCall(url,ratingObj);
       console.log(response);
   };
   var makePostCall = function(url,payload){
	   var xmlhttp = null;

       xmlhttp = new XMLHttpRequest();
	   xmlhttp.open("POST", url);
	   xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	   xmlhttp.send(JSON.stringify(payload));
   };


};
