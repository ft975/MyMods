package mods.ft975.light

import mods.ft975.light.render.RenderUtil.Color

sealed class Colors(metadata: Byte, col: (Short, Short, Short), colorName: String) {
	val meta = metadata
	val color: Color = Colors.getColor(col, Colors.darkenFactor)
	val name = colorName
	DebugOnly {
		logInfo("(%d, %s @ %s".format(meta, name, color))
	}
}

object Colors {
	lazy val vals = List[Colors](Black, Red, Green, Brown, Blue, Purple, Cyan, LightGray, Gray, Pink, Lime, Yellow, LightBlue, Magenta, Orange, White)
	val colArray = Array("191919", "993333", "667F33", "664C33", "334CB2", "7F3FB2", "4C7F99", "999999", "4C4C4C", "F27FA5", "7FCC19", "E5E533", "6699D8", "B24CD8", "D87F33", "FFFFFF")
	val colNameArray = Array("Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White")
	var darkenFactor = .4F

	private def getCol(str: String): (Short, Short, Short) = {
		val cols = str.grouped(2).toList
		try {
			(Integer.parseInt(cols(0), 16).toShort, Integer.parseInt(cols(1), 16).toShort, Integer.parseInt(cols(2), 16).toShort)
		} catch {
			case _: Throwable =>
				throw new Exception("Config file is corrupted, please delete it or delete the section with the colors")
		}
	}

	def fromID(id: Byte): Colors = if (id >= 0 && id <= vals.length) vals(id) else Black

	protected def getColor(c: (Short, Short, Short)): Color = {
		Color(c._1.toFloat / 255f, c._2.toFloat / 255f, c._3.toFloat / 255f)
	}

	protected def getColor(col: (Short, Short, Short), p: Float): Color = {
		val c = getColor(col)
		val r = c.R * p
		val g = c.G * p
		val b = c.B * p
		Color(c.R - r, c.G - g, c.B - b)
	}


	case object Black extends Colors(0, getCol(colArray(0)), "Black")

	case object Red extends Colors(1, getCol(colArray(1)), "Red")

	case object Green extends Colors(2, getCol(colArray(2)), "Green")

	case object Brown extends Colors(3, getCol(colArray(3)), "Brown ")

	case object Blue extends Colors(4, getCol(colArray(4)), "Blue")

	case object Purple extends Colors(5, getCol(colArray(5)), "Purple")

	case object Cyan extends Colors(6, getCol(colArray(6)), "Cyan")

	case object LightGray extends Colors(7, getCol(colArray(7)), "Light Gray")

	case object Gray extends Colors(8, getCol(colArray(8)), "Gray")

	case object Pink extends Colors(9, getCol(colArray(9)), "Pink")

	case object Lime extends Colors(10, getCol(colArray(10)), "Lime")

	case object Yellow extends Colors(11, getCol(colArray(11)), "Yellow")

	case object LightBlue extends Colors(12, getCol(colArray(12)), "Light Blue")

	case object Magenta extends Colors(13, getCol(colArray(13)), "Magenta")

	case object Orange extends Colors(14, getCol(colArray(14)), "Orange")

	case object White extends Colors(15, getCol(colArray(15)), "White")

}


