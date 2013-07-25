package mods.ft975.energy.api

import net.minecraft.world.World

abstract class AbstractEnergyNetwork(val world: World) {
	def refreshGraph(pm: PowerMember)

	def getBestExporter(x: Int, y: Int, z: Int): Set[PowerExporter]

	def getBestExporter(member: PowerMember): Set[PowerExporter] = {
		if (member.getWorldObj == world)
			getBestExporter(member.xCoord, member.yCoord, member.zCoord)
		else {
			new RuntimeException(s"World ${member.getWorldObj} does not correspond to EnergyNetwork world $world").printStackTrace()
			null
		}
	}

}