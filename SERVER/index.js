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

    socket.on('join', (player) => {
        players.push({ id: socket.id, ...player });
    })

    socket.on('playerMoved', (player) => {
        const index = players.findIndex(p => p.id === socket.id);

        if(index === -1) {
            return;
        }

        players[index] = { ...players[index], ...player };
        socket.broadcast.emit('playerMoved', players);

        console.log(players);
    })

});

