package com.github.prascuna.services

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.github.prascuna.models.AddressBook.Entry
import com.github.prascuna.models.AddressBook.GenderEnum.{Female, Male}
import com.github.prascuna.services.AnswersService.PersonNotFound
import org.scalatest.{Matchers, path}

import scala.util.{Failure, Success}

class AnswersServiceTest extends path.FunSpec with Matchers with AnswersServiceTestFixtures {
  describe("AnswerService") {
    val answersService = new AnswersServiceImpl(addressBook)

    describe("when counting by gender") {
      it("should return 3 Males") {
        answersService.countByGender(Male) shouldBe 3
      }
      it("should return 2 Females") {
        answersService.countByGender(Female) shouldBe 2
      }
    }

    describe("when counting the oldest person") {
      it("should return the one with the earliest date of birth") {
        answersService.oldestPerson() shouldBe Entry("Wes Jackson", Male, LocalDate.parse("14/08/74", dtf))
      }
    }

    describe("when calculating the age difference between two persons") {
      describe("if both persons are found") {
        it("should return the number of days between them") {
          answersService.ageDifference("Bill McKnight", "Paul Robinson") shouldBe Success(2862)
        }
      }
      describe("if one of the persons is not found ") {
        it("should return a Failure") {
          answersService.ageDifference("Bill McKnight", "Jimmy Robinson") shouldBe Failure(PersonNotFound("Jimmy Robinson"))
        }
      }
    }
  }
}

trait AnswersServiceTestFixtures {
  val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")

  val addressBook = List(
    Entry("Bill McKnight", Male, LocalDate.parse("16/03/77", dtf)),
    Entry("Paul Robinson", Male, LocalDate.parse("15/01/85", dtf)),
    Entry("Gemma Lane", Female, LocalDate.parse("20/11/91", dtf)),
    Entry("Sarah Stone", Female, LocalDate.parse("20/09/80", dtf)),
    Entry("Wes Jackson", Male, LocalDate.parse("14/08/74", dtf))
  )
}