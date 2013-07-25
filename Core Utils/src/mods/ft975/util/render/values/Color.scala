package mods.ft975.util.render.values

import java.awt
import java.util.InputMismatchException
import javafx.scene.paint
import org.lwjgl.util
import scala.xml.Node

@inline class Color(val rgba: Int) extends AnyVal {
	def r: Int = rgba >> 24 & 0xFF
	def g: Int = rgba >> 16 & 0xFF
	def b: Int = rgba >> 8 & 0xFF
	def a: Int = rgba >> 0 & 0xFF
	def rgb: Int = rgba >> 0 & 0x00FFFFFF

	def rD: Double = r / 255D
	def gD: Double = g / 255D
	def bD: Double = b / 255D
	def aD: Double = a / 255D

	def r_=(v: Int): Color = Color(v, g, b, a)
	def g_=(v: Int): Color = Color(r, v, b, a)
	def b_=(v: Int): Color = Color(r, g, v, a)
	def a_=(v: Int): Color = Color(r, g, b, v)

	def r_=(v: Double): Color = { r = (v * 255).toInt }
	def g_=(v: Double): Color = { g = (v * 255).toInt }
	def b_=(v: Double): Color = { b = (v * 255).toInt }
	def a_=(v: Double): Color = { a = (v * 255).toInt }

	def *(v: Int): Color = Color(r * v, g * v, b * v, a)
	def *(v: Double): Color = Color(rD * v, gD * v, bD * v, a)
	def |*|(v: Color): Color = Color(r * v.r, g * v.g, b * v.b, a)

	def +(v: Int): Color = Color(r + v, g + v, b + v, a)
	def +(v: Double): Color = Color(rD + v, gD + v, bD + v, a)
	def |+|(v: Color): Color = Color(r + v.r, g + v.g, b + v.b, a)

	def -(v: Int): Color = Color(r - v, g - v, b - v, a)
	def -(v: Double): Color = Color(rD - v, gD - v, bD - v, a)
	def |-|(v: Color): Color = Color(r - v.r, g - v.g, b - v.b, a)

	def /(v: Int): Color = Color(r / v, g / v, b / v, a)
	def /(v: Double): Color = Color(rD / v, gD / v, bD / v, a)
	def |/|(v: Color): Color = Color(r / v.r, g / v.g, b / v.b, a)

	def toXML: Node = <col>{rgba}</col>
}

object Color {
	final val White     = Color(255, 255, 255)
	final val LightGray = Color(192, 192, 192)
	final val Gray      = Color(128, 128, 128)
	final val DarkGray  = Color(64, 64, 64)
	final val Black     = Color(0, 0, 0)
	final val Red       = Color(255, 0, 0)
	final val Pink      = Color(255, 175, 175)
	final val Orange    = Color(255, 200, 0)
	final val Yellow    = Color(255, 255, 0)
	final val Green     = Color(0, 255, 0)
	final val Magenta   = Color(255, 0, 255)
	final val Cyan      = Color(0, 255, 255)
	final val Blue      = Color(0, 0, 255)

	def apply(r: Int, g: Int, b: Int, a: Int): Color = new Color((ready(r) << 24) | (ready(g) << 16) | (ready(b) << 8) | ready(a))
	def apply(r: Int, g: Int, b: Int): Color = Color(r, g, b, 0xFF)
	def apply(r: Double, g: Double, b: Double, a: Double): Color = Color(ready(r), ready(g), ready(b), ready(a))
	def apply(r: Double, g: Double, b: Double): Color = Color(r, g, b, 1)
	def apply(rgb: Int): Color = Color(rgb, 0xFF)
	def apply(rgb: Int, a: Int): Color = new Color(((rgb & 0xFFFFFF) << 8) | ready(a))
	def apply(rgb: Int, a: Double): Color = new Color(((rgb & 0xFFFFFF) << 8) | ready(a))
	def apply(v: awt.Color): Color = new Color(((v.getRGB & 0xFFFFFF) << 8) | 0xFF)
	def apply(v: awt.Color, a: Int): Color = Color(v.getRGB, a)
	def apply(v: awt.Color, a: Double): Color = Color(v.getRGB, a)
	def apply(v: util.Color): Color = Color(v.getRed, v.getGreen, v.getBlue)
	def apply(v: util.Color, a: Int): Color = Color(v.getRed, v.getGreen, v.getBlue, a)
	def apply(v: util.Color, a: Double): Color = Color(v.getRed, v.getGreen, v.getBlue, ready(a))
	def apply(v: paint.Color): Color = Color(v.getRed, v.getGreen, v.getBlue)
	def apply(v: paint.Color, a: Int): Color = Color(ready(v.getRed), ready(v.getGreen), ready(v.getBlue), a)
	def apply(v: paint.Color, a: Double): Color = Color(v.getRed, v.getGreen, v.getBlue, ready(a))
	def apply(rgb: String): Color = Color(rgb, 0xFF)
	def apply(rgb: String, a: Int): Color = Color(Integer.parseInt(rgb, 16), a)
	def apply(rgb: String, a: Float): Color = Color(rgb, ready(a))

	def fromXML(xml: Node): Color = {
		xml match {
			case <col>{v}</col> => new Color(v.text.toInt)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}

	private def ready(v: Int): Int = v & 0xFF
	private def ready(v: Double): Int = (v * 0xFF).toInt & 0xFF
}