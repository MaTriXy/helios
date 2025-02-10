package helios.instances

import arrow.core.Either
import arrow.core.Option
import arrow.core.Tuple2
import arrow.core.Tuple3
import arrow.core.NonEmptyList
import helios.arrow.UnitSpec
import helios.arrow.generators.nonEmptyList
import helios.arrow.generators.tuple2
import helios.arrow.generators.tuple3
import helios.core.JsArray
import helios.test.generators.alphaStr
import helios.test.generators.jsBoolean
import helios.test.generators.jsString
import io.kotest.assertions.arrow.either.beLeft
import io.kotest.assertions.arrow.either.beRight
import io.kotest.assertions.arrow.either.either
import io.kotest.assertions.arrow.option.option
import io.kotest.properties.Gen
import io.kotest.properties.assertAll
import io.kotest.properties.bool
import io.kotest.properties.double
import io.kotest.should

class InstancesArrowTest : UnitSpec() {
  init {

    "Option should be encoded and decoded successfully"{
      assertAll(Gen.option(Gen.alphaStr())) { sample ->
        Option.decoder(String.decoder()).decode(Option.encoder(String.encoder()).run {
          sample.encode()
        }) should beRight(sample)
      }
    }

    "Option should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        Option.decoder(String.decoder()).decode(sample) should beLeft()
      }
    }

    "Either should be encoded and decoded successfully"{
      assertAll(Gen.either(Gen.alphaStr(), Gen.double())) { sample ->
        Either.decoder(String.decoder(), Double.decoder()).decode(
          Either.encoder(String.encoder(), Double.encoder()).run {
            sample.encode()
          }) should beRight(sample)
      }
    }

    "Either should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        Either.decoder(String.decoder(), Double.decoder()).decode(sample) should beLeft()
      }
    }

    "Tuple2 should be encoded and decoded successfully"{
      assertAll(Gen.tuple2(Gen.alphaStr(), Gen.double())) { sample ->
        Tuple2.decoder(String.decoder(), Double.decoder()).decode(
          Tuple2.encoder(String.encoder(), Double.encoder()).run {
            sample.encode()
          }) should beRight(sample)
      }
    }

    "Tuple2 should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Tuple2.decoder(String.decoder(), Double.decoder()).decode(sample) should beLeft()
      }
    }

    "Tuple3 should be encoded and decoded successfully"{
      assertAll(Gen.tuple3(Gen.alphaStr(), Gen.double(), Gen.bool())) { sample ->
        Tuple3.decoder(String.decoder(), Double.decoder(), Boolean.decoder()).decode(
          Tuple3.encoder(String.encoder(), Double.encoder(), Boolean.encoder()).run {
            sample.encode()
          }) should beRight(sample)
      }
    }

    "Tuple3 should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Tuple3.decoder(
          String.decoder(),
          Double.decoder(),
          Boolean.decoder()
        ).decode(sample) should beLeft()
      }
    }

    "NonEmptyList should be encode and decode successfully" {
      assertAll(Gen.nonEmptyList(Gen.alphaStr())) { sample ->
        NonEmptyList.decoder(String.decoder()).decode(NonEmptyList.encoder(String.encoder()).run {
          sample.encode()
        }) should beRight(sample)
      }
    }

    "NonEmptyList should fail for wrong content" {
      assertAll(Gen.jsString()) { sample ->
        NonEmptyList.decoder(String.decoder()).decode(sample) should beLeft()
      }
    }

    "NonEmptyList should fail for empty lists" {
      val empty = JsArray(emptyList())
      NonEmptyList.decoder(String.decoder()).decode(empty) should beLeft()
    }

  }
}
