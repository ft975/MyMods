package mods.ft975.util.render;

import static org.lwjgl.opengl.GL11.*;

public class RenderEnum {
	private static enum BaseEnum {
		;

		public final int v;

		BaseEnum(int val) {v = val;}
	}

	public static enum DrawMode {
		Points(GL_POINTS),
		Line(GL_LINE),
		LineLoop(GL_LINE_LOOP),
		LineStrip(GL_LINE_STRIP),
		Triangles(GL_TRIANGLES),
		TriangleStrip(GL_TRIANGLE_STRIP),
		TriangleFan(GL_TRIANGLE_FAN),
		Quads(GL_QUADS),
		QuadStrip(GL_QUAD_STRIP),
		Polygon(GL_POLYGON);

		public final int v;

		DrawMode(int val) {v = val;}
	}

	public static enum Toggleable {
		AlphaTest(GL_ALPHA_TEST),
		AutoNormal(GL_AUTO_NORMAL),
		Blend(GL_BLEND),
		ClipPlane0(GL_CLIP_PLANE0),
		ClipPlane1(GL_CLIP_PLANE1),
		ClipPlane2(GL_CLIP_PLANE2),
		ClipPlane3(GL_CLIP_PLANE3),
		ClipPlane4(GL_CLIP_PLANE4),
		ClipPlane5(GL_CLIP_PLANE5),
		ColorLogicOp(GL_COLOR_LOGIC_OP),
		ColorMaterial(GL_COLOR_MATERIAL),
		CullFace(GL_CULL_FACE),
		DepthTest(GL_DEPTH_TEST),
		Dither(GL_DITHER),
		Fog(GL_FOG),
		IndexLogicOp(GL_INDEX_LOGIC_OP),
		Light0(GL_LIGHT0),
		Light1(GL_LIGHT1),
		Light2(GL_LIGHT2),
		Light3(GL_LIGHT3),
		Light4(GL_LIGHT4),
		Light5(GL_LIGHT5),
		Light6(GL_LIGHT6),
		Light7(GL_LIGHT7),
		Lighting(GL_LIGHTING),
		LineSmooth(GL_LINE_SMOOTH),
		LineStipple(GL_LINE_STIPPLE),
		Map1Color4(GL_MAP1_COLOR_4),
		Map1Index(GL_MAP1_INDEX),
		Map1Normal(GL_MAP1_NORMAL),
		Map1TextureCoord1(GL_MAP1_TEXTURE_COORD_1),
		Map1TextureCoord2(GL_MAP1_TEXTURE_COORD_2),
		Map1TextureCoord3(GL_MAP1_TEXTURE_COORD_3),
		Map1TextureCoord4(GL_MAP1_TEXTURE_COORD_4),
		Map1Vertex3(GL_MAP1_VERTEX_3),
		Map1Vertex4(GL_MAP1_VERTEX_4),
		Map2Color4(GL_MAP2_COLOR_4),
		Map2Index(GL_MAP2_INDEX),
		Map2Normal(GL_MAP2_NORMAL),
		Map2TextureCoord1(GL_MAP2_TEXTURE_COORD_1),
		Map2TextureCoord2(GL_MAP2_TEXTURE_COORD_2),
		Map2TextureCoord3(GL_MAP2_TEXTURE_COORD_3),
		Map2TextureCoord4(GL_MAP2_TEXTURE_COORD_4),
		Map2Vertex3(GL_MAP2_VERTEX_3),
		Map2Vertex4(GL_MAP2_VERTEX_4),
		Normalize(GL_NORMALIZE),
		PointSmooth(GL_POINT_SMOOTH),
		PolygonOffsetFill(GL_POLYGON_OFFSET_FILL),
		PolygonOffsetLine(GL_POLYGON_OFFSET_LINE),
		PolygonOffsetPoint(GL_POLYGON_OFFSET_POINT),
		PolygonSmooth(GL_POLYGON_SMOOTH),
		PolygonStipple(GL_POLYGON_STIPPLE),
		ScissorTest(GL_SCISSOR_TEST),
		StencilTest(GL_STENCIL_TEST),
		Texture1d(GL_TEXTURE_1D),
		Texture2d(GL_TEXTURE_2D),
		TextureGenQ(GL_TEXTURE_GEN_Q),
		TextureGenR(GL_TEXTURE_GEN_R),
		TextureGenS(GL_TEXTURE_GEN_S),
		TextureGenT(GL_TEXTURE_GEN_T);

		public final int v;

		Toggleable(int val) {v = val;}
	}

	public static enum QuadVert {
		TopLeft(0), TopRight(1), BotRight(2), BotLeft(3);

		public final byte v;

		public static final QuadVert[] values = {TopLeft, TopRight, BotRight, BotLeft};

		QuadVert(int val) {v = (byte) val;}
	}
}
