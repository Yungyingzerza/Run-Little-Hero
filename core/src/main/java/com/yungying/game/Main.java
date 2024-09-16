package com.yungying.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yungying.game.Player.Player;
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

    public static HashMap<String, Player> otherPlayer;


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

    private void addPlayer(String id, float x, float y, boolean isJumping, boolean isSliding, boolean isHighestJump, int jumpCounter, boolean isColliding){
        Player player = new Player(true);
        player.setPosition(x, y);
        player.setJumping(isJumping);
        player.setSliding(isSliding);
        player.setHighestJump(isHighestJump);
        player.setJumpCounter(jumpCounter);
        player.setColliding(isColliding);

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
                        boolean isJumping = players.getJSONObject(i).getBoolean("isJumping");
                        boolean isSliding = players.getJSONObject(i).getBoolean("isSliding");
                        boolean isHighestJump = players.getJSONObject(i).getBoolean("isHighestJump");
                        int jumpCounter = players.getJSONObject(i).getInt("jumpCounter");
                        boolean isColliding = players.getJSONObject(i).getBoolean("isColliding");

                        String id = players.getJSONObject(i).getString("id");


                        if(!id.equals(MySocketId)){
                            addPlayer(id, x, y, isJumping, isSliding, isHighestJump, jumpCounter, isColliding);
                        }else if(otherPlayer.containsKey(id)){
                            otherPlayer.get(id).setPosition(x, y);
                            otherPlayer.get(id).setJumping(isJumping);
                            otherPlayer.get(id).setSliding(isSliding);
                            otherPlayer.get(id).setHighestJump(isHighestJump);
                            otherPlayer.get(id).setJumpCounter(jumpCounter);
                            otherPlayer.get(id).setColliding(isColliding);
                        }

                    }
                } catch (Exception e) {
                    // Print the exact exception and its message for better debugging
//                    e.printStackTrace();
                }

            }
        });
    }

    public Socket getSocket(){
        return socket;
    }

}
