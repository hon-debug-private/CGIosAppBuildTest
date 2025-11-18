package com.honatsugiexp.cssparser

import com.fleeksoft.ksoup.Ksoup
import kotlin.test.Test

class ParseTest {
    @Test
    fun parseCss1() {
        val document = Ksoup.parseXml("""
            <svg width="300px" height="300px" viewBox="0 0 300 300">
                <style>
                    .fill-red {
                        fill: red;
                    }
                </style>
                <rect id="rect1" class="no-rounded fill-red" width="50px" height="50px" x="100px" y="100px" />
            </svg>
        """.trimIndent())
        val element = document.getElementById("rect1")!!
        val controller = ElementStyleController(element)
        controller.parseDocumentStyles()
        println(element)
        println(controller.styles)
    }
}