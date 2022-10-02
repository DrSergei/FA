import java.io.*
import kotlin.test.*
import kotlin.test.Test

internal class Test {
    @Test
    fun testExecute() {
        val fa = FA(File("src/test/kotlin/input.txt"))
        assertEquals(false, fa.execute(listOf()))
        assertEquals(true, fa.execute(listOf(0)))
        assertEquals(false, fa.execute(listOf(0, 0)))
        assertEquals(true, fa.execute(listOf(0, 0, 0, 0)))
        assertEquals(true, fa.execute(listOf(0, 1, 0, 0)))
        assertEquals(false, fa.execute(listOf(1)))
        assertEquals(false, fa.execute(listOf(0, 1)))
    }

    @Test
    fun testAsDFA() {
        val fa = FA(File("src/test/kotlin/input.txt"))
        assertEquals(
            "5\n" +
                    "2\n" +
                    "0\n" +
                    "1 4\n" +
                    "0 0 1\n" +
                    "0 1 2\n" +
                    "1 0 3\n" +
                    "1 1 2\n" +
                    "2 0 0\n" +
                    "2 1 2\n" +
                    "3 0 4\n" +
                    "3 1 2\n" +
                    "4 0 4\n" +
                    "4 1 2\n", fa.toDFA()
        )
    }

    @Test
    fun testMin() {
        val fa = FA(File("src/test/kotlin/min.txt"))
        assertEquals(
            "2\n" +
                    "2\n" +
                    "0\n" +
                    "0\n" +
                    "0 0 0\n" +
                    "0 1 1\n" +
                    "1 0 0\n" +
                    "1 1 1\n", fa.minimize()
        )
    }
}