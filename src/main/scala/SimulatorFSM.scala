package kafka_fsm

import akka.actor.{Actor, ActorLogging, ActorSystem, FSM}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.duration._

/**
  * Created by zhenhao.li on 16/09/16.
  */


class SimulatorFSM(producerSettings: ProducerSettings[Array[Byte],String], topic: String) extends  FSM[State, Data] with Actor with ActorLogging{

    implicit val actorSystem = ActorSystem("FSM")
    implicit val materializer = ActorMaterializer()


    startWith(Idle, LastEventTime(System.currentTimeMillis - 5000 ))


    onTransition {
        case Idle -> Active
            => stateData match {
            case LastEventTime(t) => {

                Source (10 to 1800 by 10)
                  .map (t + _)
                  .map (time => time.toString + "," + math.sin(Math.toRadians((time % 3600) /10)).toString)
                  .map {
                      elem =>
                          new ProducerRecord[Array[Byte], String] (topic, elem)
                  }
                  .runWith (Producer.plainSink (producerSettings) )

                log.info ("Stopping event producing")
            }
        }

        case Active -> Idle
            => stateData match {
            case LastEventTime(t) => {

                Source (10 to 2700 by 10)
                  .map (t + _)
                  .map (time => time.toString + "," + math.sin(Math.toRadians((time % 3600) /10)).toString)
                  .map {
                      elem =>
                          new ProducerRecord[Array[Byte], String] (topic, elem)
                  }
                  .runWith (Producer.plainSink (producerSettings) )

                log.info ("Stopping event producing")
            }
        }
    }


    when(Idle, stateTimeout = 1.8 second) {
        case Event(StateTimeout, LastEventTime(t)) =>
            log.info("Starting event producing")
            goto(Active) using LastEventTime(t + 1800)
    }

    when(Active, stateTimeout = 2.7 second) {
        case Event(StateTimeout, LastEventTime(t)) =>
            log.info("Starting event producing")
            goto(Idle) using LastEventTime(t + 2700)
    }



    initialize()


}
