package io.github.ttharmabalan.screen_function

import java.util.UUID

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import io.github.ttharmabalan.screen_function.RobotActor.RobotMessage

object MasterActor {
  def init(): Behavior[Summon] = Behaviors.receive{
    case (ctx,SummonRobotActor(receiver)) =>
      receiver ! ctx.spawn(RobotActor.init(), s"robot-actor${UUID.randomUUID()}")
      Behaviors.same
  }


  sealed trait Summon

  case class SummonRobotActor(receiver: ActorRef[ActorRef[RobotMessage]]) extends Summon
}
