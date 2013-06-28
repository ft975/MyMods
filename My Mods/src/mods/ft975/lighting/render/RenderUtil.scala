package mods.ft975.lighting.render

import net.minecraft.client.model.ModelRenderer
import org.lwjgl.opengl.GL11
import net.minecraftforge.common.ForgeDirection
import mods.ft975.lighting.Shapes
import Shapes.{Bulb, Panel, Block, Caged}
import mods.ft975.lighting.Shapes
import scala.util.Random
import net.minecraftforge.client.MinecraftForgeClient
import cpw.mods.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
object RenderUtil {
	val rand = new Random()
	var renderPass = 0

	def renderInOut(mod: ModelRenderer, size: Float) {
		GL11.glPushMatrix()
		GL11.glFrontFace(GL11.GL_CW)
		mod.render(size)
		GL11.glFrontFace(GL11.GL_CCW)
		mod.render(size)
		GL11.glPopMatrix()
	}

	// Thanks to kimixa from reddit for patiently explaining the reasons behind my bugs and how to fix them
	def renderBulbRays(bulb: ModelRenderer, rays: ModelRenderer, size: Float, isOn: Boolean, col: Color) {
		//Start Context
		GL11.glPushMatrix()

		//Render the bulb
		if (MinecraftForgeClient.getRenderPass == 0) {
			GL11.glColor3f(col.R, col.G, col.B)
			bulb.render(size)
		}

		//Render the light rays
		if (isOn && MinecraftForgeClient.getRenderPass == 1) {
			// Setup light rays
			GL11.glDepthMask(false)
			GL11.glEnable(GL11.GL_BLEND)
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)

			// Setup texture and color
			GL11.glDisable(GL11.GL_TEXTURE_2D)
			GL11.glColor4f(col.R, col.G, col.B, .5f)

			rays.render(size)

			// Reset Pt 1
			GL11.glDepthMask(true)
			GL11.glDisable(GL11.GL_BLEND)
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE)
			GL11.glEnable(GL11.GL_TEXTURE_2D)
		}

		//Reset Context
		GL11.glPopMatrix()
	}

	def rotateRender(model: ModelLamp, scale: Float, col: Color, isOn: Boolean, side: ForgeDirection) {
		GL11.glPushMatrix()
		side match {
			case ForgeDirection.DOWN =>
			case ForgeDirection.UP => {
				GL11.glRotatef(180, 1, 0, 0)
				GL11.glTranslatef(0, -2, 0)
			}
			case ForgeDirection.NORTH => {
				GL11.glRotatef(90, 1, 0, 0)
				GL11.glTranslatef(0, -1, -1)
			}
			case ForgeDirection.SOUTH => {
				GL11.glRotatef(270, 1, 0, 0)
				GL11.glTranslatef(0, -1, 1)
			}
			case ForgeDirection.WEST => {
				GL11.glRotatef(90, 0, 0, 1)
				GL11.glTranslatef(1, -1, 0)
			}
			case ForgeDirection.EAST => {
				GL11.glRotatef(270, 0, 0, 1)
				GL11.glTranslatef(-1, -1, 0)
			}
			case ForgeDirection.UNKNOWN =>
		}
		model.render(col, isOn, side)
		GL11.glPopMatrix()
	}

	def getModel(shape: Shapes): ModelLamp = {
		if (shape == null) ModelCagedLamp
		else {
			shape match {
				case Caged => ModelCagedLamp
				case Block => ModelBlockLamp
				case Panel => ModelPanelLamp
				case Bulb => ModelBulbLamp
			}
		}
	}

	case class Color(R: Float, G: Float, B: Float)

}