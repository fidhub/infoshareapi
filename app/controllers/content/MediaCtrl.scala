package controllers.content

import javax.inject.Singleton

import conf.security.{TokenCheck, TokenFailExcerption}
import domain.content.Media
import play.api.libs.json._
import play.api.mvc._
import services.content.MediaService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MediaCtrl extends InjectedController {
  val service = MediaService

  def create = Action.async(parse.json) { request =>
    val entity = Json.fromJson[Media](request.body).get
    val resp = for {
      _ <- TokenCheck.getToken(request)
      results <- service.save(entity)
    } yield results
    resp.map(_ => Ok(Json.toJson(entity))).recover {
      case _: TokenFailExcerption => Unauthorized
      case _: Exception => InternalServerError
    }
  }

  def getById(content_Id: String, id: String) = Action.async {
    implicit request: Request[AnyContent] =>
      val perms: Map[String, String] =
        Map("contentId" -> content_Id, "id" -> id)
      service.getContentMediaById(perms) map (msg => Ok(Json.toJson(msg)))
  }

  def getAll(content_id: String) = Action.async {
    implicit request: Request[AnyContent] =>
      service.getAllContentMedia(content_id) map (msg => Ok(Json.toJson(msg)))
  }
}
