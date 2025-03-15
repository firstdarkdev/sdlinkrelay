## Simple Discord Link Relay Server

This software, is a simple Relay server, that allows Simple Discord Link powered Minecraft Servers to communicate with each other.

This server allows the bot to relay Chat messages, Join/Leave messages, Advancements and Death messages to other minecraft servers, and discord servers, running Simple Discord Link.


We host a publicly usable instance at `sdlinkrelay.firstdark.dev`.

---

### Self Hosting

Why yes, you can self-host. Why would you do this? Maybe your server is closer to you than our public relay, or, you want full control over your server. Well, that's completely possible.


We provide 3 options to host, and run your own relay server.

### Option 1 - The Docker Way

You can run your own relay server, with docker or docker compose.

#### Running With Docker

```bash
docker run -d \
  --name sdlinkrelay \
  --restart always \
  -p 6500:6500 \
  ghcr.io/firstdarkdev/sdlinkrelay:latest
```

#### Running with Docker Compose

Use our included `docker-compose.yml` from this repo, or, create a new file called `docker-compose.yml` with the following values:

```yaml
services:
  sdlinkrelay_watchtower:
    image: containrrr/watchtower
    command:
      - "--label-enable"
      - "--interval"
      - "30"
      - "--rolling-restart"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock

  sdlinkrelay:
    image: ghcr.io/firstdarkdev/sdlinkrelay:latest
    container_name: "sdlinkrelay"
    restart: always
    labels:
      - "com.centurylinklabs.watchtower.enable=true"
    ports:
      - 6500:6500
```


### Running "bare metal"

You can also run a server without docker. Simple download the latest .jar file from GitHub releases, and use the following command:

```bash
java -jar sdlinkrelay-VERSION.jar
```

### Running as a velocity plugin

You can also run your own relay server on Velocity. Simply download the latest jar from GitHub releases, and slap it into your plugins folder.


---

### Config

By default, the server uses port `6500` for the websocket server. For Velocity and Standalone, you can change this by editing the `sdlinkrelay.json` file that is created the first time the server is started.

When hosting your own relay, don't forget to change the server url in your SDLink config.

---

### FAQ

Q. How safe is the relay server to use?

A: It's as safe as the token you use to configure to link servers together. The Relay Server itself, does not log messages, or store any data passing through it, aside from the open sessions. Messages are also encrypted and decrypted by the clients before passing through the server. In the end, it's up to you to use, or not use this server. Its also up to you, if you want to use our free public instance or not.

---

### License

This server is licensed under the MIT license.