package io.github.ttharmabalan.screen_function

import java.awt.image.BufferedImage

import java.awt.{Rectangle, Robot}
import java.io.File

import javax.imageio.ImageIO
import java.awt.{Color => AwtColor}

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
    }

    class PixelFunctions
    {
        def int2PixelPos(n: Int, width: Int = 1920) : (Int, Int) = {( n%width, n/width)}

        def pixelSearch(left: Int, top: Int, right: Int, bottom: Int, color: Color) = {
            val robot = new Robot()
            val rect = new Rectangle(left, top, right, bottom)
            val screenCapture: BufferedImage = robot.createScreenCapture(rect)
            val capturePixels: Array[Int] = screenCapture.getRGB(0, 0, right, bottom, null, 0, 0)
            //            var foundedPixels = List[Int, Int]

            for(p <- capturePixels) {
                if(wrapColor(p) == color){
                    //                    foundedPixels(0, 0) = wrapColor(
                }
            }
        }
    }



    val robot = new Robot

    val rect = new Rectangle(0, 0, 1920, 1080)

    val screenshot : BufferedImage= robot.createScreenCapture(rect)
    //    ImageIO.write(screenshot, "jpg", new File("ScreenshotWithScala2.jpg"))


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


    val d = screenshot.getColor(5, 6)
    val col = robot.getPixelColor(333, 80)

    //    val e = screenshot.getColor(5,6)
    //    val e = screenshot.getColor(5,6)


    //  println(s"R $r G $g B $b")
    //    println(allPixelCol(0))
    //  println(c2)

    //  println(allPixelCol.size)

    val (x,y) = new PixelFunctions().int2PixelPos(1920)
    println(s"x: $x y: $y")

}