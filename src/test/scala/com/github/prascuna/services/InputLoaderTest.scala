package com.github.prascuna.services

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.github.prascuna.models.AddressBook.Entry
import com.github.prascuna.models.AddressBook.GenderEnum._
import com.github.prascuna.services.InputLoader.WrongInputFileException
import org.scalatest.{Matchers, path}

import scala.util.{Failure, Success}

class InputLoaderTest extends path.FunSpec with Matchers {

  describe("InputLoader") {
    val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")
    val inputLoader = new InputLoaderImpl(dtf, 3)

    describe("if there are no error during the parsing") {
      val inputFile = new File(getClass.getClassLoader.getResource("sample.csv").toURI)
      it("should return a Success[List[sample.csv.Entry]]") {
        inputLoader.load(inputFile) shouldBe Success(
          List(
            Entry("Paul Robinson", Male, LocalDate.parse("15/01/85", dtf)),
            Entry("Gemma Lane", Female, LocalDate.parse("20/11/91", dtf))
          )
        )
      }
    }
    describe("and there is at least one error during the parsing") {
      describe("and the error is a wrong number of columns") {
        val inputFile = new File(getClass.getClassLoader.getResource("brokensample-columns.csv").toURI)
        it("should return a Failure") {
          inputLoader.load(inputFile) shouldBe Failure(WrongInputFileException)
        }
      }
      describe("and the error is a mispelled gender") {
        val inputFile = new File(getClass.getClassLoader.getResource("brokensample-gender.csv").toURI)
        it("should return a Failure") {
          inputLoader.load(inputFile) shouldBe Failure(WrongInputFileException)
        }
      }
      describe("and the error is an unparsable date") {
        val inputFile = new File(getClass.getClassLoader.getResource("brokensample-date.csv").toURI)
        it("should return a Failure") {
          inputLoader.load(inputFile) shouldBe Failure(WrongInputFileException)
        }
      }
    }
  }

}
