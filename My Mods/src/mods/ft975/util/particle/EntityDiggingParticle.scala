package mods.ft975.util.particle

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.world.World

@SideOnly(Side.CLIENT)
class EntityDiggingParticle(wrd: World, posX: Double, posY: Double, posZ: Double, motX: Double, motY: Double, motZ: Double, block: Block,
                            _side: Int, x: Int, y: Int, z: Int) extends EntityFX(wrd, posX, posY, posZ, motX, motY, motZ) {

	setParticleIcon(Minecraft.getMinecraft.renderEngine, block.getBlockTexture(wrd, x, y, z, _side))
	particleGravity = block.blockParticleGravity
	particleRed = 0.6F
	particleGreen = 0.6F
	particleBlue = 0.6F
	particleScale /= 2.0F

	override def getFXLayer: Int = 1

	override def renderParticle(tess: Tessellator, interpC: Float, xVal: Float, yVal: Float, zVal: Float, uMod: Float, vMod: Float) {
		var u1: Float = (particleTextureIndexX + particleTextureJitterX / 4.0F) / 16.0F
		var u2: Float = u1 + 0.015609375F
		var v1: Float = (particleTextureIndexY + particleTextureJitterY / 4.0F) / 16.0F
		var v2: Float = v1 + 0.015609375F
		val scale: Float = 0.1F * particleScale

		if (particleIcon != null) {
			u1 = particleIcon.getInterpolatedU(particleTextureJitterX / 4.0F * 16.0F)
			u2 = particleIcon.getInterpolatedU((particleTextureJitterX + 1.0F) / 4.0F * 16.0F)
			v1 = particleIcon.getInterpolatedV(particleTextureJitterY / 4.0F * 16.0F)
			v2 = particleIcon.getInterpolatedV((particleTextureJitterY + 1.0F) / 4.0F * 16.0F)
		}

		val vX: Double = prevPosX + (posX - prevPosX) * interpC - EntityFX.interpPosX
		val vY: Double = prevPosY + (posY - prevPosY) * interpC - EntityFX.interpPosY
		val vZ: Double = prevPosZ + (posZ - prevPosZ) * interpC - EntityFX.interpPosZ
		val colorModifier: Float = 1.0F

		tess.setColorOpaque_F(colorModifier * particleRed, colorModifier * particleGreen, colorModifier * particleBlue)

		tess.addVertexWithUV(vX - xVal * scale - uMod * scale, vY - yVal * scale, vZ - zVal * scale - vMod * scale, u1, v2)
		tess.addVertexWithUV(vX - xVal * scale + uMod * scale, vY + yVal * scale, vZ - zVal * scale + vMod * scale, u1, v1)
		tess.addVertexWithUV(vX + xVal * scale + uMod * scale, vY + yVal * scale, vZ + zVal * scale + vMod * scale, u2, v1)
		tess.addVertexWithUV(vX + xVal * scale - uMod * scale, vY - yVal * scale, vZ + zVal * scale - vMod * scale, u2, v2)
	}
}

