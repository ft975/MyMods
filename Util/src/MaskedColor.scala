
import com.intellij.uiDesigner.core.GridConstraints._
import com.intellij.uiDesigner.core.{Spacer, GridConstraints, GridLayoutManager}
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.image.BufferedImage
import java.awt.{Dimension, Insets}
import java.io.File
import javax.imageio.ImageIO
import javax.swing._

object MaskedColor {
  def maskImage(in: BufferedImage, mask: BufferedImage, r: Int, g: Int, b: Int): BufferedImage = {
    for (x <- 0 until in.getWidth;
         y <- 0 until in.getHeight) {
      if (mask.getRGB(x, y).avg < 250) {
        val col: Color = applyColor(new Color(r, g, b), in.getRGB(x, y))
        in.setRGB(x, y, col)
      }
    }
    in
  }

  def applyColor(ne: Color, old: Color): Color = {
    (ne + old.avg) / 2
  }

  implicit def int2Color(rgb: Int): Color = new Color(rgb)

  implicit def color2Int(rgb: Color): Int = {
    if (rgb.a > 255) 255 else rgb.a
    if (rgb.r > 255) 255 else rgb.r
    if (rgb.g > 255) 255 else rgb.g
    if (rgb.b > 255) 255 else rgb.b
    var v: Int = if (rgb.a > 255) 255 else rgb.a
    v = (v << 8) + (if (rgb.r > 255) 255 else rgb.r)
    v = (v << 8) + (if (rgb.g > 255) 255 else rgb.g)
    v = (v << 8) + (if (rgb.b > 255) 255 else rgb.b)
    v
  }
}

object ColorizeMask {
  val maskPrefix = "mask_"
  val fileType = ".png"
  val fileList = Array("block", "bulb", "caged", "panel", "part_block", "part_bulb", "part_caged")
  val colorList = Array("Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White")

  def main(args: Array[String]) {
    val frame: JFrame = new JFrame("MaskColorize")
    frame.setContentPane(Form.contentPane)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    frame.pack()
    frame.setVisible(true)
  }

  def exec(col: Array[Color], input: String, output: String) {
    for (name <- fileList;
         v <- 0 until colorList.length) {
      val mask = new File(input + maskPrefix + name + fileType)
      val image = new File(input + name + fileType)
      val out = new File(output + colorList(v) + "_" + name + fileType)
      try {
        ImageIO.write(
          MaskedColor.maskImage(
            ImageIO.read(image),
            ImageIO.read(mask),
            col(v).r, col(v).g, col(v).b),
          "png", out)
      } catch {
        case _: Throwable => {
          println(mask)
          println(image)
          println(out)
        }
      }
    }
  }
}

object Form {
  implicit def toActionListener(f: ActionEvent => Unit) = new ActionListener {
    def actionPerformed(e: ActionEvent) {
      f(e)
    }
  }

  val contentPane = new JPanel
  val run = new JButton
  val scrollPane1 = new JScrollPane
  val colorList = new JEditorPane
  val inputFolder = new JTextField
  val outputFolder = new JTextField
  private val label1 = new JLabel
  private val label2 = new JLabel
  private val spacer1 = new Spacer

  contentPane.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1))
  run.setText("Execute")
  contentPane.add(run, new GridConstraints(5, 0, 1, 1, ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED, null, null, null, 0, false))
  contentPane.add(scrollPane1, new GridConstraints(0, 1, 6, 1, ANCHOR_CENTER, FILL_BOTH, SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_WANT_GROW, SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_WANT_GROW, null, null, null, 0, false))
  scrollPane1.setViewportView(colorList)
  contentPane.add(inputFolder, new GridConstraints(1, 0, 1, 1, ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_WANT_GROW, SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false))
  contentPane.add(outputFolder, new GridConstraints(3, 0, 1, 1, ANCHOR_WEST, FILL_HORIZONTAL, SIZEPOLICY_WANT_GROW, SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false))
  label1.setText("Output folder:")
  contentPane.add(label1, new GridConstraints(2, 0, 1, 1, ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false))
  label2.setText("Input folder:")
  contentPane.add(label2, new GridConstraints(0, 0, 1, 1, ANCHOR_WEST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false))
  contentPane.add(spacer1, new GridConstraints(4, 0, 1, 1, ANCHOR_CENTER, FILL_VERTICAL, 1, SIZEPOLICY_WANT_GROW, null, null, null, 0, false))

  colorList.setText("191919\n993333\n667F33\n664C33\n334CB2\n7F3FB2\n4C7F99\n999999\n4C4C4C\nF27FA5\n7FCC19\nE5E533\n6699D8\nB24CD8\nD87F33\nFFFFFF")
  run.addActionListener((e: ActionEvent) => {
    ColorizeMask.exec(colorList.getText.split("\n").map(new Color(_)), inputFolder.getText, outputFolder.getText)
  })
}

class Color(val r: Int, val g: Int, val b: Int, val a: Int) {

  def this(r: Int, g: Int, b: Int) {
    this(r, g, b, 255)
  }

  def this(v: Int) {
    this(v & 0xFF, (v >> 8) & 0xFF, (v >> 16) & 0xFF, (v >> 24) & 0xFF)
  }

  private def this(cols: List[String]) {
    this(Integer.parseInt(cols(0), 16), Integer.parseInt(cols(1), 16), Integer.parseInt(cols(2), 16))
  }

  def this(v: String) {
    this(v.grouped(2).toList)
  }


  def +(o: Color) = {
    new Color(o.r + r, o.g + g, o.b + b)
  }

  def +(v: Int) = {
    new Color(r + v, g + v, b + v)
  }

  def -(v: Color) = {
    new Color(r - v.r, g - v.g, b - v.b)
  }

  def *(v: Color) = {
    new Color(v.r * r, v.g * g, v.b * b)
  }

  def *(v: Float) = {
    new Color((r * v).toInt, (g * v).toInt, (b * v).toInt)
  }

  def /(o: Int) = {
    new Color(r / o, g / o, b / o)
  }


  def sum: Int = {
    val o = this + Color.correction
    o.r + o.g + o.b
  }

  def avg: Int = {
    sum / 3
  }
}

object Color {
  private val correction = new Color(
    (0.2126f * 255).toInt,
    (0.7152f * 255).toInt,
    (0.0722f * 255).toInt
  )
}