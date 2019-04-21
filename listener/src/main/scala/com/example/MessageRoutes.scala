package com.example

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import com.example.BikeMessageRegistryActor._
import akka.pattern.ask
import akka.util.Timeout

trait MessageRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[MessageRoutes])

  val bikeMessageRegistryActor: ActorRef

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val bikeMessageRoutes: Route =
    pathPrefix("listener") {
      pathEnd {
        post {
          entity(as[BikeMessage]) {
            message =>
              val messageReceived: Future[ActionPerformed] =
                (bikeMessageRegistryActor ? AddMessage(message)).mapTo[ActionPerformed]
              onSuccess(messageReceived) { performed =>
                log.info(
                  "Message received with id {} lat {} and long as {}",
                  message.uid, message.lat, message.long, performed.description)
                complete((StatusCodes.OK, performed))
              }
          }
        }
      }
    }
}
