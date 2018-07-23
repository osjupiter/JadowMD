package com.teikokuvol.jadodoc

import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

data class DocGenModel(
        val pages: List<PageGenModel>,
        val outputfile: String
)

data class PageGenModel(
        val id: String,
        val content: String,
        val toc: IndexNode
)

class PagesConverter {
    private fun genToc(node: IndexNode): String {
        fun walk(i: IndexNode, buffer: StringBuffer, isRoot: Boolean = false) {
            if (!isRoot) buffer.append("<li><a href=\"#${i.text}\">" + i.text + "</a>")
            buffer.append("<ul>")
            (i.child.forEach {
                walk(it, buffer)
            })
            buffer.append("</ul>")
            if (!isRoot) buffer.append("</li>")
        }

        val buf = StringBuffer()
        walk(node, buf, true)
        return buf.toString()
    }

    fun generateModel(pages: Pages, args: CommandArgs): DocGenModel {

        val pagemodels = pages.texts.map {
            val flavour = JadoMdDescriptor(GFMFlavourDescriptor())
            val src = it.text
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(src)
            val maincontent = HtmlGenerator(src, parsedTree, flavour).generateHtml()
            PageGenModel(content = maincontent, toc = flavour.root, id = it.name)
        }
        return DocGenModel(pages = pagemodels, outputfile = args.destination)
    }
}

data class IndexNode(val text: String, val level: Int) {
    var child: MutableList<IndexNode> = mutableListOf()
}
