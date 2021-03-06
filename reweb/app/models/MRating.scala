package models

import com.novus.salat.global._
import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoURI
import se.radley.plugin.salat._
import com.novus.salat.dao._
import play.api.Play
import mongoContext._
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class MRating(@Key("_id") id: ObjectId = new ObjectId,objId:String,ratingSource:Option[String],ratingMap :Map[String,Int],
    review : Option[String])

object MRating extends ModelCompanion[MRating,ObjectId]{
  val db:String = Play.current.configuration.getString("mongodb.default.db").getOrElse("my_db")
  val ratingsCollection = MongoConnection(MongoURI(mongoUri))(db)("ratings")
  val dao = new SalatDAO[MRating, ObjectId](collection = ratingsCollection) {}
  
  def findRatings(objId:String):List[MRating] = {
    MRating.find(MongoDBObject("objId" -> objId)).toList
  }
  private def json2Object(productId:String,srcId:Option[String],strMap:Map[String,Int],reviewText:Option[String]):MRating = {
    println("Generate Rating object from Json")
    val ratingsMap:Map[String,Int] = strMap //.map{case(k,v) => (k,v.toInt)}
    new MRating(new ObjectId,productId,srcId,ratingsMap,reviewText)
  }

  
  implicit val ratingReads = (
    (__ \ "productId").read[String] and
      (__ \ "sourceId").read[Option[String]] and
      (__ \ "ratingMap").read[Map[String,Int]] and
      (__ \ "review").read[Option[String]] 
    )(MRating.json2Object _)

  implicit object RatingWriter extends Writes[MRating]{
    def writes(aRating:MRating):JsValue  = {
      Json.obj("productId" -> aRating.objId,"sourceId" -> aRating.ratingSource , "ratingMap" -> aRating.ratingMap,
          "review" -> aRating.review)
    }
  }
}