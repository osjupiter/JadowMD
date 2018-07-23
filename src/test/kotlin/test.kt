import com.teikokuvol.jadodoc.CommandArgs
import com.teikokuvol.jadodoc.JadoCli
import com.teikokuvol.jadodoc.TargetFilesSearcherImpl
import com.xenomachina.argparser.ArgParser
import org.junit.jupiter.api.Test

class TestAll {
    @Test
    fun hoge() {
        JadoCli.main(listOf("readme.md", "-o", "docs/demos/jadodoc.html").toTypedArray())

    }

    @Test
    fun testSercher_ifRoptionNotExistsSizeShoudBe1() {
        val ca = CommandArgs(ArgParser(listOf<String>().toTypedArray()))
        val page = TargetFilesSearcherImpl().getPages(ca)
        assert(page.texts.size == 1)
    }

    @Test
    fun testSercher_ifRoptionExistsSizeShoudNOTBe1() {
        val ca = CommandArgs(ArgParser(listOf<String>("-r", "test/").toTypedArray()))
        val page = TargetFilesSearcherImpl().getPages(ca)
        assert(page.texts.size != 1)
    }

}