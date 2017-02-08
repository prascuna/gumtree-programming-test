package com.github.prascuna.services

import java.time.LocalDate

import com.github.prascuna.models.AddressBook.Entry
import com.github.prascuna.models.AddressBook.GenderEnum.Gender

import scala.util.Try

trait AnswersService {
  /**
    * Counts the number of entries for the given gender
    *
    * @param gender
    * @return
    */
  def countByGender(gender: Gender): Int

  /**
    * Retrieves the oldest person
    *
    * @return
    */
  def oldestPerson(): Entry

  /**
    * Searches for the given names and calculate the age difference in days
    *
    * @param nameA
    * @param nameB
    * @return
    */
  def ageDifference(nameA: String, nameB: String): Try[Long]
}

object AnswersService {

  case object PersonNotFound extends RuntimeException("Person Not Found")

}

class AnswersServiceImpl(addressBook: List[Entry]) extends AnswersService {
  override def countByGender(gender: Gender): Int =
    Try(addressBook.groupBy(_.gender)(gender).size).getOrElse(0)

  override def oldestPerson(): Entry = {
    implicit val localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)
    implicit val ordering = Ordering.by[Entry, LocalDate](element => element.dob)
    addressBook.min
  }

  override def ageDifference(nameA: String, nameB: String): Try[Long] = ???
}
