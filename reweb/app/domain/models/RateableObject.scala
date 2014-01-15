package domain.models

/**
 * Created by skunnumkal on 1/12/14.
 */
class RateableObject(val objId:String,val features:List[String]) {

  def this(objId:String) = this(objId,Nil)

  override def hashCode = objId.hashCode
  override def equals(other: Any) = other match {
    case that: RateableObject => this.objId.equals(that.objId)
    case _ => false
  }

}
