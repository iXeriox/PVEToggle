# PVEToggle V2

Welcome to the Git of **PVEToggle**! This repository is a complete rewrite of the popular PVEToggle; This version provides a smoother, cleaner and uncluttered approach at handling mobs at your own command.

Features:

- Permission based commands.
- Combat sequencing (Unable to toggle in combat or up to 10 seconds after)
---

<details>
<summary>## Introduction</summary>

This template provides:
- A clean and well-organised project structure.
- Command examples to help you get started.
- Configuration support for Bukkit/Spigot plugins.
- Tools and setup for GitHub readiness.

</details>

---

<details>
<summary>## Getting Started</summary>

### Prerequisites
- Java Development Kit (JDK) 8 or above
- Maven build tool
- A Bukkit/Spigot server for testing your plugin

### Setting Up the Project
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/minecraft-plugin-template.git
Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
Modify the plugin.yml file with your plugin's name, version, and description.
</details>

<details> <summary>## Building the Plugin</summary>
Run Maven to package the plugin:
```bash
mvn clean package
The generated .jar file will be located in the target/ directory.
</details>

<details> <summary>## Testing </summary>
Copy the generated .jar file into your server's plugins folder.
Start or restart your server.
Test the example command:
Use /example in-game to see a response from the plugin.
</details>

<details> <summary>## Customisation</summary>
Main Class: Modify the main plugin logic in src/main/java/com/example/plugin/TemplatePlugin.java.
Commands: Add new commands by creating classes that implement CommandExecutor and registering them in the onEnable() method.
Event Listeners: Add listeners by creating classes that implement Listener and registering them in onEnable().
</details>

<details> <summary>## Contributing</summary>
Feel free to fork this repository and make it your own! Contributions are welcome through pull requests.

</details>

<details> <summary>## License</summary>
This project is licensed under the MIT License. See the LICENSE file for details.

</details> ```
