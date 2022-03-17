# Customer service - Provider side

### Idea in general:
- the server (provider) is up and the service layer is mocked. No need to have component-tests here. The focus is on the
  API breaking changes not service/repo layers etc
- The trigger and verification of a pact is happening by the pact library itself
- No surprisingly adding a new field doesn't break the test. Whereas remove/update fields it does. Also value comparison
  of fields is happening which probably doesn't make much sense

### Logic in steps using the Pact library:
- 1 test class per consumer
- 1 @state per pact/contract
- you configure the target (in our case the httpClient) so that pact can trigger the calls to uris
- you spin up the server using SpringBoot (note: also mocking can be used with target MockMVC if u want to go
  lightweight)
- the target is configured once per test method (this could have been set up once per test class...)
- all the configuration per test case happens in @State... usually setting up mocks
- The tests run using Junit 5 @TestTemplate by pact which runs for each pact the test method
- No need for a plugin here, the publication of the verification results happens per test class

**Note:**
https://docs.pact.io/implementation_guides/jvm/provider/junit5
https://docs.pact.io/implementation_guides/jvm/provider/junit5spring