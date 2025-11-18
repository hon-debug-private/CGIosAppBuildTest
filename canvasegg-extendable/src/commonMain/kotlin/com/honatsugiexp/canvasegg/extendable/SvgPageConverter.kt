package com.honatsugiexp.canvasegg.extendable

import com.fleeksoft.ksoup.nodes.Document

abstract class SvgPageConverter<PI: PageInfo, P: Page>(): SvgConverter() {
    fun parseMultiDocument(documents: Array<Document>, density: Float) {
        documents.forEach { document ->
            parse(document, density)
        }
    }
    abstract fun startPage(info: PI): P
    abstract fun endPage()
}

abstract class PageInfo
abstract class Page

object NonePageInfo: PageInfo()
object NonePage: Page()