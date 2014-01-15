package controllers

import _root_.db.models.MongoRateableObject
import play.api._
import play.api.mvc._
import services._
import models._
import play.api.libs.json._
import com.mongodb.casbah.Imports._

object Application extends Controller {
  var aMockList:List[MRating] = List()
  val mockFeatures = List("EaseOfUse","Durability","Battery")
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  def getProduct(id:String) = CORSAction {
    val mockObj = new MongoRateableObject(new ObjectId,"prdCode",mockFeatures)
    Ok(Json.toJson(mockObj))
  }
  def addProduct = Action(parse.json) {req =>
      val json = req.body
      System.out.println("Json is " + json)
      json.validate[MongoRateableObject].fold(
          valid = (validRO => {
            val rateableObjId = MongoRateableObject.insert(validRO) //returns an option of ObjectId
            rateableObjId match {
              case Some(objectId) => {Ok(Json.toJson("msg" -> "Added product with id=" + validRO.objId)) }
              case _ => {InternalServerError("Bad things happened")}
            }
            
          }),
          invalid = (e => {BadRequest("Detected error " + JsError.toFlatJson(e))}))
      

      }
  def getProductRatings(id:String) = CORSAction {
    //val reviewList = RatingService.getProductRatings(id)
    
    Ok(Json.toJson(aMockList))
  }
  def addProductRatings(id:String) = CORSAction.foo(parse.json){ req =>
    //val mbProduct = RateableObject.findByRateableObjId(id)
   
    val mbProduct = Some(new MongoRateableObject(new ObjectId,id,mockFeatures))
    mbProduct match {
      case Some(ro) => {
        val json:JsValue = req.body
        println("Adding product rating " + json)
        val modifiedJson: JsObject =  Json.obj("productId" -> ro.objId) ++ json.as[JsObject]
        System.out.println("Modified Json " + modifiedJson)
        
        modifiedJson.validate[MRating].fold(
        valid = (validRating => {
          val mbId = mockAdd(validRating)//RatingService.addProductRating(validRating)
          mbId match {
            case Some(id)=> {Ok(Json.toJson("msg" -> "Added rating for product : " + validRating.objId))
              }
            case _ => {InternalServerError("Bad things happened")}
          }
        }),
        invalid = ( e => {BadRequest("Detected error " + JsError.toFlatJson(e))} ))
      }
      case _ => {BadRequest("Couldnt find product in db")}
    }
    
    
    
    
  }
  private def mockAdd(rating:MRating):Option[ObjectId] = {
    aMockList = aMockList :+ rating
    Some(new ObjectId)
  }
  
//  def options(url: String) = Action {
//	Ok("").withHeaders(
//	"Access-Control-Allow-Origin" -> "*",
//	"Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
//	"Access-Control-Allow-Headers" -> "Content-Type, X-Requested-With, Accept",
//	// cache access control response for one day
//	"Access-Control-Max-Age" -> (60 * 60 * 24).toString
//	)
//  }
  
}