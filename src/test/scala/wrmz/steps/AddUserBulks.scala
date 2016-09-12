package wrmz.steps

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import rapture.json.Json
import wrmz.utils._

import io.gatling.core.session._
import faker.Name
import rapture.json._
import rapture.json.jsonBackends.jawn._
import rapture.json.formatters.compact
import scala.concurrent.duration._

object AddBulk {
  val headers = Map("Content-Type" -> "application/json")

  val run = exec(http("Add user bulk")
    .post("/persons/bulk")
    .headers(headers)
    .body(StringBody("${requestJson}")))
    .pause(1)
}

class AddUserBulks(var bulks: Int = 10, var quantity: Int = 10) {

  val feeder = UserFakerFeeder.feeder
  val headers = Map("Content-Type" -> "application/json")

  val run = repeat(bulks, "i") {
    exec(session => {
      var users = getUsers(quantity)
      session
        .set("requestJson", Json(users).toBareString)
        .set("userNames", extractUserNames(users))
    })
      .exec(AddBulk.run)
      .pause(1)
      .exec(session => {
        addUserNamesToStorage(session.get("userNames").as[List[String]])
        session
      })
  }

  def getUsers(quantity: Int): List[User] = {
    var users : List[User] = List()
    for( i <- 1 to quantity ) {
      var user = feeder.next()
      users = user :: users
    }
    users
  }

  def extractUserNames(users : List[User]) = {
    var names : List[String] = List()
    for (user <- users) {
      names = user.name :: names
    }
    names.reverse
  }

  def addUserNamesToStorage(names : List[String]) = {
    for (name <- names) {
      UserNameHolder.userNames.offer(name)
    }
  }

}

