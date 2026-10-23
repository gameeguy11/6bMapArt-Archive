# Maparts Link — Minecraft 26.2 / 26.3 build

See the [repo root README](../README.md) for commands, upload behavior, and
the release process. This folder is the Fabric mod project targeting
Minecraft **26.2 – 26.3**, built and verified against **26.3**.

## Requirements

- Minecraft **26.2 – 26.3**
- Fabric Loader ≥ 0.19.3 + Fabric API 0.164.0+26.3
- Java 25

## Building

```bash
./build.sh        # or: ./gradlew build
```

The jar lands in `build/libs/maparts-link-<version>.jar`. Gradle needs a JDK
with a compiler — on a JRE-only machine the foojay toolchain resolver
downloads one, or install it yourself.

Toolchain: Loader 0.19.3, Fabric API `0.164.0+26.3`, Loom 1.17.19.
