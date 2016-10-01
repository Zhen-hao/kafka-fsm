package kafka_fsm


/**
  * Created by zhenhao.li on 16/09/16.
  */
object EventsRunner {
    def main(args: Array[String]): Unit = {
        val configFilePath = args(0)
        val app = new EventSimulator(configFilePath)
    }

}
