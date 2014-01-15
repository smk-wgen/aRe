package controllers
import play.api._
import play.api.mvc._


object CORSAction {

  type ResultWithHeaders = Result { def withHeaders(headers: (String, String)*): Result }

  def apply(block: Request[AnyContent] => ResultWithHeaders): Action[AnyContent] = {
    Action { request =>
      block(request).withHeaders("Access-Control-Allow-Origin" -> "*")
    }
  }

  def apply(block: => ResultWithHeaders): Action[AnyContent] = {
    this.apply(_ => block)
  }
 
  def foo[AnyContent](bp:BodyParser[AnyContent])(block:Request[AnyContent] => ResultWithHeaders):Action[AnyContent] = {
    Action(bp) { request =>
      block(request).withHeaders("Access-Control-Allow-Origin" -> "*")
    }
  }
 

}
trait CORS{
  def MCORSAction[A](bp: BodyParser[A])(f: Request[A] => Result): Action[A] = {
	  Action(bp) { request =>
	    Logger.info("Calling action")
	    f(request)
	  }
  }
  
 
}