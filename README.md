# ⚡ Caching Proxy CLI

A command-line interface (CLI) tool built with Java and Spring Boot that starts a caching proxy server. It forwards requests to an origin server, caches the responses in memory, and serves the cached responses for subsequent requests, improving performance and reducing network traffic.

## Key Features

* **Proxy Server:** Starts an HTTP server on a configurable port.
* **Request Forwarding:** Forwards all incoming requests to a specified origin server.
* **In-Memory Cache:** Utilizes Caffeine for high-performance, in-memory caching to speed up repeated requests.
* **Cache Status Header:** Automatically adds an `X-Cache: HIT` or `X-Cache: MISS` header to all responses to indicate whether the response came from the cache or the origin server.
* **Time-to-Live (TTL):** The cache is configured to automatically evict items after a certain period (currently set to 1 minute).

-----

## 🛠️ Technologies 

* **Java 17+**
* **Spring Boot 3**
* **Spring WebFlux** (for non-blocking, reactive I/O)
* **Spring Cache Abstraction**
* **Caffeine Cache** (in-memory cache implementation)
* **Picocli** (for creating the command-line interface)
* **Maven** (for dependency management and build)

-----

## Prerequisites

Before you begin, ensure you have the following software installed:

* **Java Development Kit (JDK)** - Version 17 or higher.
* **Apache Maven** - Version 3.6 or higher.

-----

## How to Build (Generate the Executable)

To compile the project and package everything into a single executable `.jar` file, navigate to the project's root directory and run the following command in your terminal:

```bash
mvn clean package
```

This command will clean any previous builds, compile the code, run tests, and create the file `caching-proxy-0.0.1-SNAPSHOT.jar` inside the `target/` directory.

-----

## 🚀 Run and Use 

The tool has two main commands.

### 1. Starting the Proxy Server

This command starts the server and keeps it running to handle requests.

**Syntax:**

```bash
java -jar target/caching-proxy-0.0.1-SNAPSHOT.jar --port <port_number> --origin <origin_url>
```

**Example Used:**

```bash
java -jar target/caching-proxy-0.0.1-SNAPSHOT.jar --port 3000 --origin https://dummyjson.com
```

The server will start on port 3000 and will forward all requests to `https://dummyjson.com`. The terminal will block, which indicates the server is up and running.


-----

## Example Testing Workflow

After starting the server, open a **new terminal** to test the cache functionality.

**1. First Request (Cache Miss):**
Make a request for a product. The response should include the `X-Cache: MISS` header.

```bash
curl -i http://localhost:3000/products/1
```

```
HTTP/1.1 200 OK
...
X-Cache: MISS
Content-Type: application/json; charset=utf-8
...

{"id":1,"title":"iPhone 9", ...}
```

**2. Second Request (Cache Hit):**
Request the same product again. The response will be almost instantaneous, and the header will indicate a `HIT`.

```bash
curl -i http://localhost:3000/products/1
```

```
HTTP/1.1 200 OK
...
X-Cache: HIT
Content-Type: application/json; charset=utf-8
...

{"id":1,"title":"iPhone 9", ...}
```