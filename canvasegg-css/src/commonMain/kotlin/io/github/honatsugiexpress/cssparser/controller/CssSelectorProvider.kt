package io.github.honatsugiexpress.cssparser.controller

interface CssSelectorProvider {
    fun id(): String?
    fun attr(name: String): String?
    fun classNames(): Collection<String>
    fun tagName(): String?
}