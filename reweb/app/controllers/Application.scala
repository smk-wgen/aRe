package controllers

import play.api._
import play.api.mvc._
import services._
import models._
import play.api.libs.json._
import com.mongodb.casbah.Imports._

object Application extends Controller {
  var aMockList:List[Rating] = List()
  val mockFeatures = List("EaseOfUse","Durability","Battery")
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def addProduct = Action(parse.json) {req =>
      val json = req.body
      System.out.println("Json is " + json)
      json.validate[RateableObject].fold(
          valid = (validRO => {
            val rateableObjId = RateableObject.insert(validRO) //returns an option of ObjectId
            rateableObjId match {
              case Some(objectId) => {Ok(Json.toJson("msg" -> "Added product with id=" + validRO.objId)) }
              case _ => {InternalServerError("Bad things happened")}
            }
            
          }),
          invalid = (e => {BadRequest("Detected error " + JsError.toFlatJson(e))}))
      

      }
  def getProductRatings(id:String) = Action {
    //val reviewList = RatingService.getProductRatings(id)
    
    Ok(Json.toJson(aMockList))
  }
  def addProductRatings(id:String) = Action(parse.json){ req =>
    //val mbProduct = RateableObject.findByRateableObjId(id)
   
    val mbProduct = Some(new RateableObject(new ObjectId,id,mockFeatures))
    mbProduct match {
      case Some(ro) => {
        val json:JsValue = req.body
        val modifiedJson: JsObject =  Json.obj("productId" -> ro.objId) ++ json.as[JsObject]
        System.out.println("Modified Json " + modifiedJson)
        
        modifiedJson.validate[Rating].fold(
        valid = (validRating => {
          val mbId = mockAdd(validRating)//RatingService.addProductRating(validRating)
          mbId match {
            case Some(id)=> {Ok(Json.toJson("msg" -> "Added rating for product : " + validRating.objId))}
            case _ => {InternalServerError("Bad things happened")}
          }
        }),
        invalid = ( e => {BadRequest("Detected error " + JsError.toFlatJson(e))} ))
      }
      case _ => {BadRequest("Couldnt find product in db")}
    }
    
    
    
    
  }
  private def mockAdd(rating:Rating):Option[ObjectId] = {
    aMockList = aMockList :+ rating
    Some(new ObjectId)
  }
  
}