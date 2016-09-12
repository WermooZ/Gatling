package wrmz.steps

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Browse {
  val run = exec(
    exec(session => {
      session.set("page", session.get("i").as[Int] + 1)
    }).exec(http("Browse Page")
        .get("/persons/${page}"))
        .pause(1)
  )
}

class Browse(var quantity: Int = 10) {
  val run = repeat(quantity, "i") {
    exec(Browse.run)
  }
}
