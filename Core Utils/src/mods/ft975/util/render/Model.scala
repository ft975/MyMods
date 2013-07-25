package mods.ft975.util.render

import java.util.InputMismatchException
import mods.ft975.util.render.Model._
import mods.ft975.util.render.values._
import scala.runtime.ScalaRunTime
import scala.xml.{MalformedAttributeException, NodeSeq, Elem}


class Model(
						 verts: Seq[Vertex],
						 colors: (Seq[Color], UsageType),
						 uvs: (Seq[UV], UsageType),
						 light: (Seq[Light], UsageType),
						 normals: (Seq[Vector], UsageType)) {

	def render(x: Double, y: Double, z: Double) {
		Tessellator.startQuads()
		renderAlt(Pre, 0)
		for (i <- 0 until verts.size) {
			renderAlt(PerVert, i)
			Tessellator.addVert(verts(i))
		}
		Tessellator.setOffset(x, y, z)
		Tessellator.draw()
	}

	protected def renderAlt(stage: RenderStage, i: Int) {
		colors._2 match {
			case Constant => if (stage == Pre) Tessellator.setColor(colors._1(0))
			case Each => if (stage == PerVert) Tessellator.setColor(colors._1(i))
			case _ =>
		}
		uvs._2 match {
			case Constant => if (stage == Pre) Tessellator.setUV(uvs._1(0))
			case Each => if (stage == PerVert) Tessellator.setUV(uvs._1(i))
			case _ =>
		}
		light._2 match {
			case Constant => if (stage == Pre) Tessellator.setLighting(light._1(0).lvl)
			case Each => if (stage == PerVert) Tessellator.setLighting(light._1(i).lvl)
			case _ =>
		}
		normals._2 match {
			case Constant => if (stage == Pre) Tessellator.setNormal(normals._1(0))
			case Each => if (stage == PerVert) Tessellator.setNormal(normals._1(i))
			case _ =>
		}
	}

	override val hashCode: Int = ScalaRunTime._hashCode((verts, colors._1, uvs._1, light._1, normals._1))
	override def equals(obj: Any): Boolean = ScalaRunTime._equals((verts, colors._1, uvs._1, light._1, normals._1), obj)

	override val toString: String = {
		s"Verts: {${var str = " "; verts.foreach((v) => {str += s"$v, "}); str}}, " +
			s"Colors {${var str = " "; if (colors._2 != Off) colors._1.foreach((v) => {str += s"$v, "}); str}}, " +
			s"UVs {${var str = " "; if (uvs._2 != Off) uvs._1.foreach((v) => {str += s"$v, "})}; str}, " +
			s"Lighting {${var str = " "; if (light._2 != Off) light._1.foreach((v) => {str += s"$v, "}); str}}, " +
			s"Normals {${var str = " "; if (normals._2 != Off) normals._1.foreach((v) => {str += s"$v, "}); str}}"
	}
	def toXML: Elem = <Model>
											<Config>
												<Colors>{colors._2.toString}</Colors>
												<UVCoords>{uvs._2.toString}</UVCoords>
												<LightLevel>{light._2.toString}</LightLevel>
												<Normals>{normals._2.toString}</Normals>
											</Config>
											<Data>
												<Verts>{for (v <- verts) yield v.toXML}</Verts>
												<Colors>{if (colors._1 != null) for (v <- colors._1) yield v.toXML else ""}</Colors>
												<UVCoords>{if (uvs._1 != null) for (v <- uvs._1) yield v.toXML else ""}</UVCoords>
												<LightLevel>{if (light._1 != null) for (v <- light._1) yield v.toXML else ""}</LightLevel>
												<Normals>{if (normals._1 != null) for (v <- normals._1) yield v.toXML else ""}</Normals>
											</Data>
										</Model>
}

object Model {
	def fromXML(xml: Elem): Model = {
		try {
			xml match {
				case <Model>{v@_*}</Model> => {
					val conf = v \\ "Config"
					val data = v \\ "Data"

					val iCol = confMatch(conf \\ "Colors")
					val iUVc = confMatch(conf \\ "UVCoords")
					val iLgt = confMatch(conf \\ "LightLevel")
					val iNrm = confMatch(conf \\ "Normals")

					val dVrt = ((data \\ "Verts") \\ "vert").map(Vertex.fromXML)
					val dCol = if (iCol == Off) {null} else {((data \\ "Colors") \\ "col").map(Color.fromXML)}
					val dUVc = if (iUVc == Off) {null} else {((data \\ "UVCoords") \\ "uv").map(UV.fromXML)}
					val dLgt = if (iLgt == Off) {null} else {((data \\ "LightLevel") \\ "lght").map(Light.fromXML)}
					val dNrm = if (iNrm == Off) {null} else {((data \\ "Normals") \\ "vec").map(Vector.fromXML)}

					return new Model(dVrt, (dCol, iCol), (dUVc, iUVc), (dLgt, iLgt), (dNrm, iNrm))
				}
				case _ => throw new InputMismatchException(xml.toString())
			}

			def confMatch(node: NodeSeq): UsageType = {
				node.text match {
					case "OFF" => Off
					case "CONS" => Constant
					case "EACH" => Each
					case _ => throw new MalformedAttributeException(s"Invalid property $node")
				}
			}
		} catch {
			case e: Throwable => throw new Exception(xml.toString(), e)
		}
		throw new Exception(xml.toString())
	}


	sealed class UsageType(v: Byte)
	case object Off extends UsageType(0) {override def toString: String = "OFF"}
	case object Constant extends UsageType(1) {override def toString: String = "CONS"}
	case object Each extends UsageType(2) {override def toString: String = "EACH"}

	sealed class RenderStage(v: Byte)
	case object Pre extends RenderStage(0)
	case object PerVert extends RenderStage(1)
}

object ModelBuilder