var ARatingService = ARatingService || {};

ARatingService.WebServiceConnector = function WebServiceConnector(productId) {
    'use strict';
   var host= "http://localhost:9000/api/";
   
   var product = {
		   id : productId
   };
   var makeCall = function(url,type,payload){
       var xmlHttp = null;

       xmlHttp = new XMLHttpRequest();
       xmlHttp.open( type, url, false );
       xmlHttp.send( payload );
       return xmlHttp.responseText;
   };
   var init = function(){
	   console.log("Initializing with product details..");
	   var productUrl = host+"product/"+productId;
	   var response = makeCall(productUrl,"GET",null);
       console.log(response);
       product.features = response.features;
	   
   };
   var buildRatingsRequest = function(){
	   console.log("Product Id is defined",productId);
       return host+"product/"+productId+"/ratings";
   };
   init();
   
   this.getRatings = function(){
        var url = buildRatingsRequest();
        var response = makeCall(url,"GET",null);
        console.log(response);
        //make a call to backend  use XmlHttpRequest
   };
   this.addRating = function(rating,who,review){
        var url = buildRatingsRequest();
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
