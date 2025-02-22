# ReactXposed

An [Xposed] module which captures traffic going over the React Native Bridge
and dumps it to logcat.

## motivation

Inspecting a running application often gives you more information faster than static analysis. This
is especially true for React Native where things like network requests, storage, bluetooth, error
reporting and auth are often implemented in "native" code (JVM) and the UI is implemented in React.
Crossing the bridge is necessary for the app to do useful things. Traffic on the bridge is usually a
high level representation of user intent and responses from the underlying system.

This allows for dynamically instrumenting android apps without modification to the installed
binary.

## on deck

Currently, this logs bridge traffic going from RN to native. It does not do a good job of logging
events emitted from native and promises resolved or rejected in native.


[Xposed]: https://github.com/LSPosed/LSPosed
