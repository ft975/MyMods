import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object MakeScrolling {
	val intArray = new Array[Int](1)

	// Usage MakeScrolling InputFile OutputFile
	def main(args: Array[String]) {
		val dskImg: BufferedImage = ImageIO.read(new File(args(0)))
		val newImg: BufferedImage = new BufferedImage(dskImg.getWidth, dskImg.getHeight * 16, BufferedImage.TYPE_INT_RGB)
		newImg.createGraphics()
		for (offset <- 0 to 15;
		     y <- 0 to 15;
		     x <- 0 to 15) {
			newImg.setRGB(x, y + offset * 15, dskImg.getRGB(x, y))
		}
		ImageIO.write(newImg, "png", new File(args(1)))
	}
}
