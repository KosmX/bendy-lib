# bendy-lib
FabricMC library

setup with gradle:

```groovy
dependencies {
  (...)
	modImplementation "com.kosmx.bendylib:bendy-lib:${project.bendylib_version}"
	include "com.kosmx.bendylib:bendy-lib:${project.bendylib_version}"
    //you can find the latest version in GitHub packages
}
```

designed to be able to swap and bend cuboids.

The api provides a way to spaw a cuboid with priorities, to be multi-mod compatible
(bend like in Mo'bends)

to swap, you have to create a class from MutableModelPart, and implement the methods.

You don't have to use existing bendableCuboid objects, you can create your own, BUT it's highly recommend (it's a lot's of work to code a bendable stuff)

The test mod (an older and modified version of [Emotecraft](https://github.com/kosmx/emotes))

Sorry for this not finished documentation...
You can find me on the Fabric discord server and on the Emotecraft discord server

and an example image:D  
![example](https://raw.githubusercontent.com/KosmX/bendy-lib/master/example.png)  
  
The release branch contains the source of the latest release.
