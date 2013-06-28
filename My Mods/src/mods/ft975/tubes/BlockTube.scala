package mods.ft975.tubes

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.world.World
import net.minecraft.tileentity.TileEntity
import mods.ft975.util.BlockTESR

class BlockTube(id: Int) extends BlockContainer(id, Material.glass) with BlockTESR {

	def createNewTileEntity(world: World): TileEntity = {
		null
	}
}
