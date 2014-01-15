package services

import models._

object RatingService {
  
  def getProductRatings(productId:String):List[MRating]={
    
     MRating.findRatings(productId)
    
  }
  def addProductRating(rating:MRating) = {
    MRating.insert(rating)
  }

}