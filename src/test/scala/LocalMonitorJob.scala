package kafka_fsm


import akka.actor.{ActorSystem, Props}
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

/**
  * Created by zhenhao.li on 25/07/16.
  */
object LocalMonitorJob {
    def main(args: Array[String]) ={
        val system = ActorSystem("Alerting")
        val fsm = system.actorOf(Props[AlertFSM], name = "alertActor")

        //fsm ! Message


        implicit val actorSystem = ActorSystem("ReactiveKafka")
        implicit val materializer = ActorMaterializer()

        val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
          .withBootstrapServers("localhost:9092")
          .withGroupId("group1")
          .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

        val messages = Consumer
          .committableSource(consumerSettings, Subscriptions.topics("topic1"))
          .runForeach(x => fsm ! Message("test"))

        messages.onFailure {
            case e: Throwable => e.printStackTrace()
        }
    }
}
