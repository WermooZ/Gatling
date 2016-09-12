package wrmz.steps

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.utils.InsertedUserNamesFeeder
import scala.concurrent.duration._

object Search {
  val run = exec(http("Home")
    .get("/persons/1"))
    .pause(100 milliseconds)
    .feed(new InsertedUserNamesFeeder())
    .exec(http("Search User")
      .get("/persons/name/${name}"))
    .pause(1)
}

class Search(var quantity: Int = 10) {
  val run = repeat(quantity, "i") {
    exec(Search.run)
  }
}