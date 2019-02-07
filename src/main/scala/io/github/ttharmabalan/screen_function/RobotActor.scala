package io.github.ttharmabalan.screen_function

import java.awt.image.BufferedImage
import java.awt.{Rectangle, Robot}

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object RobotActor {
  def init(): Behavior[RobotMessage] = Behaviors.setup{ ctx =>
    val robot = new Robot()

    active(robot)
  }

  def active(r: Robot): Behavior[RobotMessage] = Behaviors.receiveMessage{
    case MoveMouse(x,y) =>
      r.mouseMove(x,y)
      Behaviors.same

    case TakeScreenshot(rectangle, ref) =>
      ref ! r.createScreenCapture(rectangle)
      Behaviors.same
  }


  sealed trait RobotMessage

  case class MoveMouse(x: Int, y: Int) extends RobotMessage
  case class TakeScreenshot(rectangle: Rectangle, ref: ActorRef[BufferedImage]) extends RobotMessage
}
