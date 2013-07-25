package mods.ft975.energy.tileentity

import mods.ft975.energy.api.{AbstractEnergyNetwork, PowerImporter}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.ForgeDirection

class TileTestPowerS extends TileEntity with PowerImporter {
	def getEnergyNet: AbstractEnergyNetwork = ???
	def isSideConnected(side: ForgeDirection): Boolean = ???
}
