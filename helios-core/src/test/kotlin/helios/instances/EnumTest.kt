package helios.instances

import helios.arrow.UnitSpec
import helios.core.EnumValueNotFound
import helios.core.JsInt
import helios.core.JsString
import helios.core.JsStringDecodingError
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight

private enum class Foo {
  A
}

internal class EnumTest : UnitSpec() {
  init {

    "Enums should be encoded and decoded successfully" {
      Enum.decoder<Foo>().decode(Enum.encoder<Foo>().run {
        Foo.A.encode()
      }).shouldBeRight(Foo.A)
    }

    "invalid enum value produces the correct error" {
      val decoded = Enum.decoder<Foo>().decode(JsString("B"))
      decoded.shouldBeLeft(EnumValueNotFound(JsString("B")))
    }

    "invalid json produces the correct error" {
      val decoded = Enum.decoder<Foo>().decode(JsInt(1))
      decoded.shouldBeLeft(JsStringDecodingError(JsInt(1)))
    }

  }
}
