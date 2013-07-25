package mods.ft975.energy.api

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.ForgeDirection

trait PowerMember extends TileEntity {
	def getEnergyNet: AbstractEnergyNetwork

	def isSideConnected(side: ForgeDirection): Boolean

	def clearEnergyNet()

	def setEnergyNet(enet: AbstractEnergyNetwork)
}
