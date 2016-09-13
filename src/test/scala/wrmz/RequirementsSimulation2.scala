package wrmz

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.steps._
import wrmz.utils._

class RequirementsSimulation2 extends Simulation {

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

  val scn = scenario("Check Requirements 500 users").exec(addUsers.run, addUserBulks.run, browse.run, search.run)

  setUp(scn.inject(atOnceUsers(500)).protocols(httpConf)).assertions(
    global.responseTime.max.lessThan(2),
    global.successfulRequests.percent.greaterThan(95)
  )
}
