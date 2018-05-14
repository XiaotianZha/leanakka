package com.akkademy

import akka.actor.{Actor, Status}
import akka.event.Logging
import com.akkademy.message._

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
      val response :Option[Object] = map.get(key)
      response match {
        case Some(x) => sender() ! x
        case None    => sender() ! Status.Failure(new KeyNotFoundException(key))
      }
    }
    case SetIfNotExistsRequest(key,value) =>{
      val response :Option[Object] = map.get(key)
      response match {
        case Some(x) => sender() ! x
        case None    => {
          map.put(key,value)
          sender() ! value
        }
      }
    }
    case Delete(key) =>{
      val response :Option[Object] = map.get(key)
      response match {
        case Some(x) => {
          map.remove(key)
          sender() ! x
        }
        case None    => sender() ! Status.Failure(new KeyNotFoundException(key))
      }
    }
    case o =>{
      log.info("Received unknown message: {}",o)
      Status.Failure(new ClassNotFoundException())
    }
  }
}
