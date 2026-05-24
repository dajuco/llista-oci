const express = require("express");
const router = express.Router();

router.get("/", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const usuarios = await db
            .collection("usuarios")
            .find({}, { projection: { _id: 0 } })
            .toArray();

        res.json(usuarios);
    } catch (error) {
        res.status(500).json({ error: "Error obtenint usuaris" });
    }
});

router.get("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;

        const usuario = await db
            .collection("usuarios")
            .findOne({
                $or: [{ id: id }, { username: id }]
            }, { projection: { _id: 0 } });

        if (!usuario) return res.status(404).json({ error: "Usuari no trobat" });

        res.json(usuario);
    } catch (error) {
        res.status(500).json({ error: "Error obtenint usuari" });
    }
});

router.post("/", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const u = req.body;

        if (!u.type || !u.username || !u.password || !u.display) {
            return res.status(400).json({ error: "Falten camps obligatoris" });
        }

        if (!u.id) {
            u.id = u.username;
        }

        const existent = await db.collection("usuarios").findOne({
            $or: [{ id: u.id }, { username: u.username }]
        });
        if (existent) return res.status(409).json({ error: "Ja existeix un usuari amb aquest id" });

        await db.collection("usuarios").insertOne(u);

        res.status(201).json({ message: "Usuari creat correctament" });
    } catch (error) {
        res.status(500).json({ error: "Error creant usuari" });
    }
});

router.put("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;
        const updates = req.body;

        if (!updates.id) {
            updates.id = id;
        }

        const result = await db.collection("usuarios").updateOne({
            $or: [{ id: id }, { username: id }]
        }, { $set: updates });

        if (result.matchedCount === 0) return res.status(404).json({ error: "Usuari no trobat" });

        res.json({ message: "Usuari actualitzat" });
    } catch (error) {
        res.status(500).json({ error: "Error actualitzant usuari" });
    }
});

router.delete("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;

        const result = await db.collection("usuarios").deleteOne({
            $or: [{ id: id }, { username: id }]
        });

        if (result.deletedCount === 0) return res.status(404).json({ error: "Usuari no trobat" });

        res.json({ message: "Usuari eliminat" });
    } catch (error) {
        res.status(500).json({ error: "Error eliminant usuari" });
    }
});

module.exports = router;

