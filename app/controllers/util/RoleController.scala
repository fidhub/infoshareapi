package controllers.util

import javax.inject.Singleton

import domain.security.{Roles, TokenFailException}
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, InjectedController, Request}
import services.security.TokenCheckService
import services.util.RolesService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class RoleController extends InjectedController {

  def create = Action.async(parse.json) { request =>
    val input = request.body
    val entity = Json.fromJson[Roles](input).get
    val response = for {
      _ <- TokenCheckService.apply.getToken(request)
      results <- RolesService.save(entity)
    } yield results
    response.map(_ => Ok(Json.toJson(entity))).recover {
      case _: TokenFailException => Unauthorized
      case _: Exception => InternalServerError
    }
  }

  def getRoleById(id: String) = Action.async {
    implicit request: Request[AnyContent] =>
      val response = for {
        _ <- TokenCheckService.apply.getTokenfromParam(request)
        results <- RolesService.getRoleById(id)
      } yield results
      response.map(ans => Ok(Json.toJson(ans))).recover {
        case _: TokenFailException => Unauthorized
        case _: Exception => InternalServerError
      }
  }

  def getRoles = Action.async { implicit request: Request[AnyContent] =>
    val response = for {
      _ <- TokenCheckService.apply.getTokenfromParam(request)
      results <- RolesService.getRoles
    } yield results
    response.map(ans => Ok(Json.toJson(ans))).recover {
      case _: TokenFailException => Unauthorized
      case _: Exception => InternalServerError
    }
  }
}
