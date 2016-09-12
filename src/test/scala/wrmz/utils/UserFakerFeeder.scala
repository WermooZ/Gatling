package wrmz.utils

import faker.Name

case class User(name: String, age: Int) {}

object UserFakerFeeder {

  val feeder = Iterator.continually(
    User(Name.name, scala.util.Random.nextInt(100) + 1)
  )
}
