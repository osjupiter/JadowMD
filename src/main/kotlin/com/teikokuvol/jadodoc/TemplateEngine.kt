package com.teikokuvol.jadodoc

import com.github.mustachejava.DefaultMustacheFactory
import java.io.File
import java.io.PrintWriter

interface TemplateEngine {
    fun outputDoc(model: DocGenModel)

}

class MustacheEngine : TemplateEngine {
    override fun outputDoc(model: DocGenModel) {
        val mf = DefaultMustacheFactory()
        val mustache = mf.compile("template.mustache")
        mustache.execute(PrintWriter(File(model.outputfile),"utf-8"), model).flush()
    }
}