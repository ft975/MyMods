package mods.ft975.light.render

import mods.ft975.util.render.particle.EntityDiggingParticle
import mods.ft975.util.render.values.Color
import net.minecraft.util.Icon
import net.minecraft.world.World

class EntityLampDiggingParticle(
																 wrd: World,
																 pos1X: Double,
																 pos1Y: Double,
																 pos1Z: Double,
																 motX: Double,
																 motY: Double,
																 motZ: Double,
																 icon: Icon,
																 col: Color)
	extends EntityDiggingParticle(
		wrd: World,
		pos1X: Double,
		pos1Y: Double,
		pos1Z: Double,
		motX: Double,
		motY: Double,
		motZ: Double,
		icon: Icon) {
	color = col
}
