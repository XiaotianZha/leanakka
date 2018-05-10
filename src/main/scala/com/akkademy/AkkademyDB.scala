package com.akkademy

import akka.actor.Actor
import akka.event.Logging
import com.akkademy.message.SetRequest

import scala.collection.mutable

class AkkademyDB extends Actor{
  val map = new mutable.HashMap[String,Object]
  val log = Logging(context.system, this)

  override def receive ={
    case SetRequest(key,value) =>{
      log.info("Received SetRequest - key: {} value: {}",key,value)
      map.put(key,value)
    }
    case o =>  log.info("Received unknown message: {}",o)
  }
}
