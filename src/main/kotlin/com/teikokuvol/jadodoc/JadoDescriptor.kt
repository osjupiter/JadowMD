package com.teikokuvol.jadodoc

import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.html.SimpleTagProvider
import org.intellij.markdown.parser.LinkMap
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.charset.Charset

class JadoMdDescriptor(val gfm: GFMFlavourDescriptor) : MarkdownFlavourDescriptor by gfm {
    val root = IndexNode("root", 0)
    val lists = mutableListOf<IndexNode>(root)

    class RecordAtxTargetsProvidor(val tag: String, val lists: MutableList<IndexNode>) : GeneratingProvider {
        override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
            val token =
                    node.children.first { it.type == MarkdownTokenTypes.ATX_CONTENT || it.type == MarkdownTokenTypes.SETEXT_CONTENT }
                            .children.first { it.type == MarkdownTokenTypes.TEXT }
            val content = (text.substring(token.startOffset, token.endOffset))
            visitor.consumeTagOpen(node, tag)
            visitor.consumeHtml("<a id=\"${content}\"></a>")
            node.acceptChildren(visitor)
            visitor.consumeTagClose(tag)
            val node = IndexNode(content, tag.last().toString().toInt())
            val last = lists.last()
            while (lists.last().level >= node.level) {
                lists.remove(lists.last())
            }
            lists.last().child.add(node)
            lists.add(node)
        }

    }


    override fun createHtmlGeneratingProviders(linkMap: LinkMap, baseURI: URI?): Map<IElementType, GeneratingProvider> {
        val supermap = gfm.createHtmlGeneratingProviders(linkMap, baseURI)
        val superFence = supermap[MarkdownElementTypes.CODE_FENCE]!!
        return supermap + hashMapOf(
                MarkdownElementTypes.CODE_FENCE to object : GeneratingProvider {
                    override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
                        node.acceptChildren(visitor)
                        var codelang: String? = null
                        val first = node.children.firstOrNull { it.type == MarkdownTokenTypes.FENCE_LANG }
                        first?.let {
                            codelang = (text.substring(first.startOffset, first.endOffset))
                        }
                        if (codelang == "puml") {
                            val firstToken = node.children.first { it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT }
                            val lastToken = node.children.last { it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT }
                            val content = (text.substring(firstToken.startOffset, lastToken.endOffset))
                            visitor.consumeHtml(puml2svg(content))
                        } else {
                            superFence.processNode(visitor, text, node)
                        }
                    }
                },
                // markdwon contents shoud be wrapped by div
                MarkdownElementTypes.MARKDOWN_FILE to SimpleTagProvider("div"),
                MarkdownElementTypes.ATX_1 to RecordAtxTargetsProvidor("h1", lists),
                MarkdownElementTypes.ATX_2 to RecordAtxTargetsProvidor("h2", lists),
                MarkdownElementTypes.ATX_3 to RecordAtxTargetsProvidor("h3", lists),
                MarkdownElementTypes.ATX_4 to RecordAtxTargetsProvidor("h4", lists),
                MarkdownElementTypes.ATX_5 to RecordAtxTargetsProvidor("h5", lists),
                MarkdownElementTypes.ATX_6 to RecordAtxTargetsProvidor("h6", lists),
                MarkdownElementTypes.SETEXT_1 to RecordAtxTargetsProvidor("h1", lists),
                MarkdownElementTypes.SETEXT_2 to RecordAtxTargetsProvidor("h2", lists)


        )
    }
}

fun puml2svg(puml: String): String {
    val reader = SourceStringReader(puml)
    val os = ByteArrayOutputStream()
    val desc = reader.outputImage(os, FileFormatOption(FileFormat.SVG))
    os.close()
    val svg = String(os.toByteArray(), Charset.forName("UTF-8"))

    return svg.removePrefix("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
}
