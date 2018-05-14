package com.akkademy.askdemo

import akka.actor.Actor
import akka.util.Timeout
import akka.pattern.ask
import com.akkademy.message.{GetRequest, SetRequest}

import scala.concurrent.Future

class AskDemoArticleParser(
                          cacheActorPath:String,
                          httpActorPath:String,articleParserActorPath:String,
                          implicit val timeout:Timeout
                          ) extends Actor{
  val cacheActor = context.actorSelection(cacheActorPath)
  val httpActor = context.actorSelection(cacheActorPath)
  val articleParseActor = context.actorSelection(articleParserActorPath)

  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case ParseArticle(uri) =>
      val senderRef = sender()

      val cacheResult = cacheActor ? GetRequest(uri)
      val result = cacheResult.recoverWith{
//        ask the articleParseActor
        case _:Exception =>
          val fRawResult = httpActor ? uri

          fRawResult flatMap{
            case HttpResponse(rawArticle) =>
              articleParseActor ? ParseHtmlArticle(uri,rawArticle)
            case x=>
              Future.failed(new Exception("unknown response"))
          }
      }

      result onComplete{
        case scala.util.Success(x:String) =>
          println("cached result!")
          senderRef ! x
        case scala.util.Success(x:ArticleBody) =>
          cacheActor ! SetRequest(uri,x.body)
          senderRef ! x
        case scala.util.Failure(t) =>
          senderRef ! akka.actor.Status.Failure(t)
        case x =>
          println("unknown message! "+x)

      }
  }

}
