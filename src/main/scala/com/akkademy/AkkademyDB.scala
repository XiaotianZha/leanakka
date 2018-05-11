package com.akkademy

import akka.actor.{Actor, Status}
import akka.event.Logging
import com.akkademy.message.{GetRequest, KeyNotFoundException, SetRequest}

import scala.collection.mutable

class AkkademyDB extends Actor{
  val map = new mutable.HashMap[String,Object]
  val log = Logging(context.system, this)

  override def receive ={
    case SetRequest(key,value) =>{
      log.info("Received SetRequest - key: {} value: {}",key,value)
      map.put(key,value)
      sender() ! Status.Success
    }
    case GetRequest(key) =>{
      log.info("Received GetRequest - key {}",key)
      val respons :Option[Object] = map.get(key)
      respons match {
        case Some(x) => sender() ! x
        case None    => sender() ! Status.Failure(new KeyNotFoundException(key))
      }
    }
    case o =>{
      log.info("Received unknown message: {}",o)
      Status.Failure(new ClassNotFoundException())
    }
  }
}
