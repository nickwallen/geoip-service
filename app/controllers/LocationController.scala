package controllers

import com.sanoma.cda.geoip.MaxMindIpGeo
import com.sanoma.cda.geoip.IpLocation
import play.api.mvc.Controller
import play.api.Play
import play.api.mvc.Action
import play.api.libs.json.JsPath
import com.sanoma.cda.geo.Point
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object LocationController extends Controller {

  val cacheSize: Int = Play.current.configuration.getInt("cache.size").getOrElse(1000)
  val synchronized: Boolean = Play.current.configuration.getBoolean("synchronized").getOrElse(false)
  val maxmindPath: String = Play.current.configuration.getString("maxmind.path").getOrElse("GeoLite2-City.mmdb")

  val locator = new MaxMindIpGeo(
    getClass.getClassLoader.getResourceAsStream(maxmindPath),
    cacheSize,
    synchronized,
    Set())

  def get(ip: String) = Action {
    getLocation(ip) map { location =>
      Ok(toJson(location))
    } getOrElse {
      Ok("not found")
    }
  }

  def getLocation(ip: String) = locator.getLocation(ip)

  def toJson(loc: IpLocation): JsValue = {
    Json.obj(
      "countryCode" -> loc.countryCode,
      "countryName" -> loc.countryName,
      "region" -> loc.region,
      "city" -> loc.city,
      "geoPoint" -> Json.obj(
        "latitude" -> loc.geoPoint.get.latitude,
        "longitude" -> loc.geoPoint.get.longitude
      ),
      "postalCode" -> loc.postalCode,
      "continent" -> loc.continent
    )
  }
}

