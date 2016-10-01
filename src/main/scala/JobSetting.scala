package kafka_fsm

import java.io.File
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
  * Created by zhenhao.li on 16/09/16.
  */
class JobSetting(configFilePath: String) extends Serializable {

    val log = LoggerFactory.getLogger(this.getClass.getName)
    val config = ConfigFactory.parseFile(new File(configFilePath))

    val topic = config.getString("kafka-fsm.kafka.read.topic")
    val brokerList = config.getString("kafka-fsm.kafka.read.broker-list")
    val zooKeeperHost = config.getString("kafka-fsm.kafka.read.zooKeeper-host")
    val groupId = config.getString("kafka-fsm.kafka.read.group-id")

    val systemName = config.getString("kafka-fsm.actor.system-name")

}
