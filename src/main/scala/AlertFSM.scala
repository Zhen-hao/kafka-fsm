package kafka_fsm

import akka.actor.{Actor, ActorLogging, FSM}
import net.gpedro.integrations.slack.{SlackApi, SlackMessage}

import scala.concurrent.duration._



/**
  * Created by zhenhao.li on 21/07/16.
  **/



class AlertFSM(topic: String) extends  FSM[State, Data] with Actor with ActorLogging {

    val watchingTopic = topic

    startWith(Idle, Uninitialized)

    when(Idle) {
        case Event(Message(`watchingTopic`), Uninitialized) =>
            log.info("stream flow in topic " + watchingTopic + " has been detected, enter active state now")
            val apiHandler = new SlackApi("put your slack web hook here")
            apiHandler.call(new SlackMessage("Monitor on topic " + watchingTopic, "Stream flow in topic " + watchingTopic + " has been detected. No action is required"))
            goto(Active) using Uninitialized

    }

    when(Active, stateTimeout = 5 minute) {
        case Event(StateTimeout, Uninitialized) =>
            log.info("stream has stopped, send alert and enter idle state now")
            val apiHandler = new SlackApi("put your slack web hook here")
            apiHandler.call(new SlackMessage("Monitor on topic " + watchingTopic, "Output stream stopped, please check"))
            goto(Idle) using Uninitialized

        case Event(Message(`watchingTopic`), Uninitialized) =>
            log.info("message received from topic " + watchingTopic)
            goto(Active) using Uninitialized
    }

    initialize()
}

