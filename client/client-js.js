var ARatingService = ARatingService || {};

ARatingService.WebServiceConnector = function WebServiceConnector() {
    'use strict';
   var host= "localhost:9000";
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
        var response = makeCall(url,"POST",ratingObj);
       console.log(response);
   };


};
