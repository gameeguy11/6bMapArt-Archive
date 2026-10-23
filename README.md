# Maparts Link

A Fabric mod that uploads your maparts to
**[6b-maparts.com](https://6b-maparts.com)** straight from the game. Stand in
front of the wall, run one command, done — no screenshots, no cropping.

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) and the
   [Fabric API](https://modrinth.com/mod/fabric-api) mod for your Minecraft
   version.
2. Download the jar for your Minecraft version from the
   [Releases page](../../releases).
3. Drop it in your `mods/` folder and start the game.

| Release tag | For Minecraft | Java |
| --- | --- | --- |
| `mc1.21.4-1.21.11-v…` | 1.21.4 – 1.21.11 | 21 |
| `mc26.1+-v…` | 26.2 – 26.3 | 25 |

Client-side only — nothing to install on a server, nothing to configure.

## How to use

**1. Link your account (one time).** On
[6b-maparts.com](https://6b-maparts.com), open the login page and pick
**Link Minecraft** — you get a code. In game, run `/maplink <code>`. First
time, this also creates your account (the site walks you through setting a
password). After that you stay linked.

**2. Upload a mapart.** Look at a mapart wall on 6b6t and run `/mapupload`.
The mod reads the maps right out of the item frames and uploads the image to
the site as a draft — publish it there.

### Commands

| Command | What it does |
| --- | --- |
| `/maplink <code>` | Link the game to the site (creates your account the first time). |
| `/mapupload [title]` | Upload the mapart you're looking at. Title optional — guessed from the map names if left off. |
| `/mapselect` | Upload just part of a wall: pick two opposite corners, then `/mapupload`. |
| `/mapuploadall` | A wall holding several maparts? Splits them up and uploads each one separately. |

### Good to know

- Uploads work on **6b6t** (`6b6t.org` and its alt addresses); the upload
  commands are disabled on other servers.
- Wall and floor item frames both work; ceiling frames don't.
- If the exact same mapart is already on the site, you're simply added as an
  owner of it — no duplicate is created.
- New trades on the site ping you in chat.
- The mod checks for updates on its own — it asks before installing
  anything.

## Credits

The Maparts Link mod builds on the original client mod by **BbiBelle**,
creator of the previous 6b6t mapart website. This project is owned and
maintained by **Gamerguy11** and **ComicSquid74273**.

## License

[MIT](LICENSE) — Copyright (c) 2026 Gamerguy11, ComicSquid74273; original
mod work Copyright (c) 2026 BbiBelle.

---

<details>
<summary>How uploads work, building, and releasing (for maintainers)</summary>

### Uploads, exactly

The capture reads the real vanilla map data (not screenshots): per-frame
128px colors (ARGB, rotation-aware), the grid size in item frames, and each
frame's map ID + item name. The image's sha256 goes with the draft request,
so exact duplicates resolve before any bytes move: if that exact image
already exists on the site — anyone's, published or still a draft — you're
simply added as an owner of it instead of creating a new row. Nobody's data
is ever deleted or replaced by this.

A map item's held stack (and which map ID it points at) syncs the instant its
item frame is tracked, but the map's actual pixel content streams in on a
separate channel and can lag behind on a wall with many maps. Composing too
early would silently treat not-yet-loaded frames as missing, either
truncating the wall or splitting one mapart into several — so both
`/mapupload` and `/mapuploadall` wait (up to 20s) for every involved frame's
data to arrive before drawing anything, rather than sampling once.

If an upload step (draft, image PUT, or finalize) fails, it's retried once
automatically after a 5-second delay before being reported as failed — in
both single and bulk (`/mapuploadall`) uploads.

For a floor grid, column runs east and row runs south, as if viewed from
directly above with north at the top.

The version ranges in the table above are the declared Fabric dependency
constraint, not a per-version test matrix — each jar is built once against
its folder's own mappings, so versions past the pin aren't individually
verified.

### Building

```bash
cd mc1.21.4-1.21.11   # or mc26.1+
./build.sh            # or: ./gradlew build
```

The jar lands in `<folder>/build/libs/maparts-link-<version>.jar`. Gradle
needs a JDK with a compiler — on a JRE-only machine the foojay toolchain
resolver downloads one, or install it yourself.

### Releasing

Each Minecraft version range is released **independently** — its own tag,
its own GitHub Release, its own single jar. Tags are named
`<folder>-v<version>`:

- `mc1.21.4-1.21.11-v1.6.1`, `mc1.21.4-1.21.11-v1.6.2`, … for the
  `mc1.21.4-1.21.11/` build
- `mc26.1+-v1.6.1`, `mc26.1+-v1.7.0`, … for the `mc26.1+/` build

The two variants don't need matching version numbers — bump only the one
you're actually shipping.

1. Bump `version` in that folder's `build.gradle` and its
   `src/**/resources/fabric.mod.json`.
2. Commit the version bump.
3. Tag it (prefixed with the folder name) and push the tag:
   ```bash
   git tag mc1.21.4-1.21.11-v1.6.2
   git push origin mc1.21.4-1.21.11-v1.6.2
   ```
4. GitHub Actions (`.github/workflows/release.yml`) reads the folder name off
   the tag prefix, builds only that variant, and publishes a GitHub Release
   for that tag with just its one jar attached — release notes are generated
   from the commits touching that folder since its own last release, so
   changes to the other variant never show up in the wrong changelog.

</details>
