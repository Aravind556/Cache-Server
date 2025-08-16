# Cache Server

A Spring Boot HTTP caching proxy server that sits between clients and origin servers to improve response times and reduce load.

## Features

- **HTTP Proxy**: Forward requests to origin server with transparent caching
- **Smart Cache Keys**: Based on HTTP method, URL, headers, and request body
- **Header Filtering**: Excludes non-essential headers (host, accept-encoding) to improve cache hit rates
- **Cache Management**: Clear cache via REST endpoint or CLI
- **Cache Status**: Responses include `X-Cache: HIT/MISS` header
- **Dual Mode**: Web server or CLI-only cache clearing

## Quick Start

### Start Cache Server
```bash
java -jar cache-server.jar --port=8080 --origin.url=https://api.example.com
```

### Clear Cache (CLI)
```bash
java -jar cache-server.jar --clear-cache --target.port=8080
```

### Clear Cache (HTTP)
```bash
DELETE http://localhost:8080/clear
```

## Configuration

| Parameter | Description | Example |
|-----------|-------------|---------|
| `--port` | Cache server port | `--port=8080` |
| `--origin.url` | Origin server URL | `--origin.url=https://api.example.com` |
| `--target.port` | Target port for CLI cache clear | `--target.port=8080` |
| `--clear-cache` | Enable CLI cache clearing mode | `--clear-cache` |

## Cache Behavior

- **Cache Key**: Generated from HTTP method + URL + filtered headers + request body
- **Cache Hit**: Returns cached response with `X-Cache: HIT` header
- **Cache Miss**: Fetches from origin, caches response, returns with `X-Cache: MISS` header
- **Filtered Headers**: `host` and `accept-encoding` are excluded from cache keys

## API

### Proxy All Requests
```
* /**
```
All HTTP methods are supported and forwarded to the configured origin server.

### Clear Cache
```
DELETE /clear
```
Clears all cached responses.

## Requirements

- Java 17+
- Spring Boot 3.x

## Architecture

- **ProxyController**: Handles incoming HTTP requests
- **ProxyService**: Core caching logic and origin communication
- **CacheKey**: Smart cache key generation
- **CacheClearCli**: CLI-based cache management
- **ResponseWrapper**: Serializable response container

## Example Usage

1. Start cache server proxying to JSONPlaceholder:
```bash
java -jar cache-server.jar --port=3000 --origin.url=https://jsonplaceholder.typicode.com
```

2. Make requests through cache:
```bash
curl http://localhost:3000/posts/1
# X-Cache: MISS (first request)

curl http://localhost:3000/posts/1  
# X-Cache: HIT (cached response)
```

3. Clear cache:
```bash
curl -X DELETE http://localhost:3000/clear
```