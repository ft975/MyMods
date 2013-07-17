package mods.ft975.util
package particle

import net.minecraft.world.World
import net.minecraft.util.Icon

class EntityLampDiggingParticle(
																 wrd: World,
																 pos1X: Double,
																 pos1Y: Double,
																 pos1Z: Double,
																 motX: Double,
																 motY: Double,
																 motZ: Double,
																 icon: Icon,
																 red: Float,
																 green: Float,
																 blue: Float)
	extends EntityDiggingParticle(
		wrd: World,
		pos1X: Double,
		pos1Y: Double,
		pos1Z: Double,
		motX: Double,
		motY: Double,
		motZ: Double,
		icon: Icon) {
	r = (red * 255).toInt
	b = (blue * 255).toInt
	g = (green * 255).toInt
}
