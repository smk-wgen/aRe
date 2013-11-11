package services

import models._

object RatingService {
  
  def getProductRatings(productId:String):List[Rating]={
    
     Rating.findRatings(productId)
    
  }
  def addProductRating(rating:Rating) = {
    Rating.insert(rating)
  }

}