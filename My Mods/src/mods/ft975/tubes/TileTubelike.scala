package mods.ft975.tubes

import net.minecraft.tileentity.TileEntity
import mods.ft975.tubes.util.TubeItem
import scala.collection.parallel.mutable

trait TileTubelike extends TileEntity {
	val transItems: mutable.ParSet[TubeItem]
}
