# space-x
Android architecture and best practices demo that uses the public SpaceX api


This project was designed with CLEAN architecture principles in mind, unidirectional dependency graphs with transformers between them, inner layers don't know about outer layers, outer layers only know the layer immediately below and only through interfaces by them exposed. Example: the database client and the API client aren't aware of each other, the repository knows only about their public interfaces and provides the bridging logic between both.
The viewmodels only interact with granular interfaces exposed by the repository, the inner workings of repo/cache etc are hidden/abstracted.

Architecture description and common patterns in use:
- mvvm for the UI layers, with a shared viewmodel between the Launches activity and the filter
- the filtering functionality of the shared viewmodel is fully tested for year/success/ ASC-DSC and mixed cases
- live data is used as a way for the activity to keep track of data changes in the view models
- repository pattern abstracting the loading of data from the view models
- this project implements a custom cache feature using Room DB
- integration tests of the interaction with the data access objects (DAOs) are included
- any attempt to load data from the repository will query the cache first before dispatching requests to the network
- cache older than the provided configuration will be discarded
- any new network requests will update the local cache
- this project provides unit tests and integration tests
- Espresso and Idling resources were used for the UI end to end tests
- Mock Web Server was used to provide clear recorded responses (success, failures and mixed success/failures) in the end to end tests
- Mock Web server was used to provide integration tests for the network layer (Retrofit + Gson + RxJava)
- Repository is tested at 100% coverage
- tests make use of environment configurations through product flavours (mock and prod) and Mock web server for some integration tests

- dependency injection is done through hilt in the app, and manually in the tests (for granular individual dependency configuration per test)
- RxJava was the choice for async operations
- MockK was the mocking library of choice
- all of the challenge requirements are implemented


<img width="274" alt="spacex01" src="https://user-images.githubusercontent.com/4844875/118580736-4d955680-b788-11eb-96f5-2c3c0b9e6e3c.PNG">

<img width="274" alt="spacex03" src="https://user-images.githubusercontent.com/4844875/118580773-6140bd00-b788-11eb-88a1-5843cf2b7369.PNG">

<img width="274" alt="spacex04" src="https://user-images.githubusercontent.com/4844875/118580778-63a31700-b788-11eb-90c7-809725a8f29a.PNG">
