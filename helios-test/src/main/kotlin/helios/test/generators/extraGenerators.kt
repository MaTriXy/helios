package helios.test.generators

import io.kotest.properties.*
import java.math.BigDecimal

fun Gen.Companion.alphaStr() =
  string().map { str -> str.filter(Char::isLetterOrDigit) }.filter(String::isNotBlank)

inline fun <reified A> Gen.Companion.array(genA: Gen<A>): Gen<Array<A>> =
  list(genA).map { it.toTypedArray() }

fun Gen.Companion.short(): Gen<Short> =
  choose(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).map(Int::toShort)

fun Gen.Companion.byte(): Gen<Byte> =
  choose(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).map(Int::toByte)

fun Gen.Companion.bigDecimal(): Gen<BigDecimal> =
  double().filterNot { it.isInfinite() || it.isNaN() }.map(Double::toBigDecimal)

fun <A, B, C> Gen.Companion.triple(genA: Gen<A>, genB: Gen<B>, genC: Gen<C>): Gen<Triple<A, B, C>> =
  bind(genA, genB, genC) { a, b, c -> Triple(a, b, c) }
