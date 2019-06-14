package helios.typeclasses

import arrow.core.Either
import arrow.higherkind
import helios.core.DecodingError
import helios.core.Json

@higherkind
interface Decoder<out A> {
  fun decode(value: Json): Either<DecodingError, A>
}