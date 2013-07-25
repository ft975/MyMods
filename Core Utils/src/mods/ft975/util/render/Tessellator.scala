package mods.ft975.util.render

import cpw.mods.fml.relauncher.{Side, SideOnly}
import mods.ft975.util.render.DrawMode.DrawMode
import mods.ft975.util.render.values._

@SideOnly(Side.CLIENT)
object Tessellator {
	private val tess = net.minecraft.client.renderer.Tessellator.instance

	private var color = Color.White

	def setColor(r: Double, g: Double, b: Double) { setColor(Color(r, g, b)) }
	def setColor(r: Double, g: Double, b: Double, a: Double) { setColor(Color(r, g, b, a)) }
	def setColor(col: Color) { color = col; setColor() }
	private def setColor() { tess.setColorRGBA(color.r, color.b, color.g, color.a) }

	def disableColor() { tess.disableColor() }
	def setLighting(level: Int) { tess.setBrightness(level) }

	def translate(x: Double, y: Double, z: Double) { tess.addTranslation(x.toFloat, y.toFloat, z.toFloat) }
	def setOffset(x: Double, y: Double, z: Double) { tess.setTranslation(x, y, z) }

	def addUVVert(x: Double, y: Double, z: Double, u: Double, v: Double) { setUV(u, v); addVert(x, y, z) }
	def addUVVert(v: Vertex, uv: UV) { setUV(uv.u, uv.v); addVert(v.x, v.y, v.z) }
	def setUV(u: Double, v: Double) { tess.setTextureUV(u, v) }
	def setUV(uv: UV) { tess.setTextureUV(uv.u, uv.v) }
	def addVert(x: Double, y: Double, z: Double) { tess.addVertex(x, y, z) }
	def addVert(v: Vertex) { tess.addVertex(v.x, v.y, v.z) }
	def setNormal(i: Double, j: Double, k: Double) { tess.setNormal(i.toFloat, j.toFloat, k.toFloat) }
	def setNormal(vec: Vector) { tess.setNormal(vec.i.toFloat, vec.j.toFloat, vec.k.toFloat) }

	def draw() { tess.draw() }
	def startQuads() { startMode(DrawMode.Quads) }
	def startMode(mode: DrawMode) { tess.startDrawing(mode.ordinal) }
}

object DrawMode {

	sealed class DrawMode(val ordinal: Int)

	case object Points extends DrawMode(0)
	case object Line extends DrawMode(1)
	case object LineLoop extends DrawMode(2)
	case object LineStrip extends DrawMode(3)
	case object Triangles extends DrawMode(4)
	case object TriangleStrip extends DrawMode(5)
	case object TriangleFam extends DrawMode(6)
	case object Quads extends DrawMode(7)
	case object QuadStrip extends DrawMode(8)
	case object Polygon extends DrawMode(9)

}