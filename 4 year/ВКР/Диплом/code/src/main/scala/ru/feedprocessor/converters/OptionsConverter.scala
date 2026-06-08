package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.Context
import zio.IO

trait OptionsConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Map[String, Boolean]]]
}

final class StubOptionsConverter extends OptionsConverter {
  private val knownCodes = Map(
    "Стальные диски" -> "steel-wheels",
    "Запуск двигателя с кнопки" -> "start-button",
    "Кондиционер" -> "condition",
    "Подушка безопасности водителя" -> "airbag-driver"
  )

  override def convert(context: Context): IO[ApplicationFailure, Result[Map[String, Boolean]]] =
    zio.ZIO.succeed {
      val equipment = context.externalOffer.extras.flatMap(knownCodes.get).map(_ -> true).toMap
      Result.success(equipment)
    }
}

object OptionsConverter {
  def stub: OptionsConverter = new StubOptionsConverter()
}
