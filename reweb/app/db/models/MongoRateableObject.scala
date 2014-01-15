package db.models

import domain.models.RateableObject
import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import org.bson.types._
import play.api.Play
import com.mongodb.casbah.MongoURI
import models.mongoContext._
import com.novus.salat.dao.{ModelCompanion, SalatDAO}
import play.api.libs.json._
import play.api.libs.functional.syntax._


/**
 * Created by skunnumkal on 1/12/14.
 */
case class MongoRateableObject(@Key("_id") id: ObjectId = new ObjectId,
                               override val objId:String,override val features:List[String])
  extends RateableObject(objId,features)

object MongoRateableObject extends ModelCompanion[MongoRateableObject,ObjectId]{
  val db:String = Play.current.configuration.getString("mongodb.default.db").getOrElse("my_db")
  val roCollection = MongoConnection(MongoURI(mongoUri))(db)("ros")
  val dao = new SalatDAO[MongoRateableObject, ObjectId](collection = roCollection) {}

  def findByRateableObjId(id:String):Option[MongoRateableObject] = {
    MongoRateableObject.findOne(MongoDBObject("objId" -> id))
  }

  implicit val mRObjreads:Reads[MongoRateableObject] =
    ((__ \ "productId").read[String] and
      (__ \ "features").read[List[String]] )(MongoRateableObject.json2Object _)

  implicit object MongoRateableObjectWriter extends Writes[MongoRateableObject]{
      def writes(aRateableObject:MongoRateableObject):JsValue = {
        Json.obj("productId" -> aRateableObject.objId,"features" -> aRateableObject.features)
      }
  }

  private def json2Object(objId:String,features:List[String]) = new MongoRateableObject(new ObjectId,objId,features)

}


