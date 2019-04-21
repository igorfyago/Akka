package com.example

import akka.actor.{ Actor, ActorLogging, Props }

case class BikeMessage(uid: String, lat: Double, long: Double, timestamp: Long, speed: Option[Double])

object BikeMessageRegistryActor {
  final case class ActionPerformed(description: String)
  final case class AddMessage(message: BikeMessage)

  def props: Props = Props[BikeMessageRegistryActor]
}

class BikeMessageRegistryActor extends Actor with ActorLogging {
  import BikeMessageRegistryActor._

  def receive: Receive = {
    case AddMessage(message) =>
      sender() ! ActionPerformed(s"Bike message received with content ${message.uid}, ${message.lat}, ${message.long}")
  }
}