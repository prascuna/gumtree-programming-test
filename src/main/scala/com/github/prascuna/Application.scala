package com.github.prascuna

import java.io.File
import java.time.format.DateTimeFormatter

import com.github.prascuna.services.InputLoaderImpl

import scala.util.{Failure, Success}

object Application {

  def main(args: Array[String]): Unit = {
    val file = new File(args(0))
    val Columns = 3 // in a real app this would be taken from configuration

    val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")
    val loader = new InputLoaderImpl(dtf, Columns)

    loader.load(file) match {
      case Success(addressBook) =>

      case Failure(_) =>
        println("Cannot load AddressBook")
    }
  }

}
