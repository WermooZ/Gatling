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

object AddOne {
  val headers = Map("Content-Type" -> "application/json")

  val run = exec(http("Add user")
    .post("/persons")
    .headers(headers)
    .body(StringBody("${requestJson}")))
    .pause(1)
}

class AddUsers(var quantity: Int = 10) {

  val feeder = UserFakerFeeder.feeder
  val headers = Map("Content-Type" -> "application/json")

  val run = repeat(quantity, "i") {
    exec(session => {
      var user = feeder.next()
      session
        .set("requestJson", Json(user).toBareString)
        .set("userName", user.name)
    })
      .exec(AddOne.run)
      .pause(1)
      .exec(session => {
        UserNameHolder.userNames.offer(session.get("userName").as[String])
        session
      })
  }
}
