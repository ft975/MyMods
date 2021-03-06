package mods.ft975.light.render

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mods.ft975.light.util.LampShape.{Bulb, Panel, Block, Caged}
import mods.ft975.light.util.{LampColor, LampShape}
import net.minecraft.client.model.ModelRenderer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.ForgeDirection
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object RenderUtil {

	def renderInOut(mod: ModelRenderer, size: Float) {
		glPushMatrix()
		glDisable(GL_CULL_FACE)
		mod.render(size)
		glEnable(GL_CULL_FACE)
		glPopMatrix()
	}

	// Thanks to kimixa from reddit for patiently explaining the reasons behind my bugs and how to fix them
	def renderBulbRays(bulb: ModelRenderer, rays: ModelRenderer, size: Float, renderRays: Boolean, col: LampColor, isItem: Boolean, colTex: ResourceLocation) {
		//Start Context
		glPushMatrix()

		//Render the bulb
		if (MinecraftForgeClient.getRenderPass == 0 || isItem) {
			bulb.render(size)
			glColor4d(col.color.rD, col.color.gD, col.color.bD, 1)
			FMLClientHandler.instance.getClient.renderEngine.func_110577_a(colTex)
			bulb.render(size)
		}

		//Render the light rays
		if (renderRays && (MinecraftForgeClient.getRenderPass == 1 || isItem)) {
			// Setup light rays
			glDepthMask(false)
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE)

			// Setup texture and color
			glDisable(GL_TEXTURE_2D)
			glColor4d(col.color.rD, col.color.gD, col.color.bD, .5f)
			glScalef(1.0001F, 1.0001F, 1.0001F)
			rays.render(size)

			// Reset Pt 1
			glDepthMask(true)
			glDisable(GL_BLEND)
			glBlendFunc(GL_ONE_MINUS_SRC_ALPHA, GL_ONE)
			glEnable(GL_TEXTURE_2D)
		}

		//Reset Context
		glPopMatrix()
	}

	def rotateRender(model: ModelLamp, scale: Float, col: LampColor, isOn: Boolean, side: ForgeDirection) {
		glPushMatrix()
		side match {
			case ForgeDirection.DOWN =>
			case ForgeDirection.UP => {
				glRotatef(180, 1, 0, 0)
				glTranslatef(0, -2, 0)
			}
			case ForgeDirection.NORTH => {
				glRotatef(90, 1, 0, 0)
				glTranslatef(0, -1, -1)
			}
			case ForgeDirection.SOUTH => {
				glRotatef(270, 1, 0, 0)
				glTranslatef(0, -1, 1)
			}
			case ForgeDirection.WEST => {
				glRotatef(90, 0, 0, 1)
				glTranslatef(1, -1, 0)
			}
			case ForgeDirection.EAST => {
				glRotatef(270, 0, 0, 1)
				glTranslatef(-1, -1, 0)
			}
			case ForgeDirection.UNKNOWN =>
			case default =>
		}
		model.render(col, isOn, isItem = false)
		glPopMatrix()
	}

	def getModel(shape: LampShape): ModelLamp = {
		if (shape == null) ModelCagedLamp
		else {
			shape match {
				case Caged => ModelCagedLamp
				case Block => ModelBlockLamp
				case Panel => ModelPanelLamp
				case Bulb => ModelBulbLamp
				case _ => {
					new Exception("Model Match failed").printStackTrace()
					ModelBlockLamp
				}
			}
		}
	}
}