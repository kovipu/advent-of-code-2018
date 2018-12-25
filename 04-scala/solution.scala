import java.text.SimpleDateFormat
import java.util.Date

import scala.io.Source

case class GuardData(
                      id: String = "",
                      totalSlept: Int = 0,
                      sleepSchedule: Array[Int] = Array.fill[Int](60)(0)
                    )

case class Accumulator(
                      guardOnShift: String = "",
                      fellAsleep: Int = 0,
                      guards: Map[String, GuardData] = Map()
                      )

def parseDate(line: String): Date = {
  val timestamp = line.slice(1, 17)
  return new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(timestamp)
}

def parseGuardId(line: String): String = "#(\\S*) ".r
  .findFirstMatchIn(line)
  .get
  .group(1)

def parseMinutes(line: String): Int = ":(.*)]".r
  .findFirstMatchIn(line)
  .get
  .group(1)
  .toInt

val guardMostAsleep = Source.fromFile("input.txt")
  .getLines
  .toList
  .sortWith((a, b) => parseDate(a).before(parseDate(b)))
  .foldLeft(Accumulator())((acc, line) => {
    line match {
      case l if l.endsWith("begins shift") =>
        Accumulator(parseGuardId(line), acc.fellAsleep, acc.guards)

      case l if l.endsWith("falls asleep") => {
        Accumulator(acc.guardOnShift, parseMinutes(line), acc.guards)
      }

      case l if l.endsWith("wakes up") => {
        val wokeUp = parseMinutes(line)

        val guardData = acc.guards
          .getOrElse(acc.guardOnShift, GuardData())

        val newTotalSlept = guardData.totalSlept + (wokeUp - acc.fellAsleep)


        val newSleepSchedule = guardData.sleepSchedule
          .zipWithIndex
          .map { case (value: Int, key: Int) => if (acc.fellAsleep - 1 < key && key < wokeUp) value + 1 else value }

        val newGuards = acc.guards + (acc.guardOnShift -> GuardData(acc.guardOnShift, newTotalSlept, newSleepSchedule))
        Accumulator(acc.guardOnShift, 0, newGuards)
      }
      case _ => throw new Exception("unable to parse line")
    }
  })
  .guards
  .valuesIterator
  .reduceLeft((acc: GuardData, guard: GuardData) => if (acc.totalSlept < guard.totalSlept) guard else acc)

val sleepiestMinute = guardMostAsleep.sleepSchedule
    .zipWithIndex
    .foldLeft((0, 0))((acc: (Int, Int), line: (Int, Int)) => if (acc._1 < line._1) line else acc)

println(sleepiestMinute._2 * guardMostAsleep.id.toInt)
