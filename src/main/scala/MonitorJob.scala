package kafka_fsm

import akka.actor.{ActorSystem, Props}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

/**
  * Created by zhenhao.li on 22/07/16.
  */
class MonitorJob(configFilePath: String) extends JobSetting(configFilePath){

        val system = ActorSystem(systemName)
        val fsm = system.actorOf(Props(new AlertFSM(topic)), name = "alertActor_for_topic_" + topic)


        implicit val actorSystem = ActorSystem("ReactiveKafka")
        implicit val materializer = ActorMaterializer()

        val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
          .withBootstrapServers("localhost:9092")
          .withGroupId("group1")
          .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

        val messages = Consumer
          .committableSource(consumerSettings, Subscriptions.topics("topic1"))
          .runForeach(x => fsm ! Message(topic))
/*

        messages.onFailure {
            case e: Throwable => e.printStackTrace()
        }
*/
}
