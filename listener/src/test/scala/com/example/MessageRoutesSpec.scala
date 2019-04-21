package com.example

import java.util.{UUID}

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class MessageRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest
    with MessageRoutes {

  override val bikeMessageRegistryActor: ActorRef =
    system.actorOf(BikeMessageRegistryActor.props, "bikeMessageRegistry")

  lazy val routes = bikeMessageRoutes

    "be able to add messages (POST /listener)" in {
      val uuid = UUID.randomUUID().toString
      val timeNow = System.currentTimeMillis()
      val bikeMessage = BikeMessage(uuid, 5.0, 7.0, timeNow, Some(50.1))
      val bikeMessageEntity = Marshal(bikeMessage).to[MessageEntity].futureValue // futureValue is from ScalaFutures

      val request = Post("/listener").withEntity(bikeMessageEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        contentType should ===(ContentTypes.`application/json`)

        entityAs[String] should ===(
          s"""{"description":"Bike message received with content ${bikeMessage.uid}, ${bikeMessage.lat}, ${bikeMessage.long}"}""".stripMargin)
      }
    }

  

}