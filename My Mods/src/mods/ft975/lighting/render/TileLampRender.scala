package mods.ft975.lighting.render

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11
import mods.ft975.lighting.TileLamp
import cpw.mods.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
class TileLampRender extends TileEntitySpecialRenderer {

	def renderTileEntityAt(te: TileEntity, x: Double, y: Double, z: Double, tick: Float) {
		val lamp: TileLamp = te match {
			case te: TileLamp => te
			case _ => throw new ClassCastException("te is not of type TileLamp")
		}
		if (lamp.shape != null) {
			GL11.glPushMatrix()
			GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat + 1.5F, z.toFloat + 0.5F)
			GL11.glScalef(1.0F, -1F, -1F)
			RenderUtil.getModel(lamp.shape).render(lamp.color.color, isOn = true)
			GL11.glPopMatrix()
		}

	}

}
