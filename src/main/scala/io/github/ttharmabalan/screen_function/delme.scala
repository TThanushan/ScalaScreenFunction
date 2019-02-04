package io.github.ttharmabalan.screen_function

import java.awt.{Rectangle, Robot}
import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO

class delme {
  val robot = new Robot

  val rect = new Rectangle(0, 0, 1920, 1080)

  val screenshot : BufferedImage= robot.createScreenCapture(rect)
  ImageIO.write(screenshot, "jpg", new File("ScreenshotWithScala2.jpg"))


  val allPixelCol: Array[Int] = screenshot.getRGB(0, 0, 50, 50, null,0, 50)

  println(allPixelCol(0))
}
