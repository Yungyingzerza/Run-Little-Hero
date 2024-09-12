const server = require('http').createServer();
const io = require('socket.io')(server);

let players = [];

server.listen(3000, () => {
    console.log('Server running on port 3000');
})


io.on('connection', (socket) => {

    socket.on('disconnect', () => {

        players = players.filter(p => p.id !== socket.id);
        //who left
        socket.broadcast.emit('playerLeft', { id: socket.id});

    });
    
    socket.emit('getPlayers', players);
    socket.broadcast.emit('newPlayer', { id: socket.id });

    socket.on('join', (player) => {
        players.push({ id: socket.id, ...player });
    })

    socket.on('playerMoved', (player) => {
        const index = players.findIndex(p => p.id === socket.id);
        players[index] = { ...player };
        console.log(players);
        socket.broadcast.emit('playerMoved', player);
    })

});

