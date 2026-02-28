package io.github.honatsugiexpress.canvasegg.resolvers.file

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.SvgFontFace
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.SvgFontFaceSrcKeyword
import io.github.honatsugiexpress.canvasegg.data.svg.type.font.toComposeStyle
import io.github.honatsugiexpress.canvasegg.data.util.BitmapSampleSizeUtil
import okio.Path.Companion.toPath
import okio.buffer
import okio.source
import java.io.File


actual class AndroidContentUriResolver actual constructor(
    context: Any?,
    actual override val baseUri: String,
    fontCopyFunction: FontCopyFunction
): SvgUriResolver {
    val context: Context = context as Context
    val contentResolver: ContentResolver
        get() = context.contentResolver
    val fontCopyFunction = fontCopyFunction

    actual override fun resolveImage(
        uri: String,
        width: Int,
        height: Int
    ): ImageBitmap? {
        return try {
            val treeUri = baseUri.toUri()
            val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
            val selectionArgs = arrayOf(uri.substringAfterLast("/"))
            val contentUri = contentResolver.query(
                treeUri,
                arrayOf(MediaStore.MediaColumns._ID),
                selection,
                selectionArgs,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    ContentUris.withAppendedId(treeUri, id)
                } else null
            } ?: return null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, contentUri)

                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.setTargetSize(width, height)
                    decoder.isMutableRequired = true
                }.asImageBitmap()
            } else {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                contentResolver.openInputStream(contentUri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream, null, options)
                    contentResolver.openInputStream(contentUri)?.use { inputStream2 ->
                        options.inSampleSize =
                            BitmapSampleSizeUtil.calculateInSampleSize(options, width, height)
                        options.inJustDecodeBounds = false

                        BitmapFactory
                            .decodeStream(inputStream2, null, options)
                            ?.scale(width, height)
                            ?.asImageBitmap()
                    }
                }
            }
        } catch (e: Exception) {
            CanvasEggLogger.tag("AndroidContentUriResolver").warnThrowable(e)
            null
        }
    }

    actual override fun resolveFont(fontFace: SvgFontFace): FontFamily? {
        var fontFamily: FontFamily? = null
        fontFace.src.forEach { keyword ->
            try {
                when (keyword) {
                    is SvgFontFaceSrcKeyword.Url -> {
                        val path = keyword.value.toPath(true).segments
                        val treeFile = DocumentFile.fromTreeUri(context, baseUri.toUri())
                        var current: DocumentFile = treeFile ?: return@forEach
                        path.forEach {
                            current = current.findFile(it) ?: return@forEach
                        }
                        val contentUri = current.uri
                        contentResolver.openInputStream(contentUri)?.use { stream ->
                            val filePath = stream.source().buffer().use { source ->
                                fontCopyFunction.onRequest(
                                    FontCopyRequest(
                                        source,
                                        keyword.value
                                    )
                                ) ?: return@forEach
                            }
                            Font(
                                file = File(filePath),
                                weight = fontFace.fontWeight,
                                style = fontFace.fontStyle.toComposeStyle(),
                                variationSettings = (fontFace.fontFeatureSettings + fontFace.fontVariationSettings)
                                    .asSequence()
                                    .chunked(2)
                                    .map { chunk ->
                                        if (chunk.size == 2) {
                                            chunk[0] to (chunk[1].toFloatOrNull() ?: 0f)
                                        } else {
                                            chunk[0] to 0f
                                        }
                                    }
                                    .map {
                                        FontVariation.Setting(it.first, it.second)
                                    }
                                    .let {
                                        FontVariation.Settings(
                                            weight = fontFace.fontWeight,
                                            style = fontFace.fontStyle.toComposeStyle(),
                                            *it.toList().toTypedArray()
                                        )
                                    }
                            )
                        }?.let { font ->
                            fontFamily = FontFamily(font)
                        }
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                CanvasEggLogger.tag("AndroidContentUriResolver").warnThrowable(e)
            }
        }
        return fontFamily
    }
}

@Composable
@Suppress("ComposableNaming")
actual fun AndroidContentUriResolver(baseUri: String, fontCopyFunction: FontCopyFunction): SvgUriResolver {
    return AndroidContentUriResolver(LocalContext.current.applicationContext, baseUri, fontCopyFunction)
}