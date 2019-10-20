package sample

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class AnchorTest {
  @Test
  fun pass() {
    assertTrue(true) { "Anchor test failed" }
  }
}
