package models

import com.novus.salat.global._
import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

import com.mongodb.casbah.MongoURI
import com.novus.salat.dao._
import play.api.Play
import mongoContext._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class RateableObject(@Key("_id") id: ObjectId = new ObjectId, objId:String, features:List[String] = Nil)

object RateableObject extends ModelCompanion[RateableObject,ObjectId]{
  val db:String = Play.current.configuration.getString("mongodb.default.db").getOrElse("my_db")
  val roCollection = MongoConnection(MongoURI(mongoUri))(db)("ros")
  val dao = new SalatDAO[RateableObject, ObjectId](collection = roCollection) {}
  
  def findByRateableObjId(id:String):Option[RateableObject] = {
    RateableObject.findOne(MongoDBObject("objId" -> id))
  }
  private def json2Object(objId:String,features:List[String]):RateableObject = {
    new RateableObject(new ObjectId,objId,features)
  } 
  implicit val objReads = (
      (__ \ "productId").read[String] and
      (__ \ "features").read[List[String]] 
    )(RateableObject.json2Object _)
  
  implicit object RateableObjectWriter extends Writes[RateableObject]{
    def writes(aRateableObject:RateableObject):JsValue  = {
      Json.obj("productId" -> aRateableObject.objId,"features" -> aRateableObject.features)
    }
  }
  
}