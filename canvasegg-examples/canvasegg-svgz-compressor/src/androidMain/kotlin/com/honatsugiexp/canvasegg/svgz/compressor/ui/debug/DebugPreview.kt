package com.honatsugiexp.canvasegg.svgz.compressor.ui.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.honatsugiexp.canvasegg.svgz.compressor.ui.nav.NavInit
import com.honatsugiexp.canvasegg.svgz.compressor.ui.nav.NavRoutes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.LocalResourceReader
import org.jetbrains.compose.resources.ResourceReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

@OptIn(ExperimentalResourceApi::class)
internal val InteropResourcesReader = object : ResourceReader {
    override fun getUri(path: String): String {
        val homeDir = System.getProperty("os.home")?.let {
            File(it)
        } ?: File("")
        val resFile =
            homeDir
                .resolve("AndroidStudioProjects")
                .resolve("CanvasEgg")
                .resolve("canvasegg-examples")
                .resolve("canvasegg-svgz-compressor")
                .resolve("build")
                .resolve("generated")
                .resolve("compose")
                .resolve("resourceGenerator")
                .resolve("preparedResources")
                .resolve("commonMain")
                .resolve("composeResources")
                .resolve(path.removePrefix("composeResources/canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources/"))
        return homeDir.resolve(resFile).absolutePath
    }

    override suspend fun read(path: String): ByteArray {
        return getUri(path).let {
            FileInputStream(it).use { inputStream ->
                val buffer = ByteArray(4096)
                val outputStream = ByteArrayOutputStream()
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                return outputStream.toByteArray()
            }
        }
    }

    override suspend fun readPart(
        path: String,
        offset: Long,
        size: Long
    ): ByteArray {
        return getUri(path).let {
            FileInputStream(it).use { inputStream ->
                inputStream.skip(offset)

                val bufferSize = size.toInt()
                val bytes = ByteArray(bufferSize)

                var totalBytesRead = 0
                var remaining = bufferSize

                while (remaining > 0) {
                    val count = inputStream.read(bytes, totalBytesRead, remaining)
                    if (count < 0) {
                        break
                    }
                    totalBytesRead += count
                    remaining -= count
                }

                return if (totalBytesRead == bufferSize) {
                    bytes
                } else {
                    bytes.copyOf(totalBytesRead)
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview(
    device = Devices.PIXEL_5,
    showBackground = true
)
private fun Preview() {
    CompositionLocalProvider(LocalResourceReader provides InteropResourcesReader) {
        NavInit(NavRoutes.Compressor.name)
    }
}