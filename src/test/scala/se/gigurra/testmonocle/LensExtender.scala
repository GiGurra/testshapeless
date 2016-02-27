package se.gigurra.testmonocle

import monocle._

import scala.language.higherKinds
import scala.language.implicitConversions

case class LensExtender[ObjectType, FieldType, NextCompanionType](lens: Lens[ObjectType, FieldType], c: NextCompanionType) {
  def apply[T](f: NextCompanionType => Lens[FieldType, T]): Lens[ObjectType, T] = {
    lens ^|-> f(c)
  }
}

case class RichCaseClass[ObjectType, CompanionType](o: ObjectType, c: CompanionType) {
  def set[FieldType](lensGetter: CompanionType => Lens[ObjectType, FieldType], value: FieldType): ObjectType = {
    lensGetter(c).set(value)(o)
  }
}

object LensExtender {

  implicit def ext[ObjectType, FieldType](lens: Lens[ObjectType, FieldType])
                                         (implicit c: Companion[FieldType]): LensExtender[ObjectType, FieldType, c.C] = {
    LensExtender(lens, Companion.getCompanion[FieldType])
  }

  implicit def caseClass2RichCaseClass[ObjectType](o: ObjectType)
                                                  (implicit c: Companion[ObjectType]): RichCaseClass[ObjectType, c.C] = {
    RichCaseClass(o, Companion.getCompanion[ObjectType])
  }

}
