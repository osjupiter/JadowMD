package com.teikokuvol.jadodoc

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.MissingRequiredPositionalArgumentException
import com.xenomachina.argparser.default

class CommandArgs(parser: ArgParser) {

    val source by parser.positional(
            "SOURCE",
            help = "source filename").default("index.md")
    val directory by parser.adding(
            "-r",
            help = "source filename")

    val destination by parser.storing(
            "-o",
            help = "destination dir").default("jadodoc.html")
}


class JadoCli(val searcher: TargetFilesSearcher, val pagesConverter: PagesConverter, val templateEngine: TemplateEngine) {
    fun run(args: Array<String>) {
        val config = ArgParser(args).parseInto(::CommandArgs)
        val pages = searcher.getPages(config)
        val model = pagesConverter.generateModel(pages, config)
        templateEngine.outputDoc(model)
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                JadoCli(
                        TargetFilesSearcherImpl(),
                        PagesConverter(),
                        MustacheEngine()
                ).run(args)
            } catch (e: MissingRequiredPositionalArgumentException) {
                print(e)
            }

        }

    }
}