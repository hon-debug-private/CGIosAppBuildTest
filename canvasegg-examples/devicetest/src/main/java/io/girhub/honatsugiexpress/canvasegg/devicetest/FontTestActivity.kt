package io.github.honatsugiexpress.canvasegg.devicetest

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.devicetest.ui.theme.CanvasEggTheme
import io.github.honatsugiexpress.canvasegg.resolvers.file.AndroidContentUriResolver
import io.github.honatsugiexpress.canvasegg.ui.SvgCanvas
import okio.buffer
import okio.sink
import java.io.File
import kotlin.collections.plusAssign
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FontTestActivity : ComponentActivity() {
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasEggTheme {
                println(contentResolver.persistedUriPermissions)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val document = Ksoup.parseXml(
                        """
                            <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                                <style>
                                    @font-face {
                                        font-family: roboto-custom;
                                        src: url("Fonts/Science_Gothic/ScienceGothic-VariableFont_CTRS,slnt,wdth,wght.ttf");
                                    }
                                    #text1 {
                                        font-family: roboto-custom;
                                    }
                                </style>
                                <rect id="rect1" width="50px" height="30px" x="20px" y="5px" fill="red"/>
                                <rect id="rect2" width="50px" height="30px" x="30px" y="10px" fill="blue"/>
                                <text id="text1" fill="yellow" font-size="48px" x="20px" y="20px">AAAあああ</text>
                            </svg>
                        """.trimIndent()
                    )
                    val env = SvgParserEnv.get {
                        uriResolvers += AndroidContentUriResolver(
                            "content://com.android.externalstorage.documents/tree/primary%3ADocuments%2FCanvasEggGallery"
                        ) { request ->
                            val dir = cacheDir
                                .resolve("fonts")
                                .resolve(request.path.substringBeforeLast("/"))
                            dir.mkdirs()
                            val file = File(dir, request.path.substringAfterLast("/"))
                            file.sink().buffer().use { sink ->
                                sink.writeAll(request.source)
                            }
                            file.absolutePath
                        }
                    }
                    val parser = SvgCanvasParser(document, env)
                    SvgCanvas(
                        parser = parser,
                        modifier = Modifier.size(300.dp)
                    )
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.OpenDocument()
                    ) { uri ->
                        if (uri != null) {
                            val modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            contentResolver.takePersistableUriPermission(uri, modeFlags)
                        }
                    }
                    val launcher2 = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.OpenDocumentTree()
                    ) { uri ->
                        println(uri)
                        if (uri != null) {
                            val modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            contentResolver.takePersistableUriPermission(uri, modeFlags)
                        }
                    }
                    val density = LocalDensity.current
                    Button(
                        onClick = {
                            launcher.launch(arrayOf("*/*"))
                        }
                    ) {
                        Text("ランチャーを開く")
                    }
                    Button(
                        onClick = {
                            launcher2.launch(null)
                        }
                    ) {
                        Text("ツリーーを開く")
                    }
                    Button(
                        onClick = {
                            val parser = SvgCanvasParser(document, env)
                            val bitmap = ImageBitmap(300, 300)
                            val canvas = Canvas(bitmap)
                            CanvasDrawScope().apply {
                                draw(
                                    density,
                                    layoutDirection,
                                    canvas,
                                    Size(300f, 300f)
                                ) {
                                    parser.draw(this)
                                }
                            }
                            val values = ContentValues().apply {
                                put(MediaStore.Files.FileColumns.MIME_TYPE, "image/png")
                                put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/CanvasEggGallery")
                                put(MediaStore.Files.FileColumns.DISPLAY_NAME, "${Clock.System.now().toEpochMilliseconds()}")
                            }
                            val uri = contentResolver.insert(
                                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                                values
                            )
                            uri?.let {
                                contentResolver.openOutputStream(uri)?.use {
                                    bitmap.asAndroidBitmap().compress(
                                        Bitmap.CompressFormat.PNG,
                                        100,
                                        it
                                    )
                                }
                            }
                        }
                    ) {
                        Text("画像として保存")
                    }
                }
            }
        }
    }
}