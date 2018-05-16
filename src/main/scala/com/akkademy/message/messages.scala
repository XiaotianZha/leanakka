package com.akkademy.message

import akka.actor.ActorRef

trait Request
case class SetRequest(key: String, value: Object, sender: ActorRef = ActorRef.noSender) extends Request
case class GetRequest(key: String, sender: ActorRef = ActorRef.noSender) extends Request
case class SetIfNotExistsRequest(key:String, value:Object, sender: ActorRef = ActorRef.noSender) extends Request
case class DeleteRequest(key:String, sender: ActorRef = ActorRef.noSender) extends Request

case class KeyNotFoundException(key: String) extends Exception

case class Connected() //Used as a ping
