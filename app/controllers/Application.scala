package controllers

import models.{Anythings, Somethings}
import pjax.Pjax
import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB
import play.api.mvc._
import views.{html => v}

object Application extends Controller with Pjax {

  implicit val currentApp = Play.current

  def index = PjaxAction { implicit req =>
    val title = concatTitle()
    val view = v.index()
    if (req.pjaxEnabled)
      Ok(v.pjaxPartial(title)(view))
    else
      Ok(v.layout(title)(view))
  }

  val someInputForm = Form("someInput" -> nonEmptyText)

  def createSomething = PjaxAction { implicit req =>
    someInputForm.bindFromRequest().fold(
      formWithError => {
        val title = concatTitle(Some("Some Thing went wrong"))
        val list = DB withConnection { implicit c => Somethings.findAll }
        val view = v.listSomethings(list, formWithError)
        if (req.pjaxEnabled)
          BadRequest(v.pjaxPartial(title)(view))
        else
          BadRequest(v.layout(title)(view))
      },
      input => {
        DB withConnection { implicit c => Somethings.create(input) }
        Redirect(routes.Application.listSomethings())
      }
    )
  }

  def listSomethings = PjaxAction { implicit req =>
    val title = concatTitle(Some("A List of Some Things"))
    val list = DB withConnection { implicit c => Somethings.findAll }
    val view = v.listSomethings(list, someInputForm)
    if (req.pjaxEnabled)
      Ok(v.pjaxPartial(title)(view))
    else
      Ok(v.layout(title)(view))
  }

  def anythingsForm = PjaxAction { implicit req =>
    val title = concatTitle(Some("Create Any Thing"))
    val view = v.anythingsForm(someInputForm)
    if (req.pjaxEnabled)
      Ok(v.pjaxPartial(title)(view))
    else
      Ok(v.layout(title)(view))
  }

  def createAnything = PjaxAction { implicit req =>
    someInputForm.bindFromRequest().fold(
      formWithError => {
        val title = concatTitle(Some("Any Thing went wrong"))
        val view = v.anythingsForm(formWithError)
        if (req.pjaxEnabled)
         /*
          * The problem is that the submit URL is pushed to the browser history
          * In the first example the GET & POST are equal so that is not a problem
          *
          * There are 2 ways we can fix this:
          *
          * 1. Backend: set the X-PJAX-URL header
          * 2. javascript: tell pjax not to push the submit URL on the browser history
          *
          * here is solution 1:
          */
          BadRequest(v.pjaxPartial(title)(view)).withHeaders("X-PJAX-URL" -> routes.Application.anythingsForm().url)
        else
          BadRequest(v.layout(title)(view))
      },
      input => {
        DB withConnection { implicit c => Anythings.create(input) }
        Redirect(routes.Application.redirect())
      }
    )
  }

  def redirect = PjaxAction { implicit req =>
    val title = concatTitle(Some("A List of Any Things"))
    val list = DB withConnection { implicit c => Anythings.findAll }
    val view = v.listAnythings(list)
    if (req.pjaxEnabled)
     /*
      * The problem is that pjax does not push the url of the redirect to the browser history
      *
      * the only way (that we know of) to fix this is to explicitly set the X-PJAX-URL
      */
      Ok(v.pjaxPartial(title)(view)).withHeaders("X-PJAX-URL" -> routes.Application.redirect().url)
    else
      Ok(v.layout(title)(view))
  }

  private def concatTitle(sub: Option[String] = None) = {
    val mainTitle = "PJAX Sample App"
    sub.fold(mainTitle) { subTitle => mainTitle + " - " + subTitle }
  }

}