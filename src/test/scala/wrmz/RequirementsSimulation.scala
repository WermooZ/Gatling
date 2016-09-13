package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.session._
import scala.concurrent.duration._

import wrmz.utils._
import wrmz.steps._

class RequirementsSimulation extends Simulation {

  var feeder = UserFakerFeeder.feeder
  val httpConf = http.baseURL("http://54.78.95.58:9000")
  val utils = new Utils()

  after {
    UserNameHolder.userNames.clear()
  }

  before {
    UserNameHolder.userNames.clear()
    utils.resetDatabase()
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
