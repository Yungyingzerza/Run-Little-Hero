package com.yungying.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yungying.game.Player.OtherPlayer;
import com.yungying.game.Player.Sakura;
import com.yungying.game.screens.MainMenuScreen;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    private Socket socket;
    private String MySocketId;

    public static HashMap<String, OtherPlayer> otherPlayer;


    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
        otherPlayer = new HashMap<>();

        connectToServer();
        configSocketEvents();

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        disposeSocket();
        batch.dispose();
        System.exit(0);
    }

    public void connectToServer(){
        try {
            socket = IO.socket("https://gameoopws.yungying.com/");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void disposeSocket() {
        if (socket != null) {
            try {
                socket.off();
                socket.disconnect(); // Disconnect the socket
                socket.close(); // Close the socket connection
                socket = null; // Set the socket to null to indicate it's disposed
            } catch (Exception e) {
                e.printStackTrace(); // Print the stack trace for debugging purposes
            }
        }
    }

    private void addPlayer(String id, float x, float y, float stateTime, String currentFrame){
        OtherPlayer player = new OtherPlayer();

        player.setPosition(x, y);
        player.setStateTime(stateTime);
        player.setCurrentFrameString(currentFrame);

        otherPlayer.put(id, player);
    }


    public void configSocketEvents(){

        if(socket == null){
            return;
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            //connect and send message to server
            @Override
            public void call(Object... args) {

                try {
                    JSONObject data = new JSONObject();
                    data.put("username", "yungying");
                    socket.emit("join", data);
                    System.out.println("Connected to server");

                    MySocketId = socket.id();

                    System.out.println(MySocketId);


                } catch (JSONException e) {
//                    e.printStackTrace();
                }

            }


        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray players = (JSONArray) args[0];
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                try {

                    JSONArray players = (JSONArray) args[0];

                    for(int i = 0; i < players.length(); i++){

                        float x = ((Double) players.getJSONObject(i).getDouble("x")).floatValue();
                        float y = ((Double) players.getJSONObject(i).getDouble("y")).floatValue();
                        float stateTime = ((Double) players.getJSONObject(i).getDouble("stateTime")).floatValue();
                        String currentFrame = players.getJSONObject(i).getString("currentFrame");

                        String id = players.getJSONObject(i).getString("id");

                        if(!id.equals(MySocketId)){
                            addPlayer(id, x, y, stateTime, currentFrame);
                        }else if(otherPlayer.containsKey(id)){
                            otherPlayer.get(id).setPosition(x, y);
                            otherPlayer.get(id).setStateTime(stateTime);
                            otherPlayer.get(id).setCurrentFrameString(currentFrame);
                        }

                    }
                } catch (Exception e) {
                    // Print the exact exception and its message for better debugging
//                    e.printStackTrace();
                }

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Disconnected from server");
            }
        });
    }

    public Socket getSocket(){
        return socket;
    }

}
