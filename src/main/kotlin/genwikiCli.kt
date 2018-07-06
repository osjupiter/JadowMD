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
import org.intellij.markdown.parser.MarkdownParser
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.charset.Charset


fun main(args: Array<String>) {
    val src = """
Some *Markdown*"
```puml
@startuml
a -> b
aa ->c
@enduml
```
aaaa
===

```golang

func main(){
    a:=0
}

```

hogeojo
---

### sho

- hoge
- aaa
- bbb

"""
    val flavour = MyDesc(GFMFlavourDescriptor())
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(src)
    val html = HtmlGenerator(src, parsedTree, flavour).generateHtml()

    println("################")
    println(html)

    println("################")
}


fun puml2svg(puml: String): String {
    val reader = SourceStringReader(puml)
    val os = ByteArrayOutputStream()
    val desc = reader.outputImage(os, FileFormatOption(FileFormat.SVG))
    os.close()
    val svg = String(os.toByteArray(), Charset.forName("UTF-8"))

    return svg.removePrefix("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
}

class MyDesc(val gfm: GFMFlavourDescriptor) : MarkdownFlavourDescriptor by gfm {
    override fun createHtmlGeneratingProviders(linkMap: LinkMap, baseURI: URI?): Map<IElementType, GeneratingProvider> {
        val supermap = gfm.createHtmlGeneratingProviders(linkMap, baseURI)
        val superFence = supermap[MarkdownElementTypes.CODE_FENCE]!!
        return supermap + hashMapOf(
                MarkdownElementTypes.CODE_FENCE to object : GeneratingProvider {
                    override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
                        node.acceptChildren(visitor)
                        val first = node.children.first { it.type == MarkdownTokenTypes.FENCE_LANG }
                        val codelang = (text.substring(first.startOffset, first.endOffset))
                        if (codelang == "puml") {
                            visitor.consumeTagOpen(node, "hey")
                            visitor.consumeTagClose("hey")
                            val firstToken = node.children.first { it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT }
                            val lastToken = node.children.last { it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT }
                            val content = (text.substring(firstToken.startOffset, lastToken.endOffset))
                            println(content)
                            visitor.consumeHtml(puml2svg(content))
                        } else {
                            superFence.processNode(visitor, text, node)
                        }

                    }
                },
                MarkdownElementTypes.MARKDOWN_FILE to SimpleTagProvider("div")
        )
    }
}
