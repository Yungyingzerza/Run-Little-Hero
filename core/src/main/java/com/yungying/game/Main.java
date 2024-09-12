package com.yungying.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yungying.game.Player.Player;
import com.yungying.game.screens.MainGameScreen;
import com.yungying.game.screens.MainMenuScreen;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    private Socket socket;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
        connectToServer();
        configSocketEvents();

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        socket.disconnect();
    }

    public void connectToServer(){
        try {
            socket = IO.socket("http://localhost:3000");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            //connect and send message to server
            @Override
            public void call(Object... args) {

                try {
                    JSONObject data = new JSONObject();
                    data.put("username", "yungying");
                    socket.emit("join", data);
                    System.out.println("Connected to server");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray players = (JSONArray) args[0];
                System.out.println(players);
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                try {
                    JSONArray players = (JSONArray) args[0];

                    for (int i = 0; i < players.length(); i++) {
                        JSONObject player = players.getJSONObject(i);

                        // Check if 'username' field exists
                        String username = player.has("username") ? player.getString("username") : "Unknown";

                        // Check if 'x' and 'y' fields exist
                        double x = player.has("x") ? player.getDouble("x") : 0.0;
                        double y = player.has("y") ? player.getDouble("y") : 0.0;

                        // Check if 'score' field exists
                        int score = player.has("score") ? player.getInt("score") : 0;

                        // Print player details

                        Player otherPlayer = new Player("characters/Tee/TeeRunLeft.png", x, y, score, username);

                        MainGameScreen.otherPlayers.add(otherPlayer);
                    }

                    // Print size of otherPlayers collection
                    System.out.println(MainGameScreen.otherPlayers.size());

                } catch (Exception e) {
                    // Print the exact exception and its message for better debugging
                    e.printStackTrace();
                }

            }
        });
    }

    public Socket getSocket(){
        return socket;
    }

}
