package mods.ft975.tubes

import net.minecraft.tileentity.TileEntity
import mods.ft975.tubes.util.TubeItem

trait TileTubelike extends TileEntity {
	val transItems: Set[TubeItem]
}
