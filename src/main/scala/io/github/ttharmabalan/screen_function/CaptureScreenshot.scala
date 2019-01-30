package io.github.ttharmabalan.screen_function

import java.awt.image.BufferedImage

import java.awt.{Rectangle, Robot}
import java.io.File

import javax.imageio.ImageIO
import java.awt.{Color => AwtColor}

object CaptureScreenshot extends App {

    val robot = new Robot

    //    val rect = new Rectangle(Toolkit.getDefaultToolkit.getScreenSize)
    val rect = new Rectangle(0, 0, 1920, 1080)

    val screenshot : BufferedImage= robot.createScreenCapture(rect)
    ImageIO.write(screenshot, "jpg", new File("screenshot/ScreenshotWithScala2.jpg"))

    val allPixelCol: Array[Int] = screenshot.getRGB(0, 0, 50, 50, null,0, 50)
    val b = allPixelCol(0)&0xFF
    val g = (allPixelCol(0)>>8)&0xFF
    val r = (allPixelCol(0)>>16)&0xFF

    type Color <: Int
    def wrapColor(i: Int): Color = i.asInstanceOf[Color]

    val t: Color = 5.asInstanceOf[Color]

    val c2: Color = wrapColor(5)

    val c3 = new AwtColor(allPixelCol(0))


    implicit class ColorFunctions(val c: Color) extends AnyVal {
        def getRed() = (c >> 16) & 0xFF

        def getGreen() = (c >> 8) & 0xFF

        def getBlue() = c & 0xFF

        def getHSV(outputArray: Array[Float] = Array.ofDim[Float](3)): Array[Float] = {
            require(outputArray.length == 3)
            AwtColor.RGBtoHSB(getRed(), getBlue(), getGreen(), outputArray)
        }
    }

    val hsvValues = Array.ofDim[Float](3)

    c2.getHSV(hsvValues)

    hsvValues(1)

    implicit class ExtendedBufferedImage(val bufferedImage: BufferedImage) extends AnyVal {
        def getColor(x: Int, y: Int): Color = wrapColor(bufferedImage.getRGB(x, y))
    }

    val d = screenshot.getColor(5, 6)

    d.getRed()


    val e = screenshot.getColor(5,6)
//    val e = screenshot.getColor(5,6)


    println(s"R $r G $g B $b")
    println(allPixelCol(0) == 0xFF556068)
    println(d.getRed)
//
//    println(Integer.toHexString(allPixelCol(0)))
//    println(Integer.toBinaryString(allPixelCol(0)))
//    println((0xFF556068 >> 8) == 0xFFFF5560)
//    println((0xFF556068 >> 16) == 0xFFFFFF55)
//    println((0xFF556068 & 0xFF) == 0x00000068)

//    println("A full screenshot saved!")


}
