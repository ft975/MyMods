package mods.ft975.lighting

sealed class Shapes(metaV: Byte, nameV: String, unLocNameV: String) {
	val meta = metaV
	val name = nameV
	val unLocName = unLocNameV
}

object Shapes {
	val vals = List[Shapes](Caged, Block, Panel, Bulb)

	def fromID(id: Byte): Shapes = vals(id)


	case object Caged extends Shapes(0, "Caged Lamp", "caged")

	case object Block extends Shapes(1, "Lamp", "block")

	case object Panel extends Shapes(2, "Panel Lamp", "panel")

	case object Bulb extends Shapes(3, "Bulb", "bulb")

}