package se.gigurra.testmonocle

import monocle._
import monocle.macros.GenLens

import scala.language.implicitConversions

object DirectLenses {

  implicit class LensedCaseClass[ObjectType](o: ObjectType) {

    private def getLens[FieldType](fieldSelector: GenLens[ObjectType] => Lens[ObjectType, FieldType]): Lens[ObjectType, FieldType] = {
      val genLens = GenLens[ObjectType]
      val lens = fieldSelector(genLens)
      lens
    }

    def set[FieldType](fieldSelector: GenLens[ObjectType] => Lens[ObjectType, FieldType], v: FieldType): ObjectType = {
      getLens(fieldSelector).set(v)(o)
    }

    def get[FieldType](fieldSelector: GenLens[ObjectType] => Lens[ObjectType, FieldType]): FieldType = {
      getLens(fieldSelector).get(o)
    }

    def modify[FieldType](fieldSelector: GenLens[ObjectType] => Lens[ObjectType, FieldType], f: FieldType => FieldType): ObjectType = {
      getLens(fieldSelector).modify(f)(o)
    }

  }

}
