package helios.optics

import arrow.Kind
import arrow.core.Option
import arrow.core.left
import arrow.core.right
import arrow.data.getOption
import arrow.data.k
import arrow.extension
import arrow.optics.Lens
import arrow.optics.Optional
import arrow.optics.Traversal
import arrow.optics.typeclasses.At
import arrow.optics.typeclasses.Each
import arrow.optics.typeclasses.Index
import arrow.typeclasses.Applicative
import helios.core.JsArray
import helios.core.JsObject
import helios.core.Json

@extension
interface JsObjectIndexInstance : Index<JsObject, String, Json> {
  override fun index(i: String): Optional<JsObject, Json> = Optional(
    getOrModify = { it.value[i]?.right() ?: it.left() },
    set = { map, js ->
      JsObject(map.value.map { (key, oldValue) -> key to if (key == i) js else oldValue }.toMap())
    }
  )
}

@extension
interface JsObjectAtInstance : At<JsObject, String, Option<Json>> {
  override fun at(i: String): Lens<JsObject, Option<Json>> = Lens(
    get = { it.value.getOption(i) },
    set = { js, optJs ->
      optJs.fold({
        js.copy(value = js.value - i)
      }, {
        js.copy(value = js.value + (i to it))
      })
    }
  )

}

@extension
interface JsObjectEachInstance : Each<JsObject, Json> {
  override fun each() = object : Traversal<JsObject, Json> {
    override fun <F> modifyF(
      FA: Applicative<F>,
      s: JsObject,
      f: (Json) -> Kind<F, Json>
    ): Kind<F, JsObject> = FA.run {
      s.value.k().traverse(FA, f).map { JsObject(it.toMap()) }
    }
  }
}

@extension
interface JsArrayEachInstance : Each<JsArray, Json> {
  override fun each() = object : Traversal<JsArray, Json> {
    override fun <F> modifyF(
      FA: Applicative<F>,
      s: JsArray,
      f: (Json) -> Kind<F, Json>
    ): Kind<F, JsArray> = FA.run {
      s.value.k().traverse(FA, f).map { JsArray(it.toList()) }
    }
  }
}

@extension
interface JsArrayIndexInstance : Index<JsArray, Int, Json> {
  override fun index(i: Int): Optional<JsArray, Json> = Optional(
    getOrModify = { it.value.getOrNull(i)?.right() ?: it.left() },
    set = { jsArr, js -> jsArr.copy(jsArr.value.mapIndexed { index, t -> if (index == i) js else t }) }
  )
}