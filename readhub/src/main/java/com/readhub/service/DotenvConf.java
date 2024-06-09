package com.readhub.service;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvConf {
    Dotenv dotenv = Dotenv.configure().directory("/home/eddev/Documentos/read_hub/readhub/src/main/java/com/readhub/.env").load();

    public String get(String key) {
        return dotenv.get(key);
    }
}
