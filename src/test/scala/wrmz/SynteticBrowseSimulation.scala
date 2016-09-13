package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.steps._
import wrmz.utils._

class SynteticBrowseSimulation extends Simulation {

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

  val browse = new Browse(5)

  val scn = scenario("Browse Pages").exec(browse.run)

  setUp(scn.inject(atOnceUsers(200)).protocols(httpConf))
}
