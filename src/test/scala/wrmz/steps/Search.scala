package wrmz.steps

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import wrmz.utils.InsertedUserNamesFeeder

object Search {
  val run = exec(http("Home")
    .get("/persons/1"))
    .pause(1)
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