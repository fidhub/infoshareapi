package domain.users

import java.time.{LocalDateTime => Date}

import play.api.libs.json.Json

case class ValidUser(siteId:String, userId: String, date: Date, action: String) {

  override def hashCode(): Int = {
    val prime = 31
    var result = 1
    result = prime * result + userId.hashCode
    result
  }
  override def equals(that: scala.Any): Boolean = that match {
    case that: ValidUser =>
      that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
}

object ValidUser {
  implicit val validUsersFmt = Json.format[ValidUser]
  def identity: ValidUser = ValidUser("","", Date.now(), "")
}
