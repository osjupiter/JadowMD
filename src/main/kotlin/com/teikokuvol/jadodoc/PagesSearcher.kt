package com.teikokuvol.jadodoc

import java.io.File


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
    override fun getPages(args: CommandArgs): Pages {

        // for directory
        if (args.directory.isNotEmpty()) {
            val res = mutableListOf<PageFile>()
            args.directory.forEach {mdname->
                val texts = File(mdname).listFiles()
                texts.forEach {
                    res.add(PageFile(
                            name = it.nameWithoutExtension,
                            text = it.readText()
                    ))
                }
            }
            return Pages(res)

        }
        val f = File(args.source)
        return Pages(listOf(PageFile(
                name = f.nameWithoutExtension,
                text = f.readText()
        )))
    }
}
