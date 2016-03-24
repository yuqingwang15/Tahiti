# Tahiti

Tahiti is a simple chatting service.

## Getting Started

### IDE

Tahiti includes JetBrains Intellij IDEA project files. You can directly use IDEA to open the project.

### Install ProtoBuf

Tahiti uses ProtoBuf as a data serializer and deserializer between clients and the server. The general step to use ProtoBuf is:

1. Write `.proto` file which defines the schema of the data
2. Generate source code from `.proto` by using ProtoBuf compiler `protoc`
3. Use the generated class to serialize and deserialize data

`.proto` is already contained in this repository, however all generated stuff (including generated source code and compiled files) is not included, which means you need to complete step 2 before you could do anything else.

The good news is that we have already integrated step 2 into the maven goal `compile`, so you only need to execute `mvn compile` to generate the source code of the protocol serializer. But first of all you need to install the ProtoBuf compiler.

Documentation: https://github.com/google/protobuf#protocol-compiler-installation

Notice: You need to download or compile protoc version 3.0.

> For OS X: You can use [homebrew](http://brew.sh/) to install: `brew install protobuf --devel --c++11`

### Setting Environment Variables for Maven

`Tahiti.Protocol` will invoke `protoc` based on the path from the environment variable. In Intellij IDEA, you could set up the env var as follows:

1. Open Maven window by clicking `View` menu -> `Tool Windows`

2. In Maven window, right click `tahiti.protocol` -> `Lifecycles` -> `compile`, choose `Create [tahiti.protocol] (compile)` to create a new profile

3. Select `Runner` tab in the new window and uncheck `Use project settings`

4. Add a environment variable called `PROTOC_PATH` (case sensitive), whose value is the path of your `protoc` binary, e.g, `/usr/local/bin/protoc`.

5. Save

### Compile Protocol Serializer

Now you can start to generate the serializer source code by executing your newly created profile.

Then should find out that you can use `octoteam.tahiti.protocol.SocketMessageProtos` now, which is the protocol serializer and deserializer.

### Start Server

Run `Server` configuration in Intellij IDEA, or:

```bash
cd Tahiti/Server
mvn exec:exec
```

### Start Client

Run `Client` configuration in Intellij IDEA, or:

```bash
cd Tahiti/Client
mvn exec:exec
```
