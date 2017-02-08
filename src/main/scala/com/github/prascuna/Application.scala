package com.github.prascuna

import java.io.File
import java.time.format.DateTimeFormatter

import com.github.prascuna.models.AddressBook.GenderEnum.Male
import com.github.prascuna.services.{AnswersServiceImpl, InputLoaderImpl}

import scala.util.{Failure, Success}

object Application {

  def main(args: Array[String]): Unit = {
    if(args.size != 1) {
      showUsage
    } else {
      val file = new File(args(0))
      val Columns = 3 // in a real app this would be taken from configuration

      val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")
      val loader = new InputLoaderImpl(dtf, Columns)

      loader.load(file) match {
        case Success(addressBook) =>
          val answersService = new AnswersServiceImpl(addressBook)

          val noOfMales = answersService.countByGender(Male)

          val oldestPerson = answersService.oldestPerson()

          val daysDifference = answersService.ageDifference("Bill McKnight", "Paul Robinson")

          val answers = List(
            printableAnswer(1, noOfMales.toString),
            printableAnswer(2, oldestPerson.name),
            daysDifference match {
              case Success(days) =>
                printableAnswer(3, days.toString)
              case Failure(e) =>
                printableAnswer(3, "Error: " + e.getMessage)
            }
          )

          answers.foreach(println)

        case Failure(_) =>
          println("Cannot load AddressBook")
      }
    }
  }

  private def printableAnswer(questionNumber: Int, answer: String) = s"$questionNumber. $answer"

  private def showUsage = println(
    """No such file or directory
      |Please run the application with the following command:
      |
      |sbt "run <filename>"
    """.stripMargin)


}
