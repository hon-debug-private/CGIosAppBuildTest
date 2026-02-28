package io.github.honatsugiexpress.canvasegg.devicetest

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.resolvers.file.AndroidContentUriResolver
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@RunWith(AndroidJUnit4::class)
class RenderTextTest {
    @OptIn(ExperimentalTime::class, ExperimentalTestApi::class)
    @Test
    fun fontFamilyResolveTest() = runComposeUiTest {
        setContent {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val document = Ksoup.parseXml("""
                <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                    <style>
                        @font-face {
                            font-family: roboto-custom;
                            src: url("Fonts/Roboto/Roboto-VariableFont_wdth,wght.ttf");
                        }
                        #text1 {
                            font-family: roboto-custom;
                        }
                    </style>
                    <rect id="rect1" width="50px" height="30px" x="20px" y="5px" fill="red"/>
                    <rect id="rect2" width="50px" height="30px" x="30px" y="10px" fill="blue"/>
                    <text id="text1" fill="yellow" font-size="20px" x="20px" y="20px">AAAあああ</text>
                </svg>
            """.trimIndent())
            val values = ContentValues().apply {
                put(MediaStore.Files.FileColumns.MIME_TYPE, "image/png")
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/CanvasEggGallery")
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, "${Clock.System.now().toEpochMilliseconds()}")
            }
            println(context.contentResolver.persistedUriPermissions)
            val uri = context.contentResolver.insert(
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                values
            )
            val env = SvgParserEnv.get {
                uriResolvers += AndroidContentUriResolver("content://com.android.externalstorage.documents/tree/primary%3ADocuments%2FCanvasEggGallery") {
                    null
                }
            }
            val parser = SvgCanvasParser(document, env)
            val bitmap = ImageBitmap(300, 300)
            val canvas = Canvas(bitmap)
            CanvasDrawScope().apply {
                draw(
                    LocalDensity.current,
                    layoutDirection,
                    canvas,
                    Size(300f, 300f)
                ) {
                    parser.draw(this)
                }
            }
            uri?.let {
                context.contentResolver.openOutputStream(uri)?.use {
                    bitmap.asAndroidBitmap().compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        it
                    )
                }
            }
        }
    }
}