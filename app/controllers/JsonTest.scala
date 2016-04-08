package controllers

import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

case class Location(lat: Double, long: Double)
case class Place(name: String, location: Location)

object JsonTest extends Controller {

  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "lat").write[Double] and
    (JsPath \ "long").write[Double]
  )(unlift(Location.unapply))

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
    (JsPath \ "long").read[Double]
  )(Location.apply _)

  implicit val placeWrites: Writes[Place] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "location").write[Location]
  )(unlift(Place.unapply))

  implicit val placeReads: Reads[Place] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "location").read[Location]
  )(Place.apply _)


  var list: List[Place] = {
    List(
      Place(
        "Sandleford",
        Location(51.377797, -1.318965)
      ),
      Place(
        "Watership Down",
        Location(51.235685, -1.309197)
      )
    )
  }

  def index = Action {
    Ok(views.html.jsontest())
  }

  def places = Action {
    Ok(Json.toJson(list))
  }

  def savePlace = Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Place]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      place => {
        save(place)
        Ok(Json.obj("status" -> "OK", "message" -> ("Place '" + place.name + "' saved.")))
      }
    )
  }

  def save(place: Place) = {
    list = list ::: List(place)
  }
}
