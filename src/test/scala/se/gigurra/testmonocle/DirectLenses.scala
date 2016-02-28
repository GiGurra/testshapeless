package se.gigurra.testmonocle

import monocle._
import monocle.macros.GenLens

import scala.language.implicitConversions

object DirectLenses {

  case class Lensed[ObjectType, GenLensType <: GenLens[ObjectType]](o: ObjectType, genLens: GenLensType) {
    def set[FieldType](fieldSelector: GenLensType => Lens[ObjectType, FieldType], v: FieldType): ObjectType = {
      val lens = fieldSelector(genLens)
      lens.set(v)(o)
    }
  }

  implicit def directLensSetter[ObjectType](o: ObjectType) : Lensed[ObjectType, GenLens[ObjectType]] = {
    Lensed(o, new GenLens[ObjectType])
  }

}
