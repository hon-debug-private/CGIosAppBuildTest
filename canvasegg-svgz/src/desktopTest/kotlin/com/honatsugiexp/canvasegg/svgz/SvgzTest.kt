package com.honatsugiexp.canvasegg.svgz

import ca.gosyer.appdirs.AppDirs
import com.fleeksoft.ksoup.Ksoup
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SvgzTest {
    private val testDir = AppDirs {
        appName = "CanvasEgg-Svg-Test-Suite-Test"
    }
    @Test
    fun compress1() {
        val document = Ksoup.parseXml("""
            <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                <rect width="100px" height="100px" x="100px" y="100px" fill="red"/>
                <rect width="100px" height="100px" x="150px" y="150px" fill="blue"/>
            </svg>
        """.trimIndent())
        val writer = SvgzWriter(document)
        val fileSystem = FileSystem.SYSTEM
        val testPath = testDir.getUserDataDir().toPath().resolve("svgz-test")
        fileSystem.createDirectories(testPath)
        val file = testPath.resolve("${Clock.System.now().toEpochMilliseconds()}.svgz")
        val sink = FileSystem.SYSTEM.sink(file).buffer()
        writer.writeTo(sink)
    }
}