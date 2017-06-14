package controllers.demographics

import javax.inject.Singleton

import conf.security.{TokenCheck, TokenFailExcerption}
import domain.demographics.Gender
import play.api.mvc._
import play.api.libs.json._
import services.demographics.GenderService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class GenderCtrl extends InjectedController {
  private val service = GenderService

  def create = Action.async(parse.json) { implicit request =>
    val entity = Json.fromJson[Gender](request.body).get

    val resp = for {
      _ <- TokenCheck.getToken(request)
      results <- service.save(entity)
    } yield results
    resp.map(_ => Ok(Json.toJson(entity))).recover {
      case _: TokenFailExcerption => Unauthorized
      case _: Exception => InternalServerError
    }
  }
}
