package com.akkademy

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.akkademy.message.SetRequest
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._

class AkkaDemyDBSpec extends FunSpecLike with Matchers{
  implicit val system =ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  describe("akkademyDB"){
    describe("given SetRequest"){
      it("should place key/value into map"){
        val actorRef = TestActorRef(new AkkademyDB)
        actorRef ! SetRequest("key","value")

        val akkademyDB = actorRef.underlyingActor
        akkademyDB.map.get("key") should equal (Some("value"))
      }
    }
  }

}
