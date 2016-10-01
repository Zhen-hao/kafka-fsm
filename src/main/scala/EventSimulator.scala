package kafka_fsm

import akka.actor.{ActorSystem, Props}
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer

import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

/**
  * Created by zhenhao.li on 16/09/16.
  */
class EventSimulator(configFilePath: String) extends JobSetting(configFilePath) {

    //val system = ActorSystem("simulators")

    implicit val actorSystem = ActorSystem("EventTest")
    implicit val materializer = ActorMaterializer()


    val producerSettings = ProducerSettings(actorSystem, new ByteArraySerializer, new StringSerializer)
          .withBootstrapServers("localhost:9092")

    val eventSimulator = actorSystem.actorOf(Props(new SimulatorFSM(producerSettings, "test-events")), name = "events_generator")

}
