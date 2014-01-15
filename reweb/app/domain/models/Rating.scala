package domain.models

/**
 * Created by skunnumkal on 1/12/14.
 */
class Rating(val ratingObj:RateableObject,val ratingSource:Option[String], val ratingMap :Map[String,Int],
             val review : Option[String])
