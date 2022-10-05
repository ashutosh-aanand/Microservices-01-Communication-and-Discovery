# Flowchart

![flowchart](./%23img/flowchart.jpg)

java -jar jar_file_name.jar --server.port=port_to_use 
-> to run multiple instances on different ports


# Fault tolerance and resilience

Fault tolerance
- given an app, if there is a fault, what is the impact
- how much tolerance does the app have to fault
- how much is the impact of a fault

Resilience
- how many faults can a system tolerate, before its
- how much a sys can bounce back from a fault
- is there a mechnm by which the sys can correct itself

these 2 must go together

Calling an external API
- omdbapi

Eureka Dashboard url:
`http://localhost:8761`

Hystrix Dashboard url:
`http://localhost:8081/hystrix`

To get hystrix stream:
`http://localhost:8081/actuator/hystrix.stream`

> [bug] hystrix dashboard is currently not able to connect to our app. Will fix this later.

