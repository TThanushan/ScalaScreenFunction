package io.github.ttharmabalan.screen_function

import java.awt.image.BufferedImage
import java.awt.{Rectangle, Robot}
import java.io.File

import javax.imageio.ImageIO
import java.awt.{Color => AwtColor}

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import io.github.ttharmabalan.screen_function.CaptureScreenshot.blueRect
import io.github.ttharmabalan.screen_function.MasterActor.SummonRobotActor
import io.github.ttharmabalan.screen_function.RobotActor.{RobotMessage, TakeScreenshot}

import scala.concurrent.Await
import scala.concurrent.duration._


object CaptureScreenshot extends App {

    type Color <: Int
    def wrapColor(i: Int): Color = i.asInstanceOf[Color]

    implicit class ColorFunctions(val c: Color) extends AnyVal {

        def getRed() = (c >> 16) & 0xFF

        def getGreen() = (c >> 8) & 0xFF

        def getBlue() = c & 0xFF


        def getHSV(outputArray: Array[Float] = Array.ofDim[Float](3)): Array[Float] = {
            require(outputArray.length == 3)
            AwtColor.RGBtoHSB(getRed(), getBlue(), getGreen(), outputArray)
        }

    }

    implicit class ExtendedBufferedImage(val bufferedImage: BufferedImage) extends AnyVal {
        def getColor(x: Int, y: Int): Color = wrapColor(bufferedImage.getRGB(x, y))

        def getColor(pixelNumber: Int): Color = wrapColor(bufferedImage.getColorModel.getRGB(pixelNumber))

        def filterPixels(predicate: Color => Boolean): Seq[Option[Color]] = {

            for(i <- 0 until bufferedImage.getHeight(); j <- 0 until bufferedImage.getWidth) yield {
                val color = getColor(j + i * bufferedImage.getWidth)

                Some(color).filter(predicate)
            }
        }
    }



    implicit class ExtendedRobot(val robot: Robot) extends AnyVal {
        def getColor(x: Int, y: Int): Color = wrapColor(robot.getPixelColor(x,y).getRGB)
    }

    class PixelFunctions
    {
        // Compare the color at the x and y position with the color given in parameter.
        def comparePixelColor(x: Int, y: Int, col: AwtColor, image: BufferedImage): Boolean = {
            val col2 = image.getColor(x, y)
            if (col == col2){
                true
            }
            else {
                false
            }
        }
//        def filterPixels(predicate: Color => Boolean): List[(Int,Int,Color)] = {
//            val pixel: Color = ???
//
//            predicate(pixel)
//        }

        def int2PixelPos(n: Int, width: Int = 1920) : (Int, Int) = {( n%width, n/width)}

        def pixelSearch(left: Int, top: Int, right: Int, bottom: Int, color: Color) = {
            val robot = new Robot()
            val rect = new Rectangle(left, top, right, bottom)
            val screenCapture: BufferedImage = robot.createScreenCapture(rect)
            val capturePixels: Array[Int] = screenCapture.getRGB(0, 0, right, bottom, null, 0, 0)
            //            var foundedPixels = List[Int, Int]

            for(p <- capturePixels) {
                if(wrapColor(p) == color){

                }
            }
        }
    }



    val iRobot = new Robot

    val blueRect = new Rectangle(0, 0, 1920, 1080)

    val screenshot : BufferedImage= iRobot.createScreenCapture(blueRect)
    //    ImageIO.write(screenshot, "jpg", new File("ScreenshotWithScala2.jpg"))


//    screenshot.filterPixels(c => c.getRed() == 0xFF && c.getBlue() == 0xFF && c.getGreen() == 0xFF)

    val allPixelCol: Array[Int] = screenshot.getRGB(0, 0, 1920, 1080, null,0, 1920)

    val screenCol = screenshot.getColor(0, 0)
    val r = screenCol.getRed()
    val g = screenCol.getGreen()
    val b = screenCol.getBlue()

    //    val t: Color = 5.asInstanceOf[Color]
    //
    val c2: Color = wrapColor(5)
    //
    //        val c3 = new AwtColor(allPixelCol(0))

    //    val hsvValues = Array.ofDim[Float](3)
    //
    //    c2.getHSV(hsvValues)
    //
    //    hsvValues(1)
    val pixelFunctions = new PixelFunctions
    val col = new AwtColor(0x0971e1)


    val actorSystem = ActorSystem.apply(MasterActor.init(), "ActorSystem")

    implicit val timeout: Timeout = 3.seconds
    implicit val scheduler = actorSystem.scheduler
    implicit val ec = actorSystem.executionContext

    val futureRobotActor = actorSystem ? ((ref: ActorRef[ActorRef[RobotMessage]]) => SummonRobotActor(ref))

    val robotActor = Await.result(futureRobotActor, 25.seconds)

    def blackhole[A](a: A) = ()
    val frameTimings = for(i <- 0 until 2000) yield {
        val blueRect = new Rectangle(0, 0, 1280, 720)
        if(i % 100 == 0) {
            println(s"analyzing frame $i")
        }

        val start = System.currentTimeMillis()
        val futureScreenshot = robotActor ? ((ref: ActorRef[BufferedImage]) => TakeScreenshot(blueRect, ref))

        val screenshot = Await.result(futureScreenshot, 25.seconds)
        blackhole(screenshot)
//        for (p <- 0 until screenshot.getWidth * screenshot.getHeight) {
//            val (x, y) = pixelFunctions.int2PixelPos(p, screenshot.getWidth)
//
//            if (pixelFunctions.comparePixelColor(x, y, col, screenshot)) {
//                println(s"pixel found at x : $x / y : $y col :")
//            }
//            else {
//                //            println(s"Not the same pixel ! x : $x / y : $y ")
//            }
//        }
        val end = System.currentTimeMillis()
        end - start
    }

    println(s"max frame time ${frameTimings.max}ms")
    println(s"min frame time ${frameTimings.min}ms")
    println(s"average frame time ${frameTimings.sum / frameTimings.size.toDouble}ms")
    actorSystem.terminate()
//    println(s"x: $x y: $y")
}