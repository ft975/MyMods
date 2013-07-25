package mods.ft975.cloud

import net.minecraft.world.World

object Util {

	def getHeight(wrd: World, x: Int, z: Int, area: Int) = {
		var v: Int = 0
		for (x <- x - area to x + area;
				 z <- z - area to z + area) {
			v = math.max(v, wrd.getHeightValue(x, z))
		}
		v
	}

}
