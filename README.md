# Party participant presence

## Problem
### Problem Description:
Design and build the following party participant presence service.
Assume we have a mobile app that allows app users to join a party and then leave a party. The party screen shows the party participants' profile pictures (ie their presence). We want a service that will manage the party members' presence in order to communicate any changes to the mobile apps real-time to achieve the following behaviour.

### Required Behaviour:
When another person joins the same party I am in, I will see his profile pic pop up on my view of the party.
Someone in the party leaves the party, I will see his profile pic disappear from my view.
A person leaves a party when:
- The person taps the leave button to leave the party (explicit intent to leave) OR
- He is disconnected for a period of time. For example, he puts his phone on airplane mode, after a grace period of 2 mins, he is considered offline. (implicit intent to leave)

### Deliverables:
- Describe this backend service using diagrams.
- Design the API using protobuf and build the grpc micronaut service.

- Please submit system design 1 project.
and a zip containing the micronaut


## Resolution

I'm not sure if I should build all the service or only the grpc API only, but I've tried to build all the application end to end.

This is the design:

![App layers](doc/diagram.png?raw=true "Service design")

### Running

1. run docker-compose:

    docker-compose up
    
2. run app

    ./gradlew run

3. run tests

    ./gradlew test


### Pendings

There are some TODOs that I couldn't end because of the timebox that I set myself (7 hours):

- [ ] implement persistence w/micronaut data
- [ ] add logging
- [ ] add DDL w/flyway
- [ ] add more unit testing for getting an acceptable coverage & utility
- [ ] add integration tests
- [ ] add Dockerfile & build it in docker-compose
- [ ] add metrics (eg: with prometheus)
- [ ] implement grpc client app
- [ ] implement rest layer
- [ ] add Security layer
- [ ] abstract UserRepository into another API call
- [ ] abstract PartyRepository into another API call
