package mods.ft975.util
package particle

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.world.World
import net.minecraft.util.Icon

@SideOnly(Side.CLIENT)
class EntityDiggingParticle(wrd: World, pos1X: Double, pos1Y: Double, pos1Z: Double, motX: Double, motY: Double, motZ: Double, icon: Icon
														 ) extends EntityFX(wrd, pos1X, pos1Y, pos1Z, motX, motY, motZ) {

	var r, g, b = 153

	particleIcon = icon
	particleGravity = 1
	particleScale /= 2.0F

	def this(wrd: World, pos1X: Double, pos1Y: Double, pos1Z: Double, motX: Double, motY: Double,
					 motZ: Double, block: Block, side: Int, x: Int, y: Int, z: Int) {
		this(wrd, pos1X, pos1Y, pos1Z, motX, motY, motZ, block.getBlockTexture(wrd, x, y, z, side))
	}

	override val getFXLayer = 1

	override def renderParticle(tess: Tessellator, deltaV: Float, xVal: Float, yVal: Float, zVal: Float, uMod: Float, vMod: Float) {
		var u1: Float = (particleTextureIndexX + particleTextureJitterX / 4.0F) / 16.0F
		var u2: Float = u1 + 0.015609375F
		var v1: Float = (particleTextureIndexY + particleTextureJitterY / 4.0F) / 16.0F
		var v2: Float = v1 + 0.015609375F
		val scale: Float = 0.1F * particleScale

		if (particleIcon != null) {
			u1 = particleIcon.getInterpolatedU(particleTextureJitterX * 4)
			u2 = particleIcon.getInterpolatedU((particleTextureJitterX + 1) * 4)
			v1 = particleIcon.getInterpolatedV(particleTextureJitterY * 4)
			v2 = particleIcon.getInterpolatedV((particleTextureJitterY + 1) * 4)
		}

		val vX = prevPosX + (posX - prevPosX) * deltaV - EntityFX.interpPosX
		val vY = prevPosY + (posY - prevPosY) * deltaV - EntityFX.interpPosY
		val vZ = prevPosZ + (posZ - prevPosZ) * deltaV - EntityFX.interpPosZ

		tess.setColorRGBA(r, g, b, 255)
		tess.addVertexWithUV(vX - xVal * scale - uMod * scale, vY - yVal * scale, vZ - zVal * scale - vMod * scale, u1, v2)
		tess.addVertexWithUV(vX - xVal * scale + uMod * scale, vY + yVal * scale, vZ - zVal * scale + vMod * scale, u1, v1)
		tess.addVertexWithUV(vX + xVal * scale + uMod * scale, vY + yVal * scale, vZ + zVal * scale + vMod * scale, u2, v1)
		tess.addVertexWithUV(vX + xVal * scale - uMod * scale, vY - yVal * scale, vZ + zVal * scale - vMod * scale, u2, v2)
	}
}

