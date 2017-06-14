package controllers.content

import play.api.mvc._
import javax.inject.Singleton

import conf.security.{TokenCheck, TokenFailExcerption}
import domain.content.ContentType
import play.api.libs.json._
import services.content.ContentTypeService
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ContentTypeCtrl extends InjectedController {
  val service = ContentTypeService

  def create = Action.async(parse.json) { request =>
    val entity = Json.fromJson[ContentType](request.body).get
    val response = for {
      _ <- TokenCheck.getToken(request)
      results <- service.save(entity)
    } yield results
    response.map(_ => Ok(Json.toJson(entity))).recover {
      case _: TokenFailExcerption => Unauthorized
      case _: Exception => InternalServerError
    }
  }

  def getById(id: String) = Action.async {
    implicit request: Request[AnyContent] =>
      service.getContentTypeById(id) map { msg =>
        Ok(Json.toJson(msg))
      }
  }

  def getAll = Action.async { implicit request: Request[AnyContent] =>
    service.getAllContentTypes map { msg =>
      Ok(Json.toJson(msg))
    }
  }

}
