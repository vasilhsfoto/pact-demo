# Order service - Consumer side

### Idea in general:
- Consumer creates the Pacts (contracts) - better via UT using the DSL language 
- Runs each pact against a mock server that runs by the pact library
- Once pact tests are run, contracts are written under /target/pacts directory
- u need a maven plugin that will run on e.g build phase `verify` to upload the pacts in pact broker

### Logic in steps using the Pact Library

- 1 test class per provider
- 1 @Pact method per pact u want later your provider to verify. This return the RequestResponsePact which is the contract
- You bind one @Test per @Pact method so that consumer side can run the test against a mock server provider
- For this to make sense, we need to use production code e.g a ProvideServiceNameClient
- Once it is done tests are in target and using a maven plugin, pacts are uploaded to broker

Note: https://docs.pact.io/implementation_guides/jvm/consumer/junit5