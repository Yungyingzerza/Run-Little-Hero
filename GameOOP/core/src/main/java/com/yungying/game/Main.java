package com.yungying.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.Player.OtherPlayer;

import com.yungying.game.screens.LobbyScreen;
import com.yungying.game.textureLoader.PlayerType;
import io.socket.client.IO;
import io.socket.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    private Socket socket;
    private String MySocketId;

    public static boolean isTextureLoaded = false;

    public static HashMap<String, OtherPlayer> otherPlayer;


    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new LobbyScreen(this));
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
//            System.out.println(e);
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
//                e.printStackTrace(); // Print the stack trace for debugging purposes
            }
        }
    }

    private void addPlayer(String id, float x, float y, float stateTime, boolean isSliding, boolean isJumping, String username, String playerType, boolean isDead){
        if(isDead){
            return;
        }

        OtherPlayer player = new OtherPlayer(playerType, x, y);

        player.setTargetPosition(new Vector2(x, y));
        if(player.getStateTime() == 0) player.setStateTime(stateTime);
        player.setUsername(username);
        player.setIsSliding(isSliding);
        player.setIsJumping(isJumping);

        otherPlayer.put(id, player);
    }


    public void configSocketEvents(){

        if(socket == null){
            return;
        }

        //connect and send message to server
        socket.on(Socket.EVENT_CONNECT, args -> {

            try {
                JSONObject data = new JSONObject();
                data.put("username", "Username001");
                socket.emit("join", data);
                System.out.println("Connected to server");

                MySocketId = socket.id();

                System.out.println(MySocketId);


            } catch (JSONException e) {
//                    e.printStackTrace();
            }

        }).on("getPlayers", args -> {

            if(!isTextureLoaded){
                return;
            }

            JSONArray players = (JSONArray) args[0];

            for(int i = 0; i < players.length(); i++){
                try {
                    JSONObject player = players.getJSONObject(i);
                    String id = player.getString("id");
                    float x = ((Double) player.getDouble("x")).floatValue();
                    float y = ((Double) player.getDouble("y")).floatValue();
                    float stateTime = ((Double) player.getDouble("stateTime")).floatValue();
                    String username = player.getString("username");
                    boolean isSliding = player.getBoolean("isSliding");
                    boolean isJumping = player.getBoolean("isJumping");
                    String playerType = player.getString("playerType");
                    boolean isDead = player.getBoolean("isDead");

                    if(!id.equals(MySocketId)){
                        addPlayer(id, x, y, stateTime, isSliding, isJumping, username, playerType, isDead);
                    }

                } catch (JSONException e) {
//                        e.printStackTrace();
                }
                }

        }).on("playerMoved", args -> {

            if(!isTextureLoaded){
                return;
            }

            try {

                JSONArray players = (JSONArray) args[0];

                for(int i = 0; i < players.length(); i++){

                    float x = ((Double) players.getJSONObject(i).getDouble("x")).floatValue();
                    float y = ((Double) players.getJSONObject(i).getDouble("y")).floatValue();
                    float stateTime = ((Double) players.getJSONObject(i).getDouble("stateTime")).floatValue();
                    boolean isSliding = players.getJSONObject(i).getBoolean("isSliding");
                    boolean isJumping = players.getJSONObject(i).getBoolean("isJumping");
                    String username = players.getJSONObject(i).getString("username");

                    String id = players.getJSONObject(i).getString("id");
                    String playerType = players.getJSONObject(i).getString("playerType");
                    boolean isDead = players.getJSONObject(i).getBoolean("isDead");

                    PlayerType playerType1 = PlayerType.valueOf(playerType);

                    if(otherPlayer.containsKey(id)){
                        if(isDead) otherPlayer.get(id).setPosition(0, 0);
                        otherPlayer.get(id).setTargetPosition(new Vector2(x, y));
                        if(otherPlayer.get(id).getStateTime() == 0f) otherPlayer.get(id).setStateTime(stateTime);
                        otherPlayer.get(id).setIsSliding(isSliding);
                        otherPlayer.get(id).setIsJumping(isJumping);
                        otherPlayer.get(id).setUsername(username);
                        otherPlayer.get(id).setIsDead(isDead);
                        otherPlayer.get(id).setPlayerType(playerType1);
                    }else if(!id.equals(MySocketId)){
                        addPlayer(id, x, y, stateTime, isSliding, isJumping, username, playerType, isDead);
                    }

                }
            } catch (Exception e) {
                // Print the exact exception and its message for better debugging
//                    e.printStackTrace();
            }

        }).on(Socket.EVENT_DISCONNECT, args -> System.out.println("Disconnected from server"));
    }

    public Socket getSocket(){
        return socket;
    }

}
