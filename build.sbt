
name := "kafka-fsm"

version := "1.0"

scalaVersion := "2.11.8"

oneJarSettings
mainClass in oneJar := Some("kafka_fsm.MonitorJob")

libraryDependencies +=
  "com.typesafe.akka" %% "akka-actor" % "2.4.10"

libraryDependencies += "net.gpedro.integrations.slack" % "slack-webhook" % "1.1.2"

libraryDependencies += "com.typesafe.akka" % "akka-stream-kafka_2.11" % "0.11"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.21"

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"