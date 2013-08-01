package mods.ft975.energy.tileentity

import mods.ft975.energy.api.{AbstractEnergyNetwork, PowerImporter}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.ForgeDirection

class TileTestPowerR extends TileEntity with PowerImporter {
	def getEnergyNet: AbstractEnergyNetwork = ???
	def isSideConnected(side: ForgeDirection): Boolean = ???
	def clearEnergyNet() {}
	def setEnergyNet(enet: AbstractEnergyNetwork) {}
}
