package wrmz.utils

import rapture.json.Json
import scalaj.http.Http
import rapture.json.jsonBackends.jawn._
import rapture.json.formatters.compact

class DatabasePreparer() {
  val url = "http://54.78.95.58:9000/persons/bulk"
  val feeder = UserFakerFeeder.feeder

  def fillDatabase(userQuantity: Int = 250) {
    var users: List[User] = List()
    for (i <- 1 to userQuantity) {
      var user = feeder.next()
      users = user :: users

      UserNameHolder.userNames.offer(user.name)
    }

    val requestJson = Json(users).toBareString
    Http(url).postData(requestJson)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8").asString
  }

  def clearDatabase(): Unit = {
    Http("http://54.78.95.58:9000/persons/reset")
  }
}
