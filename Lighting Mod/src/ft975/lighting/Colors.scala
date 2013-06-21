package ft975.lighting

import ft975.lighting.render.RenderUtil.Color


sealed class Colors(metadata: Byte, col: (Short, Short, Short), colorName: String) {
	val meta = metadata
	val color = Colors.getColor(col)
	println(color)
	val name = colorName
}

object Colors {
	val vals = List[Colors](Black, Red, Green, Brown, Blue, Purple, Cyan, LightGray, Gray, Pink, Lime, Yellow, LightBlue, Magenta, Orange, White)

	def fromID(id: Byte): Colors = vals(id)

	protected def getColor(c: (Short, Short, Short)): Color = {
		Color(c._1.toFloat / 255f, c._2.toFloat / 255f, c._3.toFloat / 255f)
	}


	case object Black extends Colors(0, (0x19, 0x19, 0x19), "Black")

	case object Red extends Colors(1, (0xCC, 0x4C, 0x4C), "Red")

	case object Green extends Colors(2, (0x66, 0x7F, 0x33), "Green")

	case object Brown extends Colors(3, (0x7F, 0x66, 0x4C), "Brown ")

	case object Blue extends Colors(4, (0x33, 0x66, 0xCC), "Blue")

	case object Purple extends Colors(5, (0xB2, 0x66, 0xE5), "Purple")

	case object Cyan extends Colors(6, (0x4C, 0x99, 0xB2), "Cyan")

	case object LightGray extends Colors(7, (0x99, 0x99, 0x99), "Light Gray")

	case object Gray extends Colors(8, (0x4C, 0x4C, 0x4C), "Gray")

	case object Pink extends Colors(9, (0xF2, 0xB2, 0xCC), "Pink")

	case object Lime extends Colors(10, (0x7F, 0xCC, 0x19), "Lime")

	case object Yellow extends Colors(11, (0xE5, 0xE5, 0x33), "Yellow")

	case object LightBlue extends Colors(12, (0x99, 0xB2, 0xF2), "Light Blue")

	case object Magenta extends Colors(13, (0xE5, 0x7F, 0xD8), "Magenta")

	case object Orange extends Colors(14, (0xF2, 0xB2, 0x33), "Orange")

	case object White extends Colors(15, (0xFF, 0xFF, 0xFF), "White")

}


