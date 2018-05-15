package com.akkademy.telldemo

import java.util.concurrent.TimeoutException

import akka.actor.Status.Failure
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.askdemo.{ArticleBody, HttpResponse, ParseArticle}
import com.akkademy.message.{GetRequest, SetRequest}

import scala.concurrent.Future

class TellDemoArticleParser(
                          cacheActorPath:String,
                          httpActorPath:String,articleParserActorPath:String,
                          implicit val timeout:Timeout
                          ) extends Actor{
  val cacheActor = context.actorSelection(cacheActorPath)
  val httpActor = context.actorSelection(cacheActorPath)
  val articleParseActor = context.actorSelection(articleParserActorPath)

  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case msg @ ParseArticle(uri) =>
      val extraActor = buildExtraActor(sender(), uri)

      cacheActor.tell(GetRequest(uri),extraActor)
      httpActor.tell("test",extraActor)

      context.system.scheduler.scheduleOnce(timeout.duration,extraActor,"timeout")

  }

  private def buildExtraActor(ref: ActorRef, str: String):ActorRef={
    return context.actorOf(Props(new Actor {
      override def receive: Receive = {
        case "timeout" =>
          ref ! Failure(new TimeoutException("timeout"))
          context.stop(self)
        case HttpResponse(body) =>
          ref ! body
          context.stop(self)
        case ArticleBody(uri,body) =>
          cacheActor ! SetRequest(uri,body)
          ref ! body
          context.stop(self)

        case t =>
          println("ignoring msg: "+t.getClass)
      }
    }))
  }

}
