# bendy-lib
FabricMC library

setup with gradle:

```groovy
dependencies {
  (...) 
    modImplementation "io.github.kosmx.bendylib:bendy-lib:${project.bendylib_version}"
    include "io.github.kosmx.bendylib:bendy-lib:${project.bendylib_version}"
    //you can find the latest version in GitHub packages
    //You may use com.kosmx.bendylib group id if you are using jcenter (PLS don't)
}
```
You can use the artifact from `jcenter`, I'll move to Maven Central soon

designed to be able to swap and bend cuboids.

The api provides a way to swap a cuboid with priorities, to be multi-mod compatible
(bend like in Mo'bends)

to swap, you have to create a class from MutableModelPart, and implement the methods.

You don't have to use existing bendableCuboid objects, you can create your own, BUT it's highly recommend (it's a lot's of work to code a bendable stuff)

The test mod (an older and modified version of [Emotecraft](https://github.com/kosmx/emotes))

Sorry for this not finished documentation...
You can find me on the Fabric discord server and on the Emotecraft discord server

and an example image:D  
![example](https://raw.githubusercontent.com/KosmX/bendy-lib/dev/example.png)  
  
The release branch contains the source of the latest release.
