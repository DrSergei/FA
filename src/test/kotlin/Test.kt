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
    fun testToString() {
        val fa = FA(File("src/test/kotlin/input.txt"))
        assertEquals(
            "10\n" +
                    "2\n" +
                    "[0]\n" +
                    "[[1, 2], [1, 2, 0]]\n" +
                    "[0] 0 [1, 2]\n" +
                    "[0] 1 [2]\n" +
                    "[1, 2] 0 [2, 0]\n" +
                    "[1, 2] 1 [2]\n" +
                    "[2] 0 [0]\n" +
                    "[2] 1 [2]\n" +
                    "[2, 0] 0 [1, 2, 0]\n" +
                    "[2, 0] 1 [2]\n" +
                    "[1, 2, 0] 0 [1, 2, 0]\n" +
                    "[1, 2, 0] 1 [2]\n", fa.toString()
        )
    }
}