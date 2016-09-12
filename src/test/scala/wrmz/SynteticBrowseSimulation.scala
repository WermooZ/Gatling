package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.steps._
import wrmz.utils._

class SynteticBrowseSimulation extends Simulation {

  var feeder = UserFakerFeeder.feeder
  val httpConf = http.baseURL("http://localhost:9000")
  val databasePreparer = new DatabasePreparer()

  after {
    databasePreparer.clearDatabase()
    UserNameHolder.userNames.clear()
  }

  before {
    UserNameHolder.userNames.clear()
    databasePreparer.fillDatabase(1000)
  }

  val browse = new Browse(10)

  val scn = scenario("Browse Pages").exec(browse.run)

  setUp(scn.inject(atOnceUsers(500)).protocols(httpConf))
}