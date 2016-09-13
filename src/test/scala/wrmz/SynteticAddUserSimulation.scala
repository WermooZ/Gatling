package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.steps._
import wrmz.utils._

class SynteticAddUserSimulation extends Simulation {

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

  val scn = scenario("Add Single Users").exec(addUsers.run)

  setUp(scn.inject(atOnceUsers(200)).protocols(httpConf))
}
