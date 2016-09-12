package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.steps._
import wrmz.utils._

class SynteticSearchSimulation extends Simulation {

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

  val search = new Search(100)

  val scn = scenario("Browse Pages").exec(search.run)

  setUp(scn.inject(atOnceUsers(500)).protocols(httpConf))
}
