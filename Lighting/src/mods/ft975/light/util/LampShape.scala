package mods.ft975.light.util

sealed class LampShape(metaV: Byte, nameV: String, unLocNameV: String) {
	val meta      = metaV
	val name      = nameV
	val unLocName = unLocNameV
}

object LampShape {
	val vals = List[LampShape](Caged, Block, Panel, Bulb)

	def fromID(id: Byte): LampShape = if (id >= 0 && id <= vals.length) vals(id) else Block


	case object Caged extends LampShape(0, "Caged Lamp", "caged")
	case object Block extends LampShape(1, "Lamp", "block")
	case object Panel extends LampShape(2, "Panel Lamp", "panel")
	case object Bulb extends LampShape(3, "Bulb", "bulb")

}