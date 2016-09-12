package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.session._
import scala.util.Random
import faker.Name
import rapture.json._
import rapture.json.jsonBackends.jawn._
import rapture.json.formatters.compact

import scala.concurrent.duration._

//request
import rapture.codec._
import scalaj.http.Http



import wrmz.utils._
import wrmz.steps._


class RequirementsSimulation extends Simulation {

  var feeder = UserFakerFeeder.feeder
  val httpConf = http.baseURL("http://localhost:9000")

  after {
      Http("http://localhost:9000/persons/reset")
      UserNameHolder.userNames.clear()
  }

  before {
    UserNameHolder.userNames.clear()

    val url = "http://localhost:9000/persons/bulk"
    var users : List[User] = List()
    for( i <- 1 to 250 ) {
      var user = feeder.next()
      users = user :: users
    }

    val requestJson = Json(users).toBareString
    Http(url).postData(requestJson)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8").asString
  }

  val addUsers = new AddUsers(10)
  val addUserBulks = new AddUserBulks(10, 10)
  val browse = new Browse(5)
  val search = new Search(10)

  val scn = scenario("Check Requirements").exec(addUsers.run, addUserBulks.run, browse.run, search.run)

  setUp(scn.inject(atOnceUsers(200)).protocols(httpConf)).assertions(
    global.responseTime.max.lessThan(2),
    global.successfulRequests.percent.greaterThan(95)
  )
}
