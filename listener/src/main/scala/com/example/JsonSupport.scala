package com.example

import com.example.BikeMessageRegistryActor.ActionPerformed

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val bikeMessageJsonFormat = jsonFormat5(BikeMessage)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}