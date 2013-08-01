package mods.ft975.util.render

import cpw.mods.fml.relauncher.{Side, SideOnly}
import mods.ft975.util.render.RenderEnum._
import mods.ft975.util.render.values._
import net.minecraft.util.Icon
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object Tessellator {
	def setColor(r: Double, g: Double, b: Double) { setColor(Color(r, g, b)) }
	def setColor(r: Double, g: Double, b: Double, a: Double) { setColor(Color(r, g, b, a)) }
	def setColor(col: Color) { glColor4d(col.rD, col.gD, col.bD, col.aD) }

	def translate(x: Double, y: Double, z: Double)(drawFunc: () => Unit) {
		pushMatrix()
		glTranslated(x, y, z)
		drawFunc()
		popMatrix()
	}
	def rotate(deg: Double, x: Double, y: Double, z: Double)(drawFunc: () => Unit) {
		pushMatrix()
		glRotated(deg, x, y, z)
		drawFunc()
		popMatrix()
	}
	def scale(scaleX: Double, scaleY: Double, scaleZ: Double)(drawFunc: () => Unit) {
		pushMatrix()
		glScaled(scaleX, scaleY, scaleZ)
		drawFunc()
		popMatrix()
	}
	def scale(scale: Double)(drawFunc: () => Unit) { this.scale(scale, scale, scale)(drawFunc) }

	def addVert(x: Double, y: Double, z: Double) { glVertex3d(x, y, z) }
	def addVert(v: Coord) { glVertex3d(v.x, v.y, v.z) }

	def setUV(u: Double, v: Double) { glTexCoord2d(u, v) }
	def setUV(uv: Vector2) { enable(Toggleable.Texture2d); setUV(uv.u, uv.v) }
	def setNormal(i: Double, j: Double, k: Double) { glNormal3d(i, j, k) }
	def setNormal(vec: Vector3) { setNormal(vec.i, vec.j, vec.k) }
	def setIconUV(icon: Icon, pos: Byte) { setIconUV(icon, QuadVert.values(pos)) }
	def setIconUV(icon: Icon, pos: QuadVert) {
		pos match {
			case QuadVert.TopLeft => setUV(icon.getMinU, icon.getMinV)
			case QuadVert.TopRight => setUV(icon.getMaxU, icon.getMinV)
			case QuadVert.BotRight => setUV(icon.getMaxU, icon.getMaxV)
			case QuadVert.BotLeft => setUV(icon.getMinU, icon.getMaxV)
		}
	}

	def addUVVert(x: Double, y: Double, z: Double, u: Double, v: Double) { setUV(u, v); addVert(x, y, z) }
	def addUVVert(v: Coord, uv: Vector2) { setUV(uv.u, uv.v); addVert(v.x, v.y, v.z) }

	def beginQuads() { beginMode(DrawMode.Quads) }
	def beginMode(mode: DrawMode) { glBegin(mode.v) }

	def pushMatrix() { glPushMatrix() }
	def popMatrix() { glPopMatrix() }

	def disable(element: Toggleable) { glDisable(element.v) }
	def enable(element: Toggleable) { glEnable(element.v) }
	def isCapEnabled_?(element: Toggleable) = { glIsEnabled(element.v) }
}

case class TexturedQuad(v1: TexVert, v2: TexVert, v3: TexVert, v4: TexVert)
case class TexVert(pos: Coord, uv: Vector2)

trait DrawableShape {
	def draw()
}

