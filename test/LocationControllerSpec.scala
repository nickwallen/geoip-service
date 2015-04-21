import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import com.sanoma.cda.geo.Point

@RunWith(classOf[JUnitRunner])
class LocationControllerSpec extends Specification {
  val fakeApplication = FakeApplication()

  "LocationController" should {

    "locate an ip address in China" in new WithApplication(fakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/locate/123.123.123.123"))

      // validate the response
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")

      // validate the location data
      val json = contentAsJson(result)
      (json \ "countryCode").as[String] must equalTo("CN")
      (json \ "countryName").as[String] must equalTo("China")
      (json \ "region").as[String] must equalTo("Beijing Shi")
      (json \ "city").as[String] must equalTo("Beijing")
      (json \ "geoPoint" \ "latitude").as[Double] must equalTo(39.9289)
      (json \ "geoPoint" \ "longitude").as[Double] must equalTo(116.3883)
      (json \ "continent").as[String] must equalTo("Asia")
    }

    "locate an ip address at Google" in new WithApplication(fakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/locate/8.8.8.8"))

      // validate the response
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")

      // validate the location data
      val json = contentAsJson(result)
      (json \ "countryCode").as[String] must equalTo("US")
      (json \ "countryName").as[String] must equalTo("United States")
      (json \ "region").as[String] must equalTo("California")
      (json \ "city").as[String] must equalTo("Mountain View")
      (json \ "geoPoint" \ "latitude").as[Double] must equalTo(37.386)
      (json \ "geoPoint" \ "longitude").as[Double] must equalTo(-122.0838)
      (json \ "continent").as[String] must equalTo("North America")
    }
  }
}
