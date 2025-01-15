# Seb's SystemHomes Plugin

A customizable plugin for Minecraft servers built using Spigot/Bukkit API. This plugin allows players to sethomes, warp and tpa.

## Features

- Manage homes with `/sethome`, `/home`, and `/homes`.
- Manage warps with `/setwarp`, `/warp`, `/delwarp`, and `/warps`.
- Teleport to other players with `/tpa`, `/tpaccept`, and `/tpdeny`.

## Commands

### Home Commands
| Command           | Description                         |
|-------------------|-------------------------------------|
| `/sethome <name>` | Set a home with a specific name.    |
| `/delhome <name>` | Delete a home with a specific name. |
| `/home <name>`    | Teleport to a specific home.        |
| `/homes`          | List all your homes.                |

### Warp Commands
| Command           | Description                             |
|-------------------|-----------------------------------------|
| `/setwarp <name>` | Set a global warp with a specific name. |
| `/delwarp <name>` | Delete a specific warp.                 |
| `/warp <name>`    | Teleport to a specific warp.            |
| `/warps`          | List all available warps.               |
| `/spawn`          | Teleports to the spawn warp.            |

### TPA Commands
| Command             | Description                                     |
|---------------------|-------------------------------------------------|
| `/tpa <player>`     | Request to teleport to another player.          |
| `/tpahere <player>` | Request to teleport another player to yourself. |
| `/tpaccept`         | Accept a teleport request.                      |
| `/tpdeny`           | Deny a teleport request.                        |

## Configuration

The plugin's settings can be customized in the `config.yml` file. Below is an example configuration:

```yaml
home:
  teleport_delay: 2
warp:
  teleport_delay: 2
tpa:
  teleport_delay: 2
```

## Permissions

| Permission               | Description                             |
|--------------------------|-----------------------------------------|
| `systemhomes.home`       | Grants access to all home commands.     |
| `systemhomes.warp`       | Grants access to /warp and /warps.      |
| `systemhomes.warp.admin` | Grants access to /setwarp and /delwarp. |
| `systemhomes.tpa`        | Grants access to all TPA commands.      |
| `systemhomes.spawn`      | Grants access to /spawn.                |

## Installation

1. Download the plugin's `.jar` file.
2. Place the `.jar` file in your server's `plugins` folder.
3. Restart or reload your server.
4. Edit the `config.yml` file in the `plugins/system` folder to customize settings.
5. Use the commands in-game to enjoy the plugin!

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

This project is licensed under the MIT License. See the `LICENSE` file for details.

