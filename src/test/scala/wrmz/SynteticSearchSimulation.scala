package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.steps._
import wrmz.utils._

class SynteticSearchSimulation extends Simulation {

  var feeder = UserFakerFeeder.feeder
  val httpConf = http.baseURL("http://54.78.95.58:9000")
  val databasePreparer = new DatabasePreparer()

  after {
    databasePreparer.clearDatabase()
    UserNameHolder.userNames.clear()
  }

  before {
    UserNameHolder.userNames.clear()
    databasePreparer.fillDatabase(10000)
  }

  val search = new Search(10)

  val scn = scenario("Browse Pages").exec(search.run)

  setUp(scn.inject(atOnceUsers(200)).protocols(httpConf))
}
