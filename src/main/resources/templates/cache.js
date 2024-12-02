const redis = require('redis');
const client = redis.createClient();

client.on('connect', () => {
    console.log('Connected to Redis');
});

function getCachedData(key, fetchDataFunction) {
    // Cek apakah data ada di cache
    client.get(key, (err, result) => {
        if (result) {
            // Jika data ada di cache, kembalikan data tersebut
            console.log('Data from cache:', result);
            return JSON.parse(result);
        } else {
            // Jika data tidak ada, ambil dari API atau sumber lain
            fetchDataFunction().then(data => {
                // Simpan data ke Redis cache
                client.setex(key, 3600, JSON.stringify(data));  // Cache selama 1 jam
                console.log('Data from API:', data);
                return data;
            });
        }
    });
}
