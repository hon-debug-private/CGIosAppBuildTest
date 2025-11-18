package com.honatsugiexp.canvasegg.svgtestsuite

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import ca.gosyer.appdirs.AppDirs
import canvasegg.canvasegg_examples.canvasegg_svg_test_suite.generated.resources.Res
import com.fleeksoft.ksoup.Ksoup
import com.honatsugiexp.canvasegg.common.util.attrOrNull
import com.honatsugiexp.canvasegg.data.svg.parser.SvgCanvasParser
import com.honatsugiexp.canvasegg.data.svg.parser.SvgParserEnv
import com.honatsugiexp.canvasegg.data.svg.parser.get
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.canvasegg.data.svg.type.toPxValue
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RenderTest {
    private val testDir = AppDirs {
        appName = "CanvasEgg-Svg-Test-Suite-Test"
    }
    @OptIn(ExperimentalTestApi::class, ExperimentalUuidApi::class)
    @Test
    fun renderAll() = runComposeUiTest(testTimeout = 10.minutes) {
        val files = Res.readBytes("files/files.txt").decodeToString().lines().map {
            Res.readBytes(it).decodeToString()
        }
        setContent {
            files.forEachIndexed { index, pathLine ->
                val document = Ksoup.parseXml(pathLine)
                val env = SvgParserEnv.get()
                val parser = SvgCanvasParser(document, env)
                val lenEnv = SvgLength.Env.fromCommand(document, density.density)
                val width = SvgLength(document.attrOrNull("width") ?: "300px").toPxValue(lenEnv)
                val height = SvgLength(document.attrOrNull("height") ?: "300px").toPxValue(lenEnv)
                val image = ImageBitmap(width.roundToInt(), height.roundToInt())
                val canvas = Canvas(image)
                parser.draw(
                    CanvasDrawScope().apply {
                        drawContext.canvas = canvas
                    }
                )
                val dir = File(testDir.getUserDataDir(), "svg")
                dir.mkdirs()
                val file = File(dir, "${pathLine.removePrefix("files/svg/")}.png")
                ImageIO.write(image.toAwtImage(), "png", file)
            }
        }
    }
    @OptIn(ExperimentalTestApi::class, ExperimentalUuidApi::class)
    @Test
    fun renderTest1() = runComposeUiTest(testTimeout = 10.seconds) {
        setContent {
            println("aaa")
            val svgFile = """
            <svg width="300px" height="300px">
                <rect width="100px" height="175px" x="30px" y="55px" fill="red"/>
                <g transform="scale(2, 3)">
                    <rect width="100px" height="175px" x="30px" y="55px" fill="blue"/>
                    <g transform="translate(10, 10)">
                        <rect width="100px" height="175px" x="30px" y="55px" fill="green"/>
                        <g transform="scale(0.9), translate(-10, -10)">
                            <rect width="100px" height="175px" x="30px" y="55px" fill="purple"/>
                        </g>
                    </g>
                </g>
            </svg>
        """.trimIndent()
            val document = Ksoup.parseXml(svgFile)
            println("bbb")
            val env = SvgParserEnv.get()
            val parser = SvgCanvasParser(document, env)
            println("ccc")
            val lenEnv = SvgLength.Env.fromCommand(document, density.density)
            val width = SvgLength(document.attrOrNull("width") ?: "300px").toPxValue(lenEnv)
            val height = SvgLength(document.attrOrNull("height") ?: "300px").toPxValue(lenEnv)
            val image = ImageBitmap(width.roundToInt(), height.roundToInt())
            println("ddd")
            val canvas = Canvas(image)
            println("eee")
            parser.draw(
                CanvasDrawScope().apply {
                    drawContext.canvas = canvas
                }
            )
            println("fff")
            val dir = File(testDir.getUserDataDir(), "svg-unit")
            dir.mkdirs()
            val file = File(dir, "${Uuid.random()}.png")
            ImageIO.write(image.toAwtImage(), "png", file)
        }
    }
    @OptIn(ExperimentalTestApi::class, ExperimentalUuidApi::class)
    @Test
    fun renderTest2() = runComposeUiTest(testTimeout = 10.seconds) {
        setContent {
            println("aaa")
            val svgFile = """
            <svg width="300px" height="300px">
                <rect width="100px" height="175px" x="30px" y="55px" fill="red"/>
                <g transform="scale(2)">
                    <rect width="100px" height="175px" x="30px" y="55px" fill="red"/>
                </g>
            </svg>
        """.trimIndent()
            val document = Ksoup.parseXml(svgFile)
            println("bbb")
            val env = SvgParserEnv.get()
            val parser = SvgCanvasParser(document, env)
            println("ccc")
            val lenEnv = SvgLength.Env.fromCommand(document, density.density)
            val width = SvgLength(document.attrOrNull("width") ?: "300px").toPxValue(lenEnv)
            val height = SvgLength(document.attrOrNull("height") ?: "300px").toPxValue(lenEnv)
            val image = ImageBitmap(width.roundToInt(), height.roundToInt())
            println("ddd")
            val canvas = Canvas(image)
            println("eee")
            parser.draw(
                CanvasDrawScope().apply {
                    drawContext.canvas = canvas
                }
            )
            println("fff")
            val dir = File(testDir.getUserDataDir(), "svg-unit")
            dir.mkdirs()
            val file = File(dir, "${Uuid.random()}.png")
            ImageIO.write(image.toAwtImage(), "png", file)
        }
    }
    @OptIn(ExperimentalTestApi::class, ExperimentalUuidApi::class)
    @Test
    fun renderTest3() = runComposeUiTest {
        val image = ImageBitmap(300, 300)
        val canvas = Canvas(image)
        CanvasDrawScope().apply {
            drawContext.canvas = canvas
            val colorRed = Color.Red
            val colorBlue = Color.Blue
            val colorGreen = Color.Green
            val colorPurple = Color(0xFF800080) // Purple

            // --- 1. Red (赤): 変換なし ---
            // <rect width="100px" height="175px" x="30px" y="55px" fill="red"/>
            drawRect(
                color = colorRed,
                topLeft = Offset(30f, 55f),
                size = Size(100f, 175f)
            )

            // --- 2. Blue, Green, Purple のグループ: scale(2, 3) ---
            withTransform({
                // 変換1: scale(2, 3)
                // 基準点 (0, 0) から水平2倍、垂直3倍に拡大
                scale(scaleX = 2f, scaleY = 3f)
            }) {

                // --- 2a. Blue (青): 累積変換: scale(2, 3) ---
                // 描画される位置: x=30*2=60, y=55*3=165
                // 描画されるサイズ: w=100*2=200, h=175*3=525 (画面を大きくはみ出す)
                drawRect(
                    color = colorBlue,
                    topLeft = Offset(30f, 55f),
                    size = Size(100f, 175f)
                )

                // --- 2b. Green, Purple のグループ: translate(10, 10) ---
                // 変換が累積される（scale(2, 3) の後、translate(10, 10) が適用される）
                withTransform({
                    // 変換2: translate(10, 10)
                    // 既に拡大された座標系で移動: 実際の移動量 (10*2, 10*3) = (20, 30)
                    translate(left = 10f, top = 10f)
                }) {

                    // --- 2c. Green (緑): 累積変換: scale(2, 3) translate(10, 10) ---
                    // 描画される位置: x=(30*2)+20=80, y=(55*3)+30=195
                    // 描画されるサイズ: w=200, h=525 (画面を大きくはみ出す)
                    drawRect(
                        color = colorGreen,
                        topLeft = Offset(30f, 55f),
                        size = Size(100f, 175f)
                    )

                    // --- 2d. Purple のグループ: scale(0.9) translate(-10, -10) ---
                    // 変換が累積される
                    withTransform({
                        // SVGの無効な構文を修正: scale(0.9) の後、translate(-10, -10) を適用
                        // 変換3a: scale(0.9)
                        scale(scaleX = 0.9f, scaleY = 0.9f)

                        // 変換3b: translate(-10, -10)
                        // 累積された座標系 (スケール済み) で移動
                        translate(left = -10f, top = -10f)
                    }) {

                        // --- 2e. Purple (紫): 累積変換: scale(2, 3) translate(10, 10) scale(0.9) translate(-10, -10) ---
                        drawRect(
                            color = colorPurple,
                            topLeft = Offset(30f, 55f),
                            size = Size(100f, 175f)
                        )
                    }
                }
            }
        }

        val dir = File(testDir.getUserDataDir(), "svg-unit")
        dir.mkdirs()
        val file = File(dir, "${Uuid.random()}.png")
        ImageIO.write(image.toAwtImage(), "png", file)
    }
    @OptIn(ExperimentalTestApi::class, ExperimentalUuidApi::class)
    @Test
    fun renderTest4() = runComposeUiTest(testTimeout = 10.seconds) {
        setContent {
            val svgFile = """
            <svg width="300px" height="300px">
                <defs>
                    <linearGradient id="gr1">
                        <stop offset="0%" stop-color="gold" />
                        <stop offset="5%" stop-color="gold" />
                        <stop offset="95%" stop-color="red" />
                        <stop offset="100%" stop-color="red" />
                    </linearGradient>
                </defs>
                <rect width="100px" height="175px" x="30px" y="55px" fill="url(#gr1)"/>
                <rect width="10px" height="15px" x="230px" y="255px" fill="red"/>
            </svg>
        """.trimIndent()
            val document = Ksoup.parseXml(svgFile)
            val env = SvgParserEnv.get()
            val parser = SvgCanvasParser(document, env)
            val lenEnv = SvgLength.Env.fromCommand(document, density.density)
            val width = SvgLength(document.attrOrNull("width") ?: "300px").toPxValue(lenEnv)
            val height = SvgLength(document.attrOrNull("height") ?: "300px").toPxValue(lenEnv)
            val image = ImageBitmap(width.roundToInt(), height.roundToInt())
            val canvas = Canvas(image)
            CanvasDrawScope().apply {
                draw(
                    LocalDensity.current,
                    LocalLayoutDirection.current,
                    canvas,
                    Size(300f, 300f),
                ) {
                    parser.draw(this)
                }
            }
            val dir = File(testDir.getUserDataDir(), "svg-unit")
            dir.mkdirs()
            val file = File(dir, "${Uuid.random()}.png")
            ImageIO.write(image.toAwtImage(), "png", file)
        }
    }
    @OptIn(ExperimentalTestApi::class, ExperimentalUuidApi::class)
    @Test
    fun renderTest5() = runComposeUiTest(testTimeout = 10.seconds) {
        setContent {
            val image = ImageBitmap(300, 300)
            val canvas = Canvas(image)
            CanvasDrawScope().apply {
                draw(
                    LocalDensity.current,
                    LocalLayoutDirection.current,
                    canvas,
                    Size(300f, 300f),
                ) {
                    drawRect(
                        brush = Brush.linearGradient(
                            0.0f to Color.Red,
                            0.3f to Color.Gray,
                            0.45f to Color.Green,
                            0.62f to Color.Blue,
                            0.88f to Color.Magenta,
                            1.0f to Color.Red
                        ),
                        topLeft = Offset(100f, 100f),
                        size = Size(100f, 100f)
                    )
                    drawRect(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Red,
                                Color.Gray,
                                Color.Green,
                                Color.Blue,
                                Color.Magenta,
                                Color.Red
                            )
                        ),
                        topLeft = Offset(200f, 200f),
                        size = Size(100f, 100f)
                    )
                }
            }
            val dir = File(testDir.getUserDataDir(), "svg-unit")
            dir.mkdirs()
            val file = File(dir, "${Uuid.random()}.png")
            ImageIO.write(image.toAwtImage(), "png", file)
        }
    }
}