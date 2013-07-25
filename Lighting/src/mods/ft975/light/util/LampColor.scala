package mods.ft975.light.util

import mods.ft975.light._
import mods.ft975.util.render.values.Color

sealed class LampColor(val meta: Byte) {
	val color = Color(LampColor.colArray(meta)) * (LampColor.darkenFactor + 1)
	val name  = LampColor.colNameArray(meta)
	DebugOnly {logInfo(s"($meta, $name @ $color")}
}

object LampColor {
	lazy val vals = List[LampColor](Black, Red, Green, Brown, Blue, Purple, Cyan, LightGray, Gray, Pink, Lime, Yellow, LightBlue, Magenta, Orange, White)

	val colArray     = Array("191919", "993333", "667F33", "664C33", "334CB2", "7F3FB2", "4C7F99", "999999", "4C4C4C", "F27FA5", "7FCC19", "E5E533", "6699D8", "B24CD8", "D87F33", "FFFFFF")
	val colNameArray = Array("Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White")
	var darkenFactor = .4F

	def fromID(id: Byte): LampColor = if (id >= 0 && id <= vals.length) vals(id) else Black

	case object Black extends LampColor(0)
	case object Red extends LampColor(1)
	case object Green extends LampColor(2)
	case object Brown extends LampColor(3)
	case object Blue extends LampColor(4)
	case object Purple extends LampColor(5)
	case object Cyan extends LampColor(6)
	case object LightGray extends LampColor(7)
	case object Gray extends LampColor(8)
	case object Pink extends LampColor(9)
	case object Lime extends LampColor(10)
	case object Yellow extends LampColor(11)
	case object LightBlue extends LampColor(12)
	case object Magenta extends LampColor(13)
	case object Orange extends LampColor(14)
	case object White extends LampColor(15)
}