package com.teikokuvol.jadodoc

import java.io.File
import java.nio.charset.Charset


data class PageFile(
        val name: String,
        val text: String
)

data class Pages(
        var texts: List<PageFile>
)

interface TargetFilesSearcher {
    fun getPages(args: CommandArgs): Pages

}

class TargetFilesSearcherImpl : TargetFilesSearcher {

    private fun readTextFromFile(file: File): String {
        val text = file.readText(Charset.forName("utf-8"))
        return text.replace("(\\r|\\n|\\r\\n)+", "\\\\n")
    }

    override fun getPages(args: CommandArgs): Pages {

        // for directory
        if (args.directory.isNotEmpty()) {
            val res = mutableListOf<PageFile>()
            args.directory.forEach { mdname ->
                val texts = File(mdname).listFiles()
                texts.forEach {
                    res.add(PageFile(
                            name = it.nameWithoutExtension,
                            text = readTextFromFile(it))
                    )
                }
            }
            return Pages(res)

        }
        val f = File(args.source)
        return Pages(listOf(PageFile(
                name = f.nameWithoutExtension,
                text = readTextFromFile(f)
        )))
    }
}
