const { MongoClient } = require('mongodb');
const fs = require('fs');

const uri = "mongodb://localhost:27017";
const databaseName = "ecommerce";

const collections = [
    { name: "historial", file: "ecommerce.historiales.json" },
    { name: "opiniones", file: "ecommerce.opiniones.json" },
];

async function populateDatabase() {
    const client = new MongoClient(uri);

    try {
        await client.connect();
        console.log("Conectado a MongoDB");

        const db = client.db(databaseName);

        for (const { name, file } of collections) {
            console.log(`Cargando datos en la colección ${name}`);
            const data = JSON.parse(fs.readFileSync(file, 'utf-8'));
            const collection = db.collection(name);

            await collection.deleteMany({});
            const result = await collection.insertMany(data);

            console.log(`${result.insertedCount} documentos insertados en ${name}`);
        }

        console.log("Base de datos poblada correctamente.");
    } catch (error) {
        console.error("Error al poblar la base de datos:", error);
    } finally {
        await client.close();
    }
}

populateDatabase();
