package wrmz.utils

import java.util.concurrent.ConcurrentLinkedQueue

import io.gatling.core.Predef._

object UserNameHolder {
  val userNames = new ConcurrentLinkedQueue[String]()
}

class InsertedUserNamesFeeder extends Feeder[String] {
  override def hasNext: Boolean = UserNameHolder.userNames.size() > 0
  override def next(): Map[String, String] = Map("name" -> UserNameHolder.userNames.poll())
}