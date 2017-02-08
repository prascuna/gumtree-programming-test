package com.github.prascuna.models

import java.time.LocalDate

import com.github.prascuna.models.AddressBook.GenderEnum.Gender

object AddressBook {

  object GenderEnum {

    sealed abstract class Gender(val label: String)

    case object Male extends Gender("Male")

    case object Female extends Gender("Female")

    def fromString(value: String): Option[Gender] =
      List(Male, Female).find(_.label == value)

  }

  case class Entry(name: String, gender: Gender, dob: LocalDate)

}
