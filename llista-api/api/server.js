const express = require("express");
const cors = require("cors");
const { MongoClient } = require("mongodb");

const llibresRoutes = require("./routes/llibres");
const elementosRoutes = require("./routes/elementos");
const categoriasRoutes = require("./routes/categorias");
const usuariosRoutes = require("./routes/usuarios");

const app = express();

app.use(cors());
app.use(express.json());

const port = process.env.PORT || 3000;
const mongoUrl = process.env.MONGO_URL || "mongodb://localhost:27017";
const dbName = process.env.DB_NAME || "llistaociDB";

const client = new MongoClient(mongoUrl);

async function main() {
    try {
        await client.connect();

        const db = client.db(dbName);
        app.locals.db = db;

        app.get("/", (req, res) => {
            res.json({
                message: "API Llista oci funcionant"
            });
        });

        app.use("/llibres", llibresRoutes);
        app.use("/elementos", elementosRoutes);
        app.use("/categorias", categoriasRoutes);
        app.use("/usuarios", usuariosRoutes);

        app.listen(port, "0.0.0.0", () => {
            console.log(`API escoltant al port ${port}`);
        });
    } catch (error) {
        console.error("Error iniciant servidor:", error);
    }
}

main();