# PixDay

PixDay is a simple Android app that provides an endless scroll of a user searched images result.
User inputs a keyword on the search bar and PixDay would display an endless scroll of distinct images from ShutterStock which is related to the search.

PixDay is developed for simplicity, speed and optimization. There are different section of the code worth looking into but before we delve into each sections,
lets quickly look at the libraries/dependencies used on PixDay.


## List of Libraries/Dependencies Used.
* kotlinx-coroutines-android and kotlinx-coroutines-core: This is used instead of RxAndroid to make multithreaded network call, it gives a nice implementation of suspend which is similar to async - await on other programming languages.
A coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously, coroutines help to manage long-running tasks that might
otherwise block the main thread and cause your app to become unresponsive.

* paging-runtime-ktx: This used instead of implementing RecyclerView.addOnScrollListener(), Paging has a configurable RecyclerView adapters that automatically request data as the user scrolls toward the end of the loaded data.
Paging helps you load and display pages of data from a larger dataset from local storage or over network.

* retrofit: This is used to make API/network requests. Retrofit is a type-safe REST client for Android, Java and Kotlin. It provides a framework for interacting with APIs and sending network requests with OkHttp.

* picasso: This is used to load image urls into image views. Picasso allows for hassle-free image loading and handles ImageView recycling and image download cancellation in a RecyclerView adapter.


## The different section of the code worth looking into:
The Android Architecture Component flow is used as a pattern for this project. Take a look at the project directory structure. Android architecture components are a collection of libraries that help you design robust, testable, and maintainable apps.
Start with classes for managing your UI component lifecycle and handling data persistence.

* The api section: on the api directory section we have [OAuthInterceptor], [PhotoResponse] and [ShutterStockService]. This are setup to easily make API/network request to the ShutterStock Endpoint. Because the ShutterStock endpoint requires an Authorization token,
the OAuthInterceptor is created to intercept each network request, adds the ShutterStock authorization token obtained from the ShutterStock developer web account and proceed with the request.
```
//Intercepts the current request and adds an accessToken to the chain.
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
```

* The data section: This section of the project interact with the api section and exposes data to other part of the application using the [ShutterStockPagingSource] and [ShutterStockRepository] of {getSearchResultStream} method.
```
class ShutterStockRepository(
    private val service: ShutterStockService
) {
    fun getSearchResultStream(query: String): Flow<PagingData<Photo>> {
        //We don't need a placeholders so we're setting it to false
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ShutterStockPagingSource(service, query) }
        ).flow
    }

    companion object {
        //We're getting 30 photos per page to prevent querying the api often.
        const val NETWORK_PAGE_SIZE = 30
    }
}
```

* The ui section: This is responsible for organizing, maintaining and displaying data obtained from the data section to the user, it loads data into [PhotosAdapter] and handles errors and retry state with the [PhotoLoadStateAdapter].
The [SearchRepositoriesViewModel] handles data querying and persistence, the method {searchPhoto} prevents searching for the same keyword multiple times and returns search result in kotlin Flow.
```
fun searchPhoto(queryString: String): Flow<PagingData<Photo>> {
        //We're preventing users from making the same query on multiple times.
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Photo>> = repository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
```

## Test
For simplicity sake we make use of the Injection object pattern instead of Dagger. The project has different build variant namely: release, debug and staging. The release variant has the Injection object put together for when the developer is ready to make an app release.
* Note: The release variant requires a ShutterStock OAuth access token in its [Injection] object, which can be obtained from the ShutterStock Developer web platform *

The debug variant also has its Injection object responsible for running the app in a debug or development state.
* Note: The debug variant requires a ShutterStock OAuth access token in its [Injection] object, which can be obtained from the ShutterStock Developer web platform *

The staging variant is where we run all test (Unit and Integrated) for business logic and UI elements. Developer should switch to this build variant for proper and smooth test environment to run all test. This variant has its Injection object prepared to enhance automated test.
* Note: No ShutterStock OAuth access token is required for this variant*


## Todo
This areas is worth exploring to better improve the project.
* Offline first approach: The offline first feature would fetch data from the endpoint and stores it on a database (Room), since data from database are displayed on the ui, more data is fetched from the endpoint and appended to the database when all data on database is displayed (i.e. user is almost at the end of the list).
Introduction of Room persistence and the Paging experimental class Mediator to the project would be instrumental in achieving the offline first feature.
* If the project becomes large the need to migrate the use of Injection object to Dagger would be resourceful.
* More test cases can be written to cover more edge cases to improve app performance and resolve possible loop hole that might introduce bugs.
* Improve UI flow, by introducing a gallery (thumbnail and preview) user interface.