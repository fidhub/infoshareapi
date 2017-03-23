package conf.security

import domain.users.User
import services.users.UserService

import scala.concurrent.Future

/**
  * Created by hashcode on 2016/10/06.
  */
object LoggedInUser {
  def user(email:String): Future[Option[User]] = {
    UserService.getSiteUser(email)
  }
}
