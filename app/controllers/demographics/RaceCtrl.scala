package controllers.demographics

import javax.inject.Singleton

import conf.security.{TokenCheck, TokenFailExcerption}
import domain.demographics.Race
import play.api.libs.json._
import play.api.mvc._
import services.demographics.RaceService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class RaceCtrl extends InjectedController {
  private val service = RaceService

  def create = Action.async(parse.json) { implicit request =>
    val entity = Json.fromJson[Race](request.body).get

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
