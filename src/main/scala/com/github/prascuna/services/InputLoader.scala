package com.github.prascuna.services

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.github.prascuna.models.AddressBook.{Entry, GenderEnum}
import com.github.prascuna.services.InputLoader.WrongInputFileException

import scala.util.{Failure, Try}

trait InputLoader {
  /**
    * Loads the AddressBook from a given file
    * @param file
    * @return The list of entries in that file
    */
  def load(file: File): Try[List[Entry]]
}

object InputLoader {

  case object WrongInputFileException extends RuntimeException("Input file contains errors")

}

class InputLoaderImpl(dtf: DateTimeFormatter, columns: Int) extends InputLoader {

  override def load(file: File): Try[List[Entry]] =
    Try {
      io.Source.fromFile(file).getLines().map { line =>
        val cols = line.split(",")
        // I want to intentionally explode in case of wrong input
        require(cols.size == columns)
        val gender = GenderEnum.fromString(cols(1).trim).get
        val dob = LocalDate.parse(cols(2).trim, dtf)

        Entry(cols(0).trim, gender, dob)

      }.toList
    }.recoverWith {
      case _ => Failure(WrongInputFileException)
    }
}
