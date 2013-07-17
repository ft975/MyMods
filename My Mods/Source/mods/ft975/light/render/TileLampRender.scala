package mods.ft975.light.render

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mods.ft975.light.TileLamp

@SideOnly(Side.CLIENT)
object TileLampRender extends TileEntitySpecialRenderer {

	def renderTileEntityAt(te: TileEntity, x: Double, y: Double, z: Double, tick: Float) {
		if (te == null) return
		val lamp: TileLamp = te match {
			case te: TileLamp => te
			case _ => throw new ClassCastException("base is not of type TileLamp")
		}
		if (lamp.shape != null && lamp.color != null && lamp.side != null) {
			GL11.glPushMatrix()
			GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat + 1.5F, z.toFloat + 0.5F)
			GL11.glScalef(1.0F, -1F, -1F)
			RenderUtil.getModel(lamp.shape).render(lamp.color, lamp.isOn, lamp.side)
			GL11.glPopMatrix()
		}
	}
}
