package com.yungying.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yungying.game.Player.OtherPlayer;

import com.yungying.game.screens.LobbyScreen;
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

    private void addPlayer(String id, float x, float y, float stateTime, String currentFrame, String username){
        OtherPlayer player = new OtherPlayer();

        player.setPosition(x, y);
        player.setStateTime(stateTime);
        player.setCurrentFrameString(currentFrame);
        player.setUsername(username);

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
            JSONArray players = (JSONArray) args[0];

            for(int i = 0; i < players.length(); i++){
                try {
                    JSONObject player = players.getJSONObject(i);
                    String id = player.getString("id");
                    float x = ((Double) player.getDouble("x")).floatValue();
                    float y = ((Double) player.getDouble("y")).floatValue();
                    float stateTime = ((Double) player.getDouble("stateTime")).floatValue();
                    String currentFrame = player.getString("currentFrame");
                    String username = player.getString("username");

                    if(!id.equals(MySocketId)){
                        addPlayer(id, x, y, stateTime, currentFrame, username);
                    }

                } catch (JSONException e) {
//                        e.printStackTrace();
                }
                }

        }).on("playerMoved", args -> {

            try {

                JSONArray players = (JSONArray) args[0];

                for(int i = 0; i < players.length(); i++){

                    float x = ((Double) players.getJSONObject(i).getDouble("x")).floatValue();
                    float y = ((Double) players.getJSONObject(i).getDouble("y")).floatValue();
                    float stateTime = ((Double) players.getJSONObject(i).getDouble("stateTime")).floatValue();
                    String currentFrame = players.getJSONObject(i).getString("currentFrame");
                    String username = players.getJSONObject(i).getString("username");

                    String id = players.getJSONObject(i).getString("id");

                    if(!id.equals(MySocketId)){
                        addPlayer(id, x, y, stateTime, currentFrame, username);
                    }else if(otherPlayer.containsKey(id)){
                        otherPlayer.get(id).setPosition(x, y);
                        otherPlayer.get(id).setStateTime(stateTime);
                        otherPlayer.get(id).setCurrentFrameString(currentFrame);
                        otherPlayer.get(id).setUsername(username);
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
