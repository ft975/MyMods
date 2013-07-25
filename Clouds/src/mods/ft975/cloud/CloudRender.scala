package mods.ft975.cloud

import RenderType._
import mods.ft975.cloud.CloudState.Cloud
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11._

object CloudRender extends IRenderHandler {
	val mc = Minecraft.getMinecraft
	def render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
		setupEnv()
		getRenderType match {
			case RenderType.FAST => renderEachCloud(renderCloudFast)
			case RenderType.FANCY => renderEachCloud(renderCloudFancy)
			case RenderType.NONE =>
		}
		breakdownEnv()
	}

	def renderEachCloud(renderOp: (Cloud) => Unit) {
		CloudState.op((cld) => {
			glPushMatrix()
			renderOp(cld)
			glPopMatrix()
		})
	}

	def renderCloudFancy(cld: Cloud) {

	}

	def renderCloudFast(cld: Cloud) {

	}

	def setupEnv() {
		glDisable(GL_CULL_FACE)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
	}

	def breakdownEnv() {
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		glDisable(GL_BLEND)
		glEnable(GL_CULL_FACE)
	}

	def getRenderType: RenderType = {
		if (!mc.gameSettings.clouds || !mc.theWorld.provider.isSurfaceWorld) {
			NONE
		} else if (mc.gameSettings.fancyGraphics) {
			FANCY
		} else {
			FAST
		}
	}
}
