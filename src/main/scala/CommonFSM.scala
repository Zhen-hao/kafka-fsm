package kafka_fsm


/**
  * Created by zhenhao.li on 16/09/16.
  */
// states
sealed trait State
case object Idle extends State
case object Active extends State
// data
sealed trait Data
case object Uninitialized extends Data
final case class Todo(action: Unit) extends Data

case class LastEventTime(time: Long) extends Data


// messages
case class Message(topic: String)

