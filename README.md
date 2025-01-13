# Marriage Plugin

A customizable marriage plugin for Minecraft servers built using Spigot/Bukkit API. This plugin allows players to propose marriage, accept/deny proposals, teleport to their spouse, and view the list of all married couples on the server.

## Features

- Propose marriage to another player with `/marry propose <player>`.
- Accept or deny marriage proposals with `/marry propose accept` or `/marry propose deny`.
- View a list of all married couples and how long they've been married with `/marry list`.
- Send a kiss to your spouse with `/marry kiss`, which spawns heart particles at their location.
- Divorce your spouse with `/marry divorce`.
- Teleport to your spouse with `/marry tp`.
- Configurable teleportation delay for `/marry tp`.
- Manage homes with `/sethome`, `/home`, and `/homes`.
- Manage warps with `/setwarp`, `/warp`, `/delwarp`, and `/warps`.
- Teleport to other players with `/tpa`, `/tpaccept`, and `/tpdeny`.

## Commands

### `/marry` Commands
| Command                    | Description                                   |
|----------------------------|-----------------------------------------------|
| `/marry propose <player>`  | Propose marriage to another player.          |
| `/marry propose accept`    | Accept a marriage proposal.                  |
| `/marry propose deny`      | Deny a marriage proposal.                    |
| `/marry list`              | View all married couples on the server.      |
| `/marry kiss`              | Send a kiss to your spouse.                  |
| `/marry divorce`           | Divorce your spouse.                         |
| `/marry tp`                | Teleport to your spouse.                     |

### Home Commands
| Command       | Description                               |
|---------------|-------------------------------------------|
| `/sethome <name>` | Set a home with a specific name.         |
| `/delhome <name>` | Delete a home with a specific name.         |
| `/home <name>`    | Teleport to a specific home.            |
| `/homes`          | List all your homes.                   |

### Warp Commands
| Command           | Description                               |
|-------------------|-------------------------------------------|
| `/setwarp <name>` | Set a global warp with a specific name.  |
| `/delwarp <name>` | Delete a specific warp.                  |
| `/warp <name>`    | Teleport to a specific warp.             |
| `/warps`          | List all available warps.                |
| `/spawn`          | Teleports to the spawn warp.                |

### TPA Commands
| Command           | Description                               |
|-------------------|-------------------------------------------|
| `/tpa <player>`   | Request to teleport to another player.    |
| `/tpahere <player>`   | Request to teleport another player to yourself.    |
| `/tpaccept`       | Accept a teleport request.               |
| `/tpdeny`         | Deny a teleport request.                 |

## Configuration

The plugin's settings can be customized in the `config.yml` file. Below is an example configuration:

```yaml
messages:
  welcome: "Welcome back, {player}!"
home:
  teleport_delay: 2
warp:
  teleport_delay: 2
tpa:
  teleport_delay: 2
marry:
  teleport_delay: 2
```

## Permissions

| Permission         | Description                              |
|--------------------|------------------------------------------|
| `marry.use`        | Grants access to all `/marry` commands.  |
| `csystem.home`         | Grants access to all home commands.      |
| `csystem.warp`         | Grants access to /warp and /warps.      |
| `csystem.warp.admin`         | Grants access to /setwarp and /delwarp.      |
| `csystem.tpa`          | Grants access to all TPA commands.       |
| `csystem.marry`         | Grants access to /marry commands.      |
| `csystem.marry.tp`          | Grants access to /marry tp.       |
| `csystem.spawn`          | Grants access to /spawn.       |

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

