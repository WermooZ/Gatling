package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.steps._
import wrmz.utils._

class RequirementsSimulation2 extends Simulation {

  var feeder = UserFakerFeeder.feeder
  val httpConf = http.baseURL("http://localhost:9000")
  val databasePreparer = new DatabasePreparer()

  after {
    databasePreparer.clearDatabase()
    UserNameHolder.userNames.clear()
  }

  before {
    UserNameHolder.userNames.clear()
    databasePreparer.fillDatabase(250)
  }

  val addUsers = new AddUsers(10)
  val addUserBulks = new AddUserBulks(10, 10)
  val browse = new Browse(5)
  val search = new Search(10)

  val scn = scenario("Check Requirements 500 users").exec(addUsers.run, addUserBulks.run, browse.run, search.run)

  setUp(scn.inject(atOnceUsers(500)).protocols(httpConf)).assertions(
    global.responseTime.max.lessThan(2),
    global.successfulRequests.percent.greaterThan(95)
  )
}
