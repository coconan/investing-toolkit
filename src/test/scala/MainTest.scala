import org.scalatest.funsuite.AnyFunSuite

class MainTest extends AnyFunSuite {
    test("investing-toolkit should be able fetch remote resources") {
        assert(Main.getAll("SP10005282").body.right.get.size > 0)
    }
}