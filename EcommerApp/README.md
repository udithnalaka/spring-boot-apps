# Getting Started
SpringBoot application for a simple Order service using CompletableFuture for Async calls.
This will execute the CompletableFuture wrapped Object in different threads and then combine the results before returning back.


### Reference Documentation

TODO

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

