package mods.ft975.energy.block

import mods.ft975.energy.tileentity.{TileTestPowerS, TileTestPowerR}
import net.minecraft.block.material.Material
import net.minecraft.block.{ITileEntityProvider, Block}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockTestPower(id: Int, receiver: Boolean) extends Block(id, Material.iron) with ITileEntityProvider {
	def createNewTileEntity(world: World): TileEntity = if (receiver) new TileTestPowerR else new TileTestPowerS
}
