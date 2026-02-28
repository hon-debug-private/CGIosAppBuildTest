package io.github.honatsugiexpress.canvasegg.extendable

abstract class SvgPageConverter<PI: PageInfo, P: Page>(): SvgConverter() {
    abstract fun startPage(info: PI): P
    abstract fun endPage()
}

abstract class PageInfo
abstract class Page

object NonePageInfo: PageInfo()
object NonePage: Page()